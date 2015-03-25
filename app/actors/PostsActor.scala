package actors

import actors.PostsActor.api._
import actors.PostsActor._

import akka.actor.{Props, Actor}
import play.libs.Akka
import facebookapi.FacebookApiService
import facebookapi.domain.Post
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by sumnulu on 22/03/15.
 */
class PostsActor(token: String) extends Actor {
  val InitialFacebookSyncNumberOfDays = 14
  val DefaultFacebookSyncNumberOfDays = 1

  val FacebookSyncEvery = 1800 seconds


  val rankingService: FacebookApiService = new FacebookApiService()

  var allPosts: List[Post] = Nil

  var posts: List[Post] = Nil

  var postsSortedByRank: List[Post] = Nil

  var postsSortedByUpdateDate: List[Post] = Nil

  override def receive: Receive = {

    case GetPostsByRank(d,t)         => sender ! postsSortedByRank.drop(d).take(t)

    case GetPostsByUpdateDate(d,t)   => sender ! postsSortedByUpdateDate.drop(d).take(t)

    case GetPostsByCreationDate(d,t) => sender ! posts.drop(d).take(t)

    case GetPost(id)                 => sender ! posts.find(_.getSid == id)

    case SyncFacebookPosts(days)     => Future {
                                         val now = System.currentTimeMillis
                                         val deltaDate = 1000 * 60 * 60 * 24 * InitialFacebookSyncNumberOfDays
                                         val minDate = now - deltaDate

                                         val facebookResult = importFacebookPosts(days)

                                         //todo fixme inefficient
                                         val notUpdatedPosts = allPosts.filterNot(p => facebookResult.exists(fb => fb.getSid == p.getSid))

                                         allPosts = (facebookResult ::: notUpdatedPosts) sortBy order.byUpdateDate

                                         postsSortedByUpdateDate = allPosts takeWhile (p => p.getUpdatedTime.getTime > minDate)
                                         posts = postsSortedByUpdateDate sortBy order.byCreationDate
                                         postsSortedByRank = postsSortedByUpdateDate sortBy order.byRank
                                       }
  }

  override def preStart() = {
    import context.system
    rankingService.init(token)

    //Initial Facebook sync (fetches more posts i.e. slow)
    system.scheduler.scheduleOnce(10 second, self, SyncFacebookPosts(InitialFacebookSyncNumberOfDays))

    //Normal Facebook sync (faster)
    system.scheduler.schedule(0 second, FacebookSyncEvery, self, SyncFacebookPosts(DefaultFacebookSyncNumberOfDays))
  }

  def importFacebookPosts(days:Int) = {
    import scala.collection.JavaConversions._
    val posts: List[Post] = rankingService.getPosts("418686428146403",days).toList
    posts
  }

}


object PostsActor {
  //todo move this to application.conf
  val token = "343800439138314|EfAO_J7NepZsopex7pTx83hlFU0"


  object order {
    val byRank: (Post) => Int = p => -(p.getComments.size + p.getLikes.size)
    val byUpdateDate: (Post) => Long = p => -p.getUpdatedTime.getTime
    val byCreationDate: (Post) => Long = p => -p.getCreatedTime.getTime
  }

  object api {

    case class GetPost(id: String)

    case class GetPostsByRank(drop: Int = 0, take: Int = 100)

    case class GetPostsByUpdateDate(drop: Int = 0, take: Int = 100)

    case class GetPostsByCreationDate(drop: Int = 0, take: Int = 100)

    case class SyncFacebookPosts(days:Int)

  }


  lazy val postsActor = Akka.system.actorOf(Props(new PostsActor(token)))

  def apply() = postsActor

}