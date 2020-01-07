package com.github.fedeoasi.parsing

import com.github.fedeoasi.model.Lines._
import com.github.fedeoasi.model._
import com.github.tototoshi.csv.CSVReader

object StationParser {
  def parse(filename: String): Seq[Station] = {
    val reader = CSVReader.open(filename)
    reader.allWithHeaders().map { row =>
      val stopId = row("STOP_ID").toInt
      val mapId = row("MAP_ID").toInt
      val direction = row("DIRECTION_ID")
      val lines = extractLines(row)
      Station(stopId, mapId, direction, row("STOP_NAME"), lines)
    }
  }

  private def extractLines(row: Map[String, String]): Seq[Line] = {
    Seq(
      optionalLine(row, "RED", RedLine),
      optionalLine(row, "BLUE", BlueLine),
      optionalLine(row, "G", GreenLine),
      optionalLine(row, "BRN", BrownLine),
      optionalLine(row, "P", PurpleLine),
      optionalLine(row, "Pexp", PurpleLine),
      optionalLine(row, "Y", YellowLine),
      optionalLine(row, "Pnk", PinkLine),
      optionalLine(row, "O", OrangeLine)
    ).flatten
  }

  private def optionalLine(row: Map[String, String], key: String, line: Line): Option[Line] = {
    if (row(key) == "true") Some(line) else None
  }
}
