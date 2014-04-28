name := "products"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

libraryDependencies ++= Seq(
  "net.sf.barcode4j" % "barcode4j" % "2.0",
  "org.scalatest" % "scalatest_2.10" % "2.1.3"
)

play.Project.playScalaSettings
