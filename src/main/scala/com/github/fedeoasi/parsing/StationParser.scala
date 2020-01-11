package com.github.fedeoasi.parsing

import com.github.fedeoasi.model.Line._
import com.github.fedeoasi.model._
import com.github.tototoshi.csv.CSVReader

object StationParser {
  private val allLinesAndCodes = Seq(
    (RedLine, "RED"),
    (BlueLine, "BLUE"),
    (GreenLine, "G"),
    (BrownLine, "BRN"),
    (PurpleLine, "P"),
    (PurpleLine, "Pexp"),
    (YellowLine, "Y"),
    (PinkLine, "Pnk"),
    (OrangeLine, "O")
  )

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
    allLinesAndCodes.flatMap { case (line, lineCode) =>
      if (row(lineCode) == "true") Some(line) else None
    }
  }
}
