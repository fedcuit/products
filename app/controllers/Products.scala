package controllers

import play.api.mvc.{Action, Controller}
import models.Product

object Products extends Controller {
  def list = Action {
    Ok(views.html.products.list(Product.findAll))
  }
}
