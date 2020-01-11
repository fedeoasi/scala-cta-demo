package com.github.fedeoasi.parsing

import com.github.fedeoasi.TestConstants
import com.github.fedeoasi.model.Station
import org.scalatest.{FunSpec, Matchers}
import com.github.fedeoasi.model.Line._

class StationParserSpec extends FunSpec with Matchers {
  describe("StationParser") {
    it("parses a list of stations") {
      val stations = StationParser.parse(TestConstants.ctaLStationsSampleFile)
      stations.size shouldBe 9
      stations.head shouldBe Station(30161, 40830, "E", "18th (Loop-bound)", Seq(PinkLine))
      stations.last shouldBe Station(30210, 41080, "S", "47th (63rd-bound)", Seq(GreenLine))
    }
  }
}
