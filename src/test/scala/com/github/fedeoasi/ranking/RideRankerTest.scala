package com.github.fedeoasi.ranking

import java.time.LocalDate

import com.github.fedeoasi.model.DayType.{Saturday, SundayOrHoliday, WeekDay}
import com.github.fedeoasi.model.{DailyRideCount, _}
import com.github.fedeoasi.ranking.RideRanker._
import org.scalatest.{FunSpec, Matchers}

class RideRankerTest extends FunSpec with Matchers {
  private val dailyRideCounts = Seq(
    DailyRideCount(StationReference(1, "Lake"), LocalDate.parse("2020-01-01"), WeekDay, 100),
    DailyRideCount(StationReference(2, "Monroe"), LocalDate.parse("2020-01-01"), WeekDay, 101),
    DailyRideCount(StationReference(1, "Lake"), LocalDate.parse("2020-01-03"), Saturday, 40),
    DailyRideCount(StationReference(1, "Lake"), LocalDate.parse("2020-01-04"), SundayOrHoliday, 50)
  )

  it("ranks by day") {
    rankRideCounts(dailyRideCounts)(_.date) shouldBe Seq(
      ScoredResult(LocalDate.parse("2020-01-01"), 201),
      ScoredResult(LocalDate.parse("2020-01-04"), 50),
      ScoredResult(LocalDate.parse("2020-01-03"), 40)
    )
  }

  it("ranks by day type") {
    rankRideCounts(dailyRideCounts)(_.dayType) shouldBe Seq(
      ScoredResult(WeekDay, 201),
      ScoredResult(SundayOrHoliday, 50),
      ScoredResult(Saturday, 40)
    )
  }
}
