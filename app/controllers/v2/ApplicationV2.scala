package controllers.v2

import actors.PostsActor.api._
import actors.UserActor.api.{BasicAuth, GetUser, Profile}
import actors.{PostsActor, UserActor}
import akka.pattern.ask
import akka.util.Timeout
import facebookapi.domain.Post
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object ApplicationV2 extends Controller {
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
                                    case "day"          => GetPostsByRank(drop, take,"day")
                                    case "2day"         => GetPostsByRank(drop, take,"2day")
                                    case "week"         => GetPostsByRank(drop, take,"week")
                                    case "newest"       => GetPostsByCreationDate(drop, take)
                                    case "lastUpdated"  => GetPostsByUpdateDate(drop, take)
                                    case "all"          => GetAllPosts(drop, take)
                                    case _              => GetPostsByRank(drop, take)
                                   }

    (postsActor ? sortByCommand).mapTo[List[Post]] map { _posts =>
      val posts = _posts take NumberOfItemsForEachPage

      val nextPage = if (_posts.size <= NumberOfItemsForEachPage) 1 else currentPage + 1
      val firstRank = 1 + (NumberOfItemsForEachPage * (-1 + currentPage))

      Ok(views.html.v2.index(posts)(sort, firstRank, currentPage, nextPage))
    }
  }


  def post(id: String) = Action.async {
    (postsActor ? GetPost(id)).mapTo[Option[Post]] map {
      _.map { post =>

        Ok(views.html.v2.post(post))

      }.getOrElse {

        NotFound
      }
    }

  }


  def user(id: String) = Action.async {
    (userActor ? GetUser(id)).mapTo[Option[Profile]] map {
      _.map { profile =>

        Ok(views.html.v2.user(profile))

      }.getOrElse {

        NotFound
      }
    }

  }


  def logout() = Action {
    Redirect("/") withNewSession
  }


  def showLoginPage() = Action { request =>
    Ok(views.html.v2.login())
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
    Ok(views.html.v2.register())
  }


  def test() = Action {
    val myList = List("xxx", "yyyy", "11111", "00000")

    Ok(views.html.v2.test("parametre1", myList))
  }
  def test2() = Action {
    val myList = List(1,2,3,4,5)
    Ok(views.html.v2.test2(myList))
  }

  def submit() = Action {
    Ok(views.html.v2.submit())
  }

  def robots = Action{
    Ok(
      """
        |User-agent: *
        |Disallow: /
      """.stripMargin)
  }
}
