package com.github.fedeoasi.model

sealed trait  Line

object Lines {
  case object RedLine extends Line
  case object BlueLine extends Line
  case object GreenLine extends Line
  case object BrownLine extends Line
  case object PurpleLine extends Line
  case object YellowLine extends Line
  case object PinkLine extends Line
  case object OrangeLine extends Line
}
