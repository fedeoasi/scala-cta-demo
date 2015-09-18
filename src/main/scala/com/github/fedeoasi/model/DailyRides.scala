package com.github.fedeoasi.model

import org.joda.time.LocalDate

case class DailyRides(stationId: Int, stationName: String, date: LocalDate, dayType: String, rides: Int)
