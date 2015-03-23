package actors

import actors.PostsActor.api._
import actors.PostsActor._

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

  var postsSortedByRank: List[Post] = Nil

  var postsSortedByUpdateDate: List[Post] = Nil

  override def receive: Receive = {

    case GetPostsByRank         => sender ! postsSortedByRank

    case GetPostsByUpdateDate   => sender ! postsSortedByUpdateDate

    case GetPostsByCreationDate => sender ! posts

    case GetPost(id)            => sender ! posts.find(_.getSid == id)

    case SyncFacebookPosts      => Future {
                                     postsSortedByUpdateDate = importFacebookPosts sortBy order.byUpdateDate
                                     posts = postsSortedByUpdateDate sortBy order.byCreationDate
                                     postsSortedByRank = postsSortedByUpdateDate sortBy order.byRank
                                   }
  }

  override def preStart() = {
    rankingService.init(token)

    context.system.scheduler.schedule(0 seconds, 1800 seconds, self, SyncFacebookPosts)
  }

  def importFacebookPosts() = {
    import scala.collection.JavaConversions._
    val posts: List[Post] = rankingService.getPosts.toList
    posts
  }

}


object PostsActor {
  val token = "343800439138314|EfAO_J7NepZsopex7pTx83hlFU0"

  object order {
    val byRank: (Post) => Int = p => -(p.getComments.size + p.getLikes.size)
    val byUpdateDate: (Post) => Long = p => -p.getUpdatedTime.getTime
    val byCreationDate: (Post) => Long = p => -p.getCreatedTime.getTime
  }

  object api {

    case class GetPost(id: String)

    object GetPostsByRank

    object GetPostsByUpdateDate

    object GetPostsByCreationDate

    object SyncFacebookPosts

  }


  lazy val postsActor = Akka.system.actorOf(Props(new PostsActor(token)))

  def apply() = postsActor

}