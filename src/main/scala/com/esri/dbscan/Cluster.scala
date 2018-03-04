package com.esri.dbscan

/**
  * A cluster.
  *
  * @param id     the cluster id.
  * @param points the points making up the cluster.
  */
case class Cluster[T <: DBSCANPoint](id: Int, points: Iterable[T])
