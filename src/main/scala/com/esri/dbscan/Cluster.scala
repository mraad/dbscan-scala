package com.esri.dbscan

/**
  * A cluster.
  *
  * @param id    the cluster id.
  * @param point the points making up the cluster.
  */
case class Cluster(id: Int, point: Iterable[DBSCANPoint])
