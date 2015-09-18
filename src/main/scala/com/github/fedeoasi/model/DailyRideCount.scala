package com.github.fedeoasi.model

import org.joda.time.LocalDate

case class DailyRideCount(station: Station, date: LocalDate, dayType: String, rides: Int)

case class Station(stationId: Int, stationName: String)