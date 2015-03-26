package controllers


import actors.UserActor.api.{BasicAuth, GetUser, Profile}
import actors.{UserActor, PostsActor}
import actors.PostsActor.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import facebookapi.domain.Post
import akka.pattern.ask
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout

object Application extends Controller {
  implicit val defaultTimeout = Timeout(5 seconds)
  lazy val postsActor = PostsActor()
  lazy val userActor = UserActor()

  val NumberOfItemsForEachPage = 30

  def index(page: Int = 1) = posts("rank", page)

  def posts(sort: String = "rank", page: Int = 1) = Action.async {
    val currentPage = if (page < 1) 1 else page

    val take = NumberOfItemsForEachPage + 1
    val drop = NumberOfItemsForEachPage * (currentPage - 1)

    val sortByCommand = sort match {
      case "day" => GetPostsByRank(drop, take, "day")
      case "2day" => GetPostsByRank(drop, take, "2day")
      case "week" => GetPostsByRank(drop, take, "week")
      case "newest" => GetPostsByCreationDate(drop, take)
      case "lastUpdated" => GetPostsByUpdateDate(drop, take)
      case "all" => GetAllPosts(drop, take)
      case _ => GetPostsByRank(drop, take)
    }

    (postsActor ? sortByCommand).mapTo[List[Post]] map { _posts =>
      val posts = _posts take NumberOfItemsForEachPage

      val nextPage = if (_posts.size <= NumberOfItemsForEachPage) 1 else currentPage + 1
      val firstRank = 1 + (NumberOfItemsForEachPage * (-1 + currentPage))

      Ok(views.html.index(posts)(sort, firstRank, currentPage, nextPage))
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
        Redirect("/")
      }
    }

  }


  def logout() = Action {
    (Redirect("/") withNewSession).discardingCookies(DiscardingCookie(name = "access_key"))
  }


  def showLoginPage() = Action { request =>
    Ok(views.html.login())
  }

  def authenticate() = Action.async { request =>

    val maybeBasicAuth = for {
      formData <- request.body.asFormUrlEncoded

      usernameSeq <- formData.get("username")
      username <- usernameSeq.headOption

      passwordSeq <- formData.get("password")
      password <- passwordSeq.headOption
    } yield BasicAuth(username, password)


    maybeBasicAuth.map { ba =>

      (userActor ? ba).mapTo[Option[Profile]] map { maybeProfile =>

        maybeProfile map { profile =>

          Redirect("/").withSession("username" -> profile.username)

        } getOrElse Forbidden("Error: wrong password or username")
      }

    } getOrElse (Future successful Forbidden("Error: form does not contain username or password fields!"))


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


  def showAuthPage() = Action { request =>
    Ok(views.html.auth())
  }

  def passKeyAuth() = Action { request =>

    val maybeBasicAuth: Option[String] = for {
      formData <- request.body.asFormUrlEncoded

      codeSeq <- formData.get("code")
      code <- codeSeq.headOption

    } yield code


    maybeBasicAuth.map { code =>
      if (code == "cipetpet") {
        Redirect("/").withCookies(Cookie(name = "access_key", value = "cipetpet", maxAge = Some(2000000000)))

      } else {

        Forbidden("Wrong passkey!")
      }
    } getOrElse {
      Forbidden("Missing passkey!")
    }


  }


}
