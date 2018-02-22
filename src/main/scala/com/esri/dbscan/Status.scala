package com.esri.dbscan

/**
  * Status of a DBSCANPoint.
  */
object Status extends Enumeration {
  type Status = Status.Value
  val UNCLASSIFIED, CLASSIFIED, NOISE = Value
}
