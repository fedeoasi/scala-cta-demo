package com.github.fedeoasi.main

import java.text.NumberFormat

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model.Aggregates._
import com.github.fedeoasi.model.{DailyRideCount, Station}
import com.github.fedeoasi.parsing.{DailyRidesParser, StationParser}
import com.github.fedeoasi.ranking.RideRanker._
import org.joda.time.LocalDate

import scala.Console._

object ParserMain {
  val orderingByRides = Ordering.by[DailyRideCount, Long](_.rides)
  val stationParser = new StationParser
  val RidesParser = new DailyRidesParser
  val now = LocalDate.now

  def main(args: Array[String]) {
    printSeparator()
    println("Parsing stations file...")
    val stations = stationParser.parse(ctaLStationsFile)
    println(s"Found ${stations.size} stations")

    println("Parsing daily rides file...")
    val dailyRideCounts = RidesParser.parse(ctaDailyRidesFile)
    println(s"Found ${dailyRideCounts.size} daily ride entries")

    printSeparator()
    println("All Time Statistics")
    computeStatistics(stations, dailyRideCounts)

    printSeparator()
    println("Current Year Statistics")
    computeStatistics(stations, dailyRideCounts.filter(_.date.getYear == now.getYear))
  }

  def computeStatistics(stations: Seq[Station], dailyRideCounts: Seq[DailyRideCount]): Unit = {
    println(s"Computing Distinct Stations...")
    val distinctStations = dailyRideCounts.map(_.station).toSet
    println(distinctStations.size)

    println("Computing Top Number of Rides in one day...")
    val maxRides = dailyRideCounts.map(_.rides).max
    println(maxRides)

    println(s"Computing Top $K Station Days...")
    val busiestStationDays = dailyRideCounts.sortBy(_.rides).reverse.take(K)
    val scoredStationDays = busiestStationDays.map { dailyRideCount =>
      ScoredResult(dailyRideCount, dailyRideCount.rides)
    }
    prettyPrint(scoredStationDays, "Station Days")

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

    println(s"Computing Top $K Busiest day types")
    val ridesByDayType = dailyRideCounts.groupBy(_.dayType)
    val busiestDayTypes = rankGroupedRideCounts(ridesByDayType)
    prettyPrint(busiestDayTypes, s"Busiest Day Type")

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
    val colors = Seq(GREEN, YELLOW, RED)
    println(s"Results for $title")
    results.zipWithIndex.foreach { case (r, i) =>
      val highlight = if (i < colors.size) colors(i) else ""
      println(s"$highlight${NumberFormat.getInstance().format(r.score)}\t${r.value}$RESET")
    }
  }

  private def printSeparator(): Unit = {
    println("-----------------------------------------------")
  }
}
