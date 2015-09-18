package com.github.fedeoasi.main

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model.DailyRideCount
import com.github.fedeoasi.parsing.{StationParser, DailyRidesParser}

object ParserMain {
  val orderingByRides: Ordering[DailyRideCount] = Ordering.by[DailyRideCount, Long](_.rides)
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

    println("Computing Top dailyRideCount...")
    val maxRides = dailyRideCounts.max(orderingByRides)
    println(maxRides)

    println("Computing Distinct Stations...")
    val distinctStations = dailyRideCounts.map(_.station).toSet
    println(distinctStations.size)

    println(s"Top $K busiest days")
    val ridesByDay = dailyRideCounts.groupBy(_.date)
    val days = rankGroupedRideCounts(ridesByDay)
    println(days)

    println(s"Top $K busiest Years")
    val ridesByYear = dailyRideCounts.groupBy(_.date.getYear)
    val years = rankGroupedRideCounts(ridesByYear)
    println(years)

    println(s"Top $K Busiest stations of all time")
    val ridesByStation = dailyRideCounts.groupBy(_.station)
    val busiestStations = rankGroupedRideCounts(ridesByStation)
    println(busiestStations)
  }
  
  def rankGroupedRideCounts[T](groupedRideCounts: Map[T, Seq[DailyRideCount]],
                               k: Int = K): Seq[(T, Long)] = {
    groupedRideCounts.mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }.toSeq.sortBy(_._2).reverse.take(K)
  }
}
