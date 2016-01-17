package com.esri.dbscan

/**
  */
case class RowCol(val row: Int, val col: Int) {
  def toInnerEnvp(cellSize: Double, eps: Double) = {
    val xmin = col * cellSize
    val ymin = row * cellSize
    val xmax = xmin + cellSize
    val ymax = ymin + cellSize
    (xmin + eps, ymin + eps, xmax - eps, ymax - eps)
  }
}
