package com.github.fedeoasi.parsing

import com.github.fedeoasi.model.Station
import com.github.tototoshi.csv.CSVReader

class StationParser {
  def parse(filename: String): Seq[Station] = {
    val reader = CSVReader.open(filename)
    reader.allWithHeaders().map { row =>
      val stopId = row("STOP_ID").toInt
      val direction = row("DIRECTION_ID")
      Station(stopId, direction, row("STOP_NAME"))
    }
  }
}
