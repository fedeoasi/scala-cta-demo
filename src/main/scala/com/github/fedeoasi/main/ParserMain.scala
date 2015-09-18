package com.github.fedeoasi.main

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model.{Station, DailyRideCount}
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

    println("Computing Top dailyRideCount...")
    val maxRides = dailyRideCounts.max(orderingByRides)
    println(maxRides)

    println("Computing Top K dailyRideCount...")
    val busiestStationDay = dailyRideCounts.sortBy(_.rides).reverse.take(K)
    println(busiestStationDay)

    println(s"Computing Top $K busiest days")
    val ridesByDay = dailyRideCounts.groupBy(_.date)
    val days = rankGroupedRideCounts(ridesByDay)
    println(days)

    println(s"Computing Top $K busiest Years")
    val ridesByYear = dailyRideCounts.groupBy(_.date.getYear)
    val years = rankGroupedRideCounts(ridesByYear)
    println(years)

    println(s"Computing Top $K Busiest stations of all time")
    val ridesByStation = dailyRideCounts.groupBy(_.station)
    val busiestStations = rankGroupedRideCounts(ridesByStation)
    println(busiestStations)

    println("Computing Top K lines")
    val ridesAndStations = computeRidesAndStations(stations, dailyRideCounts)
    val lineAndRidesSeq = ridesAndStations.flatMap { ridesAndStation =>
      ridesAndStation.station.lines.map { line =>
        LineAndRides(line, ridesAndStation.dailyRideCount)
      }
    }
    val byLine = lineAndRidesSeq.groupBy(_.line)
    val busiestLines = byLine.mapValues { lineAndRides =>
      lineAndRides.map(_.dailyRideCount.rides).sum
    }.toSeq.sortBy(_._2).reverse.take(K)
    println(busiestLines)
  }
  
  def rankGroupedRideCounts[T](groupedRideCounts: Map[T, Seq[DailyRideCount]],
                               k: Int = K): Seq[(T, Long)] = {
    groupedRideCounts.mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }.toSeq.sortBy(_._2).reverse.take(K)
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
}
