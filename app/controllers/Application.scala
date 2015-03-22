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


  val token = "CAAE4r0cMaAoBABvDb4XGMuM9p6ZAMVN4NZAFZAG5CCkZB3wxo8hAKIbSwM4DmG1Do7XxZBHMHTvGWUZCexiJorNawHjzgE5k7sRjEyhvDF5QFUfwBCr8iebX9oX3OonmMQddDJS3It37EsOElhDlsEISX014rCqCwqGnFYKACvkLSYllpqZA4852nZA9qU08AXBxM67Aw6ngQjsuDnwcmkzyLs24iZAc2btoZD"
  val rankingService: PostRankingService = new PostRankingService()
  rankingService.init(token)


}
