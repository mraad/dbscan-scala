package com.esri.dbscan

import scala.collection.mutable.ArrayBuffer

/**
  */
case class Cluster() {
  private val _points = new ArrayBuffer[DBSCANPoint]()

  def +=(point: DBSCANPoint) = {
    _points += point
    this
  }

  def points() = _points.toSeq
}
