package scalategy.net

import com.badlogic.gdx.Net.HttpMethods
import com.badlogic.gdx.net.HttpRequestBuilder
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalategy.Session
import scalategy.shared.Constants

class AutowireClient(session: Session) extends autowire.Client[String, Reader, Writer] {
  override def doCall(req: Request): Future[String] = {
    val builder = new HttpRequestBuilder
    builder
      .newRequest()
      .header(Constants.sessionHeaderName, session.token.getOrElse(""))
      .method(HttpMethods.POST)
      .url("http://127.0.0.1:9000/api/" + req.path.mkString("/"))
      .content(upickle.default.write(req.args))
    gdxs.net.sendHttpRequest(builder.build())
      .map(_.content)
  }
  override def read[Result: Reader](p: String): Result = upickle.default.read[Result](p)
  override def write[Result: Writer](r: Result): String = upickle.default.write(r)
}
