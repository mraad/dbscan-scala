package com.esri.dbscan

/**
 * Assignment status of a DBSCANPoint.
 * UNK -> Unknown state
 * CORE -> Assigned to a cluster
 * NOISE -> Noise.
 */
private[dbscan] object Status extends Enumeration {
  type Status = Status.Value
  val UNK, CLUSTERED, NOISE = Value
}
