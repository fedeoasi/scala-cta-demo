package com.github.fedeoasi.parsing

import org.scalatest._
import com.github.fedeoasi.TestConstants._

class DailyRidesParserSpec extends FunSpec with Matchers {
  describe("DailyRidesParser") {
    it("loads the rides from a file") {
      val parser = new DailyRidesParser
      val dailyRides = parser.parse(dailyRidesSampleFile)
      dailyRides.size shouldBe 9
    }
  }
}
