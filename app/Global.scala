/**
 * Created by sumnulu on 22/03/15.
 */

import play.api._
import play.api.mvc.{WithFilters, Result, RequestHeader, Filter}
import play.api.mvc._


import scala.concurrent.Future

object Global extends WithFilters(PassKeyFilter) {

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}

object PassKeyFilter extends Filter {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def checkKey(key: String) = {
    key == "cipetpet"
  }

  def apply(nextFilter: (RequestHeader) => Future[Result])(requestHeader: RequestHeader): Future[Result] = {


    nextFilter(requestHeader).map { result =>

      val key = requestHeader.cookies.get("access_key").map(_.value).getOrElse("")
      println(requestHeader.uri)
      if (checkKey(key)) {
        result
      } else if (requestHeader.uri.startsWith("/auth")) {

        result
      } else {

        Results.Redirect("/auth")
      }


    }
  }

}