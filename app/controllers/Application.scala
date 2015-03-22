package controllers


import actors.PostsActor
import actors.PostsActor.api.{GetPost, Get24hPosts}
import play.api.mvc._
import postranker.domain.Post
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout

object Application extends Controller {
  implicit val defaultTimeout = Timeout(5 seconds)
  lazy val postsActor = PostsActor()


  def index = Action.async {

    (postsActor ? Get24hPosts).mapTo[List[Post]] map { posts =>
      Ok(views.html.index(posts))
    }

  }


  def post(id: String) = Action.async {
    (postsActor ? GetPost(id)).mapTo[Option[Post]] map {
      _.map { post =>

        Ok(views.html.post(post))

      }.getOrElse {

        NotFound
      }
    }

  }


}
