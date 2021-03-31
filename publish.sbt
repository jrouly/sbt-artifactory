organizationHomepage := Some(url("https://rouly.net"))

homepage := Some(url("https://github.com/jrouly/sbt-artifactory"))

licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/jrouly/sbt-artifactory"),
    "scm:git@github.com:jrouly/sbt-artifactory.git"
  )
)

developers := List(
  Developer(
    id = "jrouly",
    name = "Michel Rouly",
    email = "michel@rouly.net",
    url = url("https://michel.rouly.net")
  )
)

pomIncludeRepository := { _ => false }

pgpSigningKey := Some("0xFA8B833314500A89")
