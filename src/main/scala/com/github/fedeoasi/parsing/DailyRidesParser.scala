package com.github.fedeoasi.parsing

import com.github.fedeoasi.model.{StationReference, DailyRideCount}
import com.github.tototoshi.csv.CSVReader
import org.joda.time.LocalDate
import ParsingUtil._

class DailyRidesParser {
  def parse(filename: String): Seq[DailyRideCount] = {
    val reader = CSVReader.open(filename)
    reader.allWithHeaders().map { row =>
      val stationId = row("station_id").toInt
      val date = LocalDate.parse(row("date"), dateFormatter)
      val rides = row("rides").toLong
      DailyRideCount(StationReference(stationId, row("stationname")), date, row("daytype"), rides)
    }
  }
}
