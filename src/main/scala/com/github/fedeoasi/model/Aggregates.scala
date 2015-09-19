package com.github.fedeoasi.model

object Aggregates {
  case class StationAndRides(station: Station, dailyRideCount: DailyRideCount) extends HasRides {
    override def rides: Long = dailyRideCount.rides
  }
  case class LineAndRides(line: Line, dailyRideCount: DailyRideCount) extends HasRides {
    override def rides: Long = dailyRideCount.rides
  }
}
