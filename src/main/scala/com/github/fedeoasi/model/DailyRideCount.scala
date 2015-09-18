package com.github.fedeoasi.model

import org.joda.time.LocalDate

case class DailyRideCount(station: StationReference, date: LocalDate, dayType: String, rides: Long)

case class StationReference(stationId: Int, stationName: String)
