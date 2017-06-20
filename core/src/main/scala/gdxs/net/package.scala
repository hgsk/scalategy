package gdxs

import com.badlogic.gdx.Net.{HttpRequest, HttpResponseListener}
import com.badlogic.gdx.{Gdx, Net}

import scala.collection.JavaConverters._
import scala.concurrent.{Future, Promise}
import scalategy.CanceledException

package object net {
  def sendHttpRequest(httpRequest: HttpRequest): Future[HttpResponse] = {
    val promise = Promise[HttpResponse]
    Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener {
      override def failed(t: Throwable): Unit = promise.failure(t)
      override def handleHttpResponse(httpResponse: Net.HttpResponse): Unit = {
        promise.success {
          val headers = httpResponse.getHeaders.asScala.keys.map(k => (k, httpResponse.getHeader(k))).toMap
          HttpResponse(httpResponse.getResultAsString, headers)
        }
      }
      override def cancelled(): Unit = promise.failure(CanceledException)
    })
    promise.future
  }
}
