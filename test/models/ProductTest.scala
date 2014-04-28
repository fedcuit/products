package models

import org.scalatest.Matchers
import org.scalatest.BeforeAndAfter

class ProductTest extends org.scalatest.FunSpec with Matchers with BeforeAndAfter {
  describe("Product DAO") {

    before {
      Product.products = Set(
        Product(5010255079763L, "Paperclips Large",
          "Large Plain Pack of 1000"),
        Product(5018206244666L, "Giant Paperclips",
          "Giant Plain 51mm 100 pack"),
        Product(5018306332812L, "Paperclip Giant Plain",
          "Giant Plain Pack of 10000"),
        Product(5018306312913L, "No Tear Paper Clip",
          "No Tear Extra Large Pack of 1000"),
        Product(5018206244611L, "Zebra Paperclips",
          "Zebra Length 28mm Assorted 150 Pack")
      )
    }

    after {
      Product.products = Set()
    }

    it("should support add a new product") {
      val product = Product(2323823L, "name", "description")
      val oldSize = Product.findAll.size

      Product.add(product)

      Product.findAll.size should equal (oldSize + 1)
    }

    it("should support search by ean") {
      val option = Product.findByEan(5018206244666L)

      option match {
        case Some(x) => {
          x.name should be("Giant Paperclips")
          x.description should be("Giant Plain 51mm 100 pack")
        }
        case None => fail("Expect find by ear, but not found")
      }
    }

    it("should able to update a product") {
      val product = Product.findByEan(5010255079763L)
      val updatedProduct = product.get.copy(name = "updated")

      Product.update(updatedProduct)

      Product.findAll should have size 5
      Product.findByEan(5010255079763L).get.name should equal ("updated")
    }
  }
}