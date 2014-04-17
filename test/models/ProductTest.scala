package models

import org.specs2.matcher.ShouldMatchers

class ProductTest extends org.scalatest.FunSpec with ShouldMatchers {
  describe("Product DAO") {
    it("should support add a new product") {
      val product = Product(2323823L, "name", "description")
      val oldSize = Product.findAll.size

      Product.add(product)

      Product.findAll.size should equalTo(oldSize + 1)
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

  }
}
