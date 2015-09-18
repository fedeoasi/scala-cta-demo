package com.github.fedeoasi.main

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model.DailyRideCount
import com.github.fedeoasi.parsing.{StationParser, DailyRidesParser}

object ParserMain {
  val orderingByRides: Ordering[DailyRideCount] = Ordering.by[DailyRideCount, Long](_.rides)
  val K = 10

  def main(args: Array[String]) {
    println("Parsing stations file...")
    val stationParser = new StationParser
    val stations = stationParser.parse(ctaLStationsFile)
    println(s"Found ${stations.size} stations")

    println("Parsing daily rides file...")
    val RidesParser = new DailyRidesParser()
    val dailyRideCounts = RidesParser.parse(ctaDailyRidesFile)

    println("Computing Top dailyRideCount...")
    val maxRides = dailyRideCounts.max(orderingByRides)
    println(maxRides)

    println("Computing Distinct Stations...")
    val distinctStations = dailyRideCounts.map(_.station).toSet
    println(distinctStations.size)

    println(s"Top $K busiest days")
    val ridesByDay = dailyRideCounts.groupBy(_.date).mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }
    val days = ridesByDay.toSeq.sortBy(_._2).reverse.take(K)
    println(days)

    println(s"Top $K busiest Years")
    val ridesByYear = dailyRideCounts.groupBy(_.date.getYear).mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }
    val years = ridesByYear.toSeq.sortBy(_._2).reverse.take(K)
    println(years)

    println(s"Top $K Busiest stations of all time")
    val ridesByStation = dailyRideCounts.groupBy(_.station).mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }
    val busiestStations = ridesByStation.toSeq.sortBy(_._2).reverse.take(K)
    println(busiestStations)
  }
}
