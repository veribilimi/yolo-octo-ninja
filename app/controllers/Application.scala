package controllers


import actors.UserActor.api.{GetUser, Profile}
import actors.{UserActor, PostsActor}
import actors.PostsActor.api.{GetPostsByRank, GetPost, GetPostsByUpdateDate, GetPostsByCreationDate}
import play.api.data._
import play.api.data.Forms._
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


  def index() = posts("rank")

  def posts(sort: String = "rank") = Action.async {
    val sortByCommand = sort match {
      case "rank" => GetPostsByRank
      case "newest" => GetPostsByCreationDate
      case "lastUpdated" => GetPostsByUpdateDate
      case _ => GetPostsByRank
    }

    (postsActor ? sortByCommand).mapTo[List[Post]] map { posts =>
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


  def logout() = Action {
    Redirect("/") withNewSession
  }


  def showLoginPage() = Action { request =>
    Ok(views.html.login())
  }

  def authenticate() = Action { request =>
    //todo change this function with real implementation
    def stupidAuth = (u: String, p: String) => if (p == "password") Some(Profile("TODO_ID", u)) else None

    val mayBeProfile = for {
      data <- request.body.asFormUrlEncoded

      usernameSeq <- data.get("username")
      username <- usernameSeq.headOption

      passwordSeq <- data.get("password")
      password <- passwordSeq.headOption

      authenticatedUser <- stupidAuth(username, password)

    } yield authenticatedUser



    mayBeProfile map { profile =>

      Redirect("/").withSession("username" -> profile.username)

    } getOrElse {

      Forbidden("sorry wrong pass or username")

    }
  }


  def showRegisterPage() = Action {
    Ok(views.html.register())
  }


  def test() = Action {
    val myList = List("xxx", "yyyy", "11111", "00000")

    Ok(views.html.test("parametre1", myList))
  }

  def submit() = Action {
    Ok(views.html.submit())
  }
}
