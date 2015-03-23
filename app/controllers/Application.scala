package controllers


import actors.UserActor.api.{GetUser, Profile}
import actors.{UserActor, PostsActor}
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
  lazy val userActor = UserActor()


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


  def user(id: String) = Action.async {
    (userActor ? GetUser(id)).mapTo[Option[Profile]] map {
      _.map { profile =>

        Ok(views.html.user(profile))

      }.getOrElse {

        NotFound
      }
    }

  }


  def logout() = play.mvc.Results.TODO

  def login() = play.mvc.Results.TODO


  def test() = Action{
    val myList = List("xxx","yyyy","11111", "00000")

    Ok(views.html.test("parametre1",myList))
  }

}
