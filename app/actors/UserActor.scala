package actors

import actors.UserActor.api.{Profile, GetUser}
import akka.actor.{Props, Actor}
import play.libs.Akka.system


/**
 * Created by sumnulu on 23/03/15.
 */
class UserActor extends Actor {
  override def receive: Receive = {

    case GetUser(id) => sender ! Some(Profile(id, "testUserName"))

  }

}


object UserActor {

  object api {

    case class GetUser(id: String)

    case class Profile(id: String, username: String)

  }

  lazy val userActor = system.actorOf(Props(new UserActor))

  def apply() = userActor

}