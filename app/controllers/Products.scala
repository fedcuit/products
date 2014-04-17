package controllers

import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import models.Product

object Products extends Controller {
  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def list = Action {
    Ok(views.html.products.list(Product.findAll))
  }

  def details(ean: Long) = Action {
    val product = Product.findByEan(ean)
    product match {
      case Some(p) => Ok(views.html.products.details(p))
      case None => Ok(views.html.products.notFound())
    }
  }
}
