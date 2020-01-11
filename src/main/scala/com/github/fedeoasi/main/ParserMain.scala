package com.github.fedeoasi.main

import java.text.NumberFormat

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model._
import com.github.fedeoasi.parsing.{DailyRidesParser, StationParser}
import com.github.fedeoasi.ranking.RideRanker
import com.github.fedeoasi.ranking.RideRanker._
import java.time.LocalDate

import scala.Console._

object ParserMain {
  private val now = LocalDate.now

  def main(args: Array[String]) {
    printSeparator()
    println("Parsing stations file...")
    val stations = StationParser.parse(ctaLStationsFile)
    println(s"Found ${stations.size} stations")

    println("Parsing daily rides file...")
    val dailyRideCounts = DailyRidesParser.parse(ctaDailyRidesFile)
    println(s"Found ${dailyRideCounts.size} daily ride entries")

    printSeparator()
    println("All Time Statistics")
    printStatistics(stations, dailyRideCounts)

    val currentYearData = dailyRideCounts.filter(_.date.getYear == now.getYear)
    if (currentYearData.nonEmpty) {
      printSeparator()
      println("Current Year Statistics")
      printStatistics(stations, currentYearData)
    }
  }

  def printStatistics(stations: Seq[Station], dailyRideCounts: Seq[DailyRideCount]): Unit = {
    println(s"Computing Distinct Stations...")
    val distinctStations = dailyRideCounts.map(_.station).toSet
    println(distinctStations.size)

    println("Computing Top Number of Rides in one day...")
    val maxRides = dailyRideCounts.map(_.rides).max
    println(maxRides)

    val k = RideRanker.K

    println(s"Computing Top $k Station Days...")
    prettyPrint(rankRideCounts(dailyRideCounts, k)(by = identity), "Station Days")

    println(s"Computing $k Busiest Days...")
    val days = rankRideCounts(dailyRideCounts, k)(by = _.date)
    prettyPrint(days, s"Busiest Days")

    println(s"Computing $k Busiest Years...")
    val years = rankRideCounts(dailyRideCounts, k)(by = _.date.getYear)
    prettyPrint(years, s"Busiest Years")

    println(s"Computing $k Busiest stations of all time...")
    val busiestStations = rankRideCounts(dailyRideCounts, k)(by = _.station)
    prettyPrint(busiestStations, s"Busiest Stations")

    println(s"Computing $k Busiest day types...")
    val busiestDayTypes = rankRideCounts(dailyRideCounts, k)(by = _.dayType)
    prettyPrint(busiestDayTypes, s"Busiest Day Type")

    println(s"Computing $k Busiest Train Lines...")
    val ridesAndStations = computeRidesAndStations(stations, dailyRideCounts)
    val lineAndRidesSeq = ridesAndStations.flatMap { ridesAndStation =>
      ridesAndStation.station.lines.map { line =>
        LineAndRides(line, ridesAndStation.dailyRideCount)
      }
    }
    val busiestLines = rankRideCounts(lineAndRidesSeq, k)(by = _.line)
    prettyPrint(busiestLines, s"Busiest Stations")
  }

  def computeRidesAndStations(stations: Seq[Station], rideCounts: Seq[DailyRideCount]): Seq[StationAndRides] = {
    val stationsById = stations.groupBy(_.mapId)
    val ridesAndStations = rideCounts.flatMap { rideCount =>
      stationsById.get(rideCount.station.stationId).map { seq =>
        StationAndRides(seq.head, rideCount)
      }
    }
    ridesAndStations
  }

  def prettyPrint(results: Seq[ScoredResult[_]], title: String): Unit = {
    val colors = Seq(GREEN, YELLOW, RED)
    printSeparator()
    println(s"${CYAN}Results for $title$RESET")
    printSeparator()
    results.zipWithIndex.foreach { case (r, i) =>
      val highlight = if (i < colors.size) colors(i) else ""
      val formattedScore = NumberFormat.getInstance().format(r.score)
      println(s"$highlight$formattedScore\t${r.value}$RESET")
    }
  }

  private def printSeparator(): Unit = {
    println("-----------------------------------------------")
  }
}
