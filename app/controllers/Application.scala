package controllers


import play.api.mvc._
import postranker.PostRankingService
import postranker.domain.Post
import scala.collection.JavaConversions._

object Application extends Controller {


  val rankPosts: (Post) => Int = p => -(p.getComments.size + p.getLikes.size)

  lazy val posts: List[Post] = rankingService.getPosts.toList.sortBy(rankPosts)

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


  val token = "CAAE4r0cMaAoBANO8YOeLDm5OHQWXimP56jhqawisYRAHcU6OsYZCZBUMfcvePv6FbdhQYMpdlHuxDoJlPLk8ctdmBWz7asHzZAzx7SUJJmH77PXZABHgQTrrqN5SCsI6WaABikq03wF43SyJZAJwtLKqomdpFe3h55bUT0n3QpblAxPZC4fZB0zsTY6LoqEsYjMATCXBRZAgvNPFjZBfu1GXElvcAbZCYXfX6sHC3jPfyhAgZDZD"
  val rankingService: PostRankingService = new PostRankingService()
  rankingService.init(token)


}
