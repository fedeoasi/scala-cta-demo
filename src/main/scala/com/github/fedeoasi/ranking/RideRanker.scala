package com.github.fedeoasi.ranking

import com.github.fedeoasi.model.HasRides

object RideRanker {
  val K = 10

  case class ScoredResult[T](value: T, score: Long)

  def rankRideCounts[T <: HasRides, U](rideData: Seq[T], k: Int = K)(by: T => U): Seq[ScoredResult[U]] = {
    val groupedTotals = rideData.groupBy(by).mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }
    val topK = groupedTotals
      .toSeq
      .sortBy(_._2)
      .reverse
      .take(K)

    topK.map { case (value, score) =>
      ScoredResult(value, score)
    }
  }
}
