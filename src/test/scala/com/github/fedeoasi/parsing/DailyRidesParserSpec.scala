package com.github.fedeoasi.parsing

import com.github.fedeoasi.TestConstants._
import com.github.fedeoasi.model.DayType.SundayOrHoliday
import com.github.fedeoasi.model.{DailyRideCount, StationReference}
import org.joda.time.LocalDate
import org.scalatest._

class DailyRidesParserSpec extends FunSpec with Matchers {
  describe("DailyRidesParser") {
    val day = LocalDate.parse("2001-01-01")

    it("loads the rides from a file") {
      val parser = new DailyRidesParser
      val dailyRides = parser.parse(dailyRidesSampleFile)
      dailyRides.size shouldBe 9
      dailyRides.head shouldBe DailyRideCount(StationReference(40010, "Austin-Forest Park"), day, SundayOrHoliday, 290)
      dailyRides.last shouldBe DailyRideCount(StationReference(40090, "Damen-Brown"), day, SundayOrHoliday, 411)
    }
  }
}
