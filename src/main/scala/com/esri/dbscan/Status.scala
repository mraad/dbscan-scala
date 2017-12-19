package com.esri.dbscan

object Status extends Enumeration {
  type Status = Status.Value
  val UNCLASSIFIED, CLASSIFIED, NOISE = Value
}
