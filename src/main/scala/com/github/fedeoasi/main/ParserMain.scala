package com.github.fedeoasi.main

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.model.DailyRideCount
import com.github.fedeoasi.parsing.DailyRidesParser

object ParserMain {
  def main(args: Array[String]) {
    val parser = new DailyRidesParser()

    println("Parsing input file...")
    val dailyRideCounts = parser.parse(ctaFile)

    println("Computing Top dailyRideCount...")
    val maxRides = dailyRideCounts.max(Ordering.by[DailyRideCount, Int](_.rides))
    println(maxRides)

    println("Computing Distinct Stations...")
    val distinctStations = dailyRideCounts.map(_.station).toSet
    println(distinctStations.size)
    println(distinctStations)
  }
}
