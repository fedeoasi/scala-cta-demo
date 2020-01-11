package com.github.fedeoasi.model

import java.time.LocalDate

trait HasRides {
  def rides: Long
}

case class DailyRideCount(station: StationReference, date: LocalDate, dayType: DayType, rides: Long) extends HasRides

case class StationReference(stationId: Int, stationName: String)

case class Station(stopId: Int, mapId: Int, direction: String, stopName: String, lines: Seq[Line])

sealed trait Line

object Line {
  case object RedLine extends Line
  case object BlueLine extends Line
  case object GreenLine extends Line
  case object BrownLine extends Line
  case object PurpleLine extends Line
  case object YellowLine extends Line
  case object PinkLine extends Line
  case object OrangeLine extends Line
}

sealed trait DayType

object DayType {
  case object WeekDay extends DayType
  case object Saturday extends DayType
  case object SundayOrHoliday extends DayType

  def apply(dayType: String): DayType = dayType match {
    case "W" => WeekDay
    case "A" => Saturday
    case "U" => SundayOrHoliday
    case _ => throw new IllegalArgumentException(s"Unrecognized dayType $dayType")
  }
}

case class StationAndRides(station: Station, dailyRideCount: DailyRideCount) extends HasRides {
  override def rides: Long = dailyRideCount.rides
}
case class LineAndRides(line: Line, dailyRideCount: DailyRideCount) extends HasRides {
  override def rides: Long = dailyRideCount.rides
}