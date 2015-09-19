package com.github.fedeoasi.model

import org.joda.time.LocalDate

trait HasRides {
  def rides: Long
}

case class DailyRideCount(station: StationReference, date: LocalDate, dayType: String, rides: Long) extends HasRides

case class StationReference(stationId: Int, stationName: String)
