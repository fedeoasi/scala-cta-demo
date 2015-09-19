package com.github.fedeoasi.main

import java.text.NumberFormat

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model.{HasRides, Station, DailyRideCount}
import com.github.fedeoasi.parsing.{StationParser, DailyRidesParser}
import com.github.fedeoasi.model.Aggregates._

object ParserMain {
  val orderingByRides = Ordering.by[DailyRideCount, Long](_.rides)
  val K = 10
  val stationParser = new StationParser
  val RidesParser = new DailyRidesParser

  def main(args: Array[String]) {
    println("Parsing stations file...")
    val stations = stationParser.parse(ctaLStationsFile)
    println(s"Found ${stations.size} stations")

    println("Parsing daily rides file...")
    val dailyRideCounts = RidesParser.parse(ctaDailyRidesFile)
    println(s"Found ${dailyRideCounts.size} daily ride entries")

    println("Computing Distinct Stations...")
    val distinctStations = dailyRideCounts.map(_.station).toSet
    println(distinctStations.size)

    println("Computing Top Station Day...")
    val maxRides = dailyRideCounts.max(orderingByRides)
    println(maxRides)

    println(s"Computing Top $K Station Days...")
    val busiestStationDay = dailyRideCounts.sortBy(_.rides).reverse.take(K)
    println(busiestStationDay)

    println(s"Computing Top $K Busiest Days")
    val ridesByDay = dailyRideCounts.groupBy(_.date)
    val days = rankGroupedRideCounts(ridesByDay)
    prettyPrint(days, s"Busiest Days")

    println(s"Computing Top $K Busiest Years")
    val ridesByYear = dailyRideCounts.groupBy(_.date.getYear)
    val years = rankGroupedRideCounts(ridesByYear)
    prettyPrint(years, s"Busiest Years")

    println(s"Computing Top $K Busiest stations of all time")
    val ridesByStation = dailyRideCounts.groupBy(_.station)
    val busiestStations = rankGroupedRideCounts(ridesByStation)
    prettyPrint(busiestStations, s"Busiest Stations")

    println(s"Computing Top $K Train Lines")
    val ridesAndStations = computeRidesAndStations(stations, dailyRideCounts)
    val lineAndRidesSeq = ridesAndStations.flatMap { ridesAndStation =>
      ridesAndStation.station.lines.map { line =>
        LineAndRides(line, ridesAndStation.dailyRideCount)
      }
    }
    val byLine = lineAndRidesSeq.groupBy(_.line)
    val busiestLines = rankGroupedRideCounts(byLine)
    prettyPrint(busiestLines, s"Busiest Stations")
  }
  
  def rankGroupedRideCounts[T](groupedRideCounts: Map[T, Seq[HasRides]],
                               k: Int = K): Seq[ScoredResult[_]] = {
    val topK = groupedRideCounts.mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }.toSeq.sortBy(_._2).reverse.take(K)
    val result = topK.map { r =>
      ScoredResult(r._1, r._2)
    }
    result
  }

  def computeRidesAndStations(stations: Seq[Station], rideCounts: Seq[DailyRideCount]): Seq[StationAndRides] = {
    val stationsById = stations.groupBy(_.mapId)
    val ridesAndStations = rideCounts.flatMap { rideCount =>
      val optionalStations = stationsById.get(rideCount.station.stationId)
      optionalStations.map { seq =>
        StationAndRides(seq.head, rideCount)
      }
    }
    ridesAndStations
  }

  def prettyPrint(results: Seq[ScoredResult[_]], title: String): Unit = {
    println(s"Results for $title")
    results.foreach { r =>
      println(s"${NumberFormat.getInstance().format(r.score)}\t${r.value}")
    }
  }

  case class ScoredResult[T](value: T, score: Long)
}
