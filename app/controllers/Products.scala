package controllers

import play.api.mvc.{Flash, Action, Controller}
import play.api.data._
import play.api.data.Forms._
import models.Product

object Products extends Controller {
  private val newForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  private val editForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.doesnotexist", Product.findByEan(_).isDefined),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def list = Action {
    implicit request =>
      Ok(views.html.products.list(Product.findAll))
  }

  def details(ean: Long) = Action {
    implicit request =>
      Product.findByEan(ean).map(
        product => Ok(views.html.products.details(product))
      ).getOrElse(NotFound)
  }

  def newProduct = Action {
    implicit request =>
      val form = if (flash.get("error").isDefined) newForm.bind(flash.data) else newForm
      Ok(views.html.products.newProduct(form))
  }

  def save = Action {
    implicit request =>
      val form: Form[Product] = newForm.bindFromRequest()
      form.fold(
        hasErrors = {
          form =>
            Redirect(routes.Products.newProduct()).flashing(Flash(form.data) + "error" -> "Please check your input")
        },
        success = {
          newProduct =>
            Product.add(newProduct)
            Redirect(routes.Products.details(newProduct.ean)).flashing("success" -> "New product added successfully")
        }
      )
  }

  def editProduct(ean: Long) = Action {
    implicit request =>
      val product: Option[Product] = Product.findByEan(ean)
      product match {
        case Some(p) => {
          val form: Form[Product] = if (flash.get("error").isDefined) newForm.bind(flash.data) else newForm.fill(p)
          Ok(views.html.products.editProduct(form))
        }
        case None => NotFound
      }
  }

  def update(ean: Long) = Action {
    implicit request =>
      val form: Form[Product] = editForm.bindFromRequest()
      form.fold(
        hasErrors = {
          form =>
            Redirect(routes.Products.editProduct(ean)).flashing(Flash(form.data) + "error" -> form.errorsAsJson.toString())
        },
        success = {
          product =>
            Product.update(product.copy())
            Redirect(routes.Products.details(ean)).flashing("success" -> "Product updated successfully")
        }
      )
  }

  def delete(ean: Long) = Action {
    val option = Product.findByEan(ean)
    option match {
      case Some(x) =>
        Product.remove(x)
        Ok("Success")
      case None => NotFound
    }
  }

}
