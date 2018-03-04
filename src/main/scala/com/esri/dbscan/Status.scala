package com.esri.dbscan

/**
  * Assignment status of a DBSCANPoint.
  * UNCLASSIFIED -> Initial state
  * CLASSIFIED -> Assigned to a cluster
  * NOISE -> Noise.
  */
private[dbscan] object Status extends Enumeration {
  type Status = Status.Value
  val UNCLASSIFIED, CLASSIFIED, NOISE = Value
}
