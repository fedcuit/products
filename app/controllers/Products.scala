package controllers

import play.api.mvc.{Action, Controller}
import models.Product

object Products extends Controller {
  def list = Action {
    Ok(views.html.products.list(Product.findAll))
  }

  def details(ean: Long) = Action {
    val product = Product.findByEan(ean)
    product match {
      case Some(product) => Ok(views.html.products.details(product))
      case None => Ok(views.html.products.notFound())
    }
  }
}
