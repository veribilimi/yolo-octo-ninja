package actors

import actors.PostsActor.api._
import actors.PostsActor.rankPosts

import akka.actor.{Props, Actor}
import play.libs.Akka
import postranker.PostRankingService
import postranker.domain.Post
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by sumnulu on 22/03/15.
 */
class PostsActor(token: String) extends Actor {
  val rankingService: PostRankingService = new PostRankingService()
  var posts: List[Post] = Nil

  override def receive: Receive = {

    case Get24hPosts       => sender ! posts

    case GetPost(id)       => sender ! posts.find(_.getSid == id)

    case SyncFacebookPosts => Future(posts = importFacebookPosts sortBy rankPosts)
  }

  override def preStart() = {
    rankingService.init(token)

    context.system.scheduler.schedule(0 seconds, 180 seconds, self, SyncFacebookPosts)
  }

  def importFacebookPosts() = {
    import scala.collection.JavaConversions._
    val posts: List[Post] = rankingService.getPosts.toList
    posts
  }

}


object PostsActor {
  val token = "343800439138314|EfAO_J7NepZsopex7pTx83hlFU0"
  val rankPosts: (Post) => Int = p => -(p.getComments.size + p.getLikes.size)

  object api {

    case class GetPost(id: String)

    object Get24hPosts

    object SyncFacebookPosts

  }


  lazy val postsActor = Akka.system.actorOf(Props(new PostsActor(token)))

  def apply() = postsActor

}