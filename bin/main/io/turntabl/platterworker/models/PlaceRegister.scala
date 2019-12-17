package io.turntabl.platterworker.models

case class PlaceRegister( id:Long,  place_name: String,  place_path: String){
  def this() = this(0, "", "")
}
