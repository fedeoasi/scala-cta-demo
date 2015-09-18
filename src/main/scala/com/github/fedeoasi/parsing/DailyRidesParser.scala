package com.github.fedeoasi.parsing

import com.github.fedeoasi.model.DailyRides
import com.github.tototoshi.csv.CSVReader
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class DailyRidesParser {
  val dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")

  def parse(filename: String): Seq[DailyRides] = {
    val reader = CSVReader.open(filename)
    reader.allWithHeaders().map { row =>
      val stationId = row("station_id").toInt
      val date = LocalDate.parse(row("date"), dateFormatter)
      val rides = row("rides").toInt
      DailyRides(stationId, row("stationname"), date, row("daytype"), rides)
    }
  }
}
