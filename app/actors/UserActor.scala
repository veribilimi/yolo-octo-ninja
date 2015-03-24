package actors

import actors.UserActor.api.{BasicAuth, Profile, GetUser}
import akka.actor.{Props, Actor}
import play.libs.Akka.system


/**
 * Created by sumnulu on 23/03/15.
 */
class UserActor extends Actor {
  override def receive: Receive = {

    case GetUser(id) => sender ! Some(Profile(id, "testUserName"))

    case BasicAuth(username, password) =>
      //todo change this function with real implementation
      val maybeProfile = if (password == "password") Some(Profile(id = "TODO_ID", username = username)) else None
      sender ! maybeProfile
  }

}


object UserActor {

  object api {

    case class GetUser(id: String)

    case class Profile(id: String, username: String)

    case class BasicAuth(username: String, password: String)

  }

  lazy val userActor = system.actorOf(Props(new UserActor))

  def apply() = userActor

}