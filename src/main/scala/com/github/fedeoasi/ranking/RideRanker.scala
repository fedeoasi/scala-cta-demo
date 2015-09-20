package com.github.fedeoasi.ranking

import com.github.fedeoasi.model.HasRides

object RideRanker {
  val K = 10

  case class ScoredResult[T](value: T, score: Long)

  def rankGroupedRideCounts[T](groupedRideCounts: Map[T, Seq[HasRides]],
                               k: Int = K): Seq[ScoredResult[_]] = {
    val topK = groupedRideCounts.mapValues { dailyRidesSeq =>
      dailyRidesSeq.map(_.rides).sum
    }.toSeq.sortBy(_._2).reverse.take(K)
    val result = topK.map { case ((value, score)) =>
      ScoredResult(value, score)
    }
    result
  }

}
