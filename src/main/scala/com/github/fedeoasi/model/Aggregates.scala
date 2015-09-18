package com.github.fedeoasi.model

object Aggregates {
  case class StationAndRides(station: Station, dailyRideCount: DailyRideCount)
  case class LineAndRides(line: Line, dailyRideCount: DailyRideCount)
}
