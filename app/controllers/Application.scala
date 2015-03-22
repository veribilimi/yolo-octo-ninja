package controllers

import java.util.UUID.randomUUID
import play.api.mvc._

object Application extends Controller {


  def index = Action { request =>
    val posts: List[Long] = List(1,2,3,4,5,6)
    Ok(views.html.index(posts))
  }


  def post(id: Long) = Action { request =>
    val postDesc =
      """
        | blablah blah ad sljhd jsahd kjahdkj sahkjd sjkd
        | asjh kjAHS KJADHSKJ AHSDKJ AHSKJD AKSJHD KJASHDJK S
        | ASJD KJASHDKJ AHSK JDAHSKJDHA SKJHD KJASHD KJSAHDKJ ASDK
        | JASD ASJHD AHSD AGHSDG JHDGAJSGD JAHSG DJHSAG DHJGASJHD
        | BITTI....
      """.stripMargin


    Ok(views.html.post(postDesc, id))
  }


}
