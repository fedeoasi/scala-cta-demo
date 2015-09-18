package com.github.fedeoasi.main

import com.github.fedeoasi.Constants._
import com.github.fedeoasi.parsing.DailyRidesParser

object ParserMain {
  def main(args: Array[String]) {
    val parser = new DailyRidesParser()
    parser.parse(ctaFile)
  }
}
