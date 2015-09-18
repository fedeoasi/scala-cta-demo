package com.github.fedeoasi.parsing

import com.github.fedeoasi.TestConstants
import com.github.fedeoasi.model.Station
import org.scalatest.{FunSpec, Matchers}

class StationParserSpec extends FunSpec with Matchers {
  describe("StationParser") {
    it("parses a list of stations") {
      val parser = new StationParser
      val stations = parser.parse(TestConstants.lStationsSampleFile)
      stations.size shouldBe 9
      stations.head shouldBe Station(30161, "E", "18th (Loop-bound)")
    }
  }
}
