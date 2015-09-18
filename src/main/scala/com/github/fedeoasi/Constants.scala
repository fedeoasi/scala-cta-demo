package com.github.fedeoasi

import java.io.File

object Constants {
  val / = File.separator
  val ctaDailyRidesFile = s"files${/}CTA_-_Ridership_-__L__Station_Entries_-_Daily_Totals.csv"
  val ctaLStationsFile = s"files${/}CTA_-_System_Information_-_List_of__L__Stops.csv"
}
