package controllers

import play.api.mvc.{Flash, Action, Controller}
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
      val form = if (flash.get("error").isDefined) productForm.bind(flash.data) else productForm
      Ok(views.html.products.newProduct(form))
  }

  def save = Action {
    implicit request =>
      val newProductForm: Form[Product] = productForm.bindFromRequest()
      newProductForm.fold(
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
}
