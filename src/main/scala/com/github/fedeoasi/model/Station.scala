package com.github.fedeoasi.model

case class Station(stopId: Int, mapId: Int, direction: String, stopName: String, lines: Seq[Line])
