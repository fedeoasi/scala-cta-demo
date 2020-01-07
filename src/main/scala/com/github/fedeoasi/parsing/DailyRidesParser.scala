package com.github.fedeoasi.parsing

import com.github.fedeoasi.model.{DayType, StationReference, DailyRideCount}
import com.github.tototoshi.csv.CSVReader
import java.time.LocalDate
import ParsingUtil._

object DailyRidesParser {
  def parse(filename: String): Seq[DailyRideCount] = {
    val reader = CSVReader.open(filename)
    reader.allWithHeaders().map { row =>
      val stationId = row("station_id").toInt
      val date = LocalDate.parse(row("date"), dateFormatter)
      val rides = row("rides").toLong
      val dayType = DayType(row("daytype"))
      DailyRideCount(StationReference(stationId, row("stationname")), date, dayType, rides)
    }
  }
}
