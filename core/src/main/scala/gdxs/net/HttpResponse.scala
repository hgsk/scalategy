package gdxs.net

case class HttpResponse(content: String, headers: Map[String, String]) {
  def getHeader(name: String): Option[String] = headers.get(name)
}

