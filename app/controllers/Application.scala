package controllers


import play.api.mvc._
import postranker.PostRankingService
import postranker.domain.Post
import scala.collection.JavaConversions._

object Application extends Controller {
  lazy val posts: List[Post] = rankingService.getPosts.toList

  def index = Action { request =>
    Ok(views.html.index(posts))
  }


  def post(id: String) = Action { request =>
    posts.find(_.getSid == id).map { post =>

      Ok(views.html.post(post))

    }.getOrElse {

      NotFound
    }
  }


  val token = "CAAE4r0cMaAoBAHqj61PYpuTZBLUBKoMsYUucwgZAkQTYqzZCMnx1YTEkVLlkbIbZBX9Nxd3lZB2RJVFUlWrF25tFQhecg1mtOZCyb345MfHYaOcpwK9r03Sletc0IRL3XQAP1OTqToUOb0ZAKvRxA2a0OxqkyCNegwzuZBLgexc1Gbf7arNCh9hwrqrTpGos5GYfZCFZBZAXikZC3YZAUoF6RHwtwUFSnZBRG63ZBUZD"
  val rankingService: PostRankingService = new PostRankingService()
  rankingService.init(token)


}
