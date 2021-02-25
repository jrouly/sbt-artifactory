version := "0.1.0"

lazy val check = TaskKey[Unit]("check")
aggregate in check := true

def checkImpl(projectName: String): Def.Initialize[Task[Unit]] = Def.task {
  publishTo.value match {
    case None => sys.error("No resolver defined.")
    case Some(resolver) =>
      if (!expectations(projectName).values.toSet.contains(resolver.toString)) {
        println(projectName + " -> " + resolver.toString)
        sys.error("Resolver did not match expectations.")
      }
  }

  credentials.value.exists {
    case credentials: DirectCredentials =>
      val hostMatch = credentials.host == "example.com" || credentials.host == "demo.jfrog.io"
      val realmMatch = credentials.realm == "Artifactory Realm"
      hostMatch && realmMatch
    case _ => false
  }
}

lazy val snapshotIvyHttps = project
  .settings(
    isSnapshot := true,
    publishMavenStyle := false,
    artifactoryConnection := artifactoryHttps(hostname = "example.com", path = "/path/to/artifacts/"),
    check := checkImpl("snapshotIvyHttps").value
  )

lazy val snapshotIvyCloud = project
  .settings(
    isSnapshot := true,
    publishMavenStyle := false,
    artifactoryConnection := artifactoryCloud("demo"),
    check := checkImpl("snapshotIvyCloud").value
  )

lazy val releaseIvyHttps = project
  .settings(
    isSnapshot := false,
    publishMavenStyle := false,
    artifactoryConnection := artifactoryHttps(hostname = "example.com", path = "/path/to/artifacts/"),
    check := checkImpl("releaseIvyHttps").value
  )

lazy val releaseIvyCloud = project
  .settings(
    isSnapshot := false,
    publishMavenStyle := false,
    artifactoryConnection := artifactoryCloud("demo"),
    check := checkImpl("releaseIvyCloud").value
  )

lazy val snapshotMavenHttps = project
  .settings(
    isSnapshot := true,
    publishMavenStyle := true,
    artifactoryConnection := artifactoryHttps(hostname = "example.com", path = "/path/to/artifacts/"),
    check := checkImpl("snapshotMavenHttps").value
  )

lazy val snapshotMavenCloud = project
  .settings(
    isSnapshot := true,
    publishMavenStyle := true,
    artifactoryConnection := artifactoryCloud("demo"),
    check := checkImpl("snapshotMavenCloud").value
  )

lazy val releaseMavenHttps = project
  .settings(
    isSnapshot := false,
    publishMavenStyle := true,
    artifactoryConnection := artifactoryHttps(hostname = "example.com", path = "/path/to/artifacts/"),
    check := checkImpl("releaseMavenHttps").value
  )

lazy val releaseMavenCloud = project
  .settings(
    isSnapshot := false,
    publishMavenStyle := true,
    artifactoryConnection := artifactoryCloud("demo"),
    check := checkImpl("releaseMavenCloud").value
  )

lazy val expectations: Map[String, Map[String, String]] = Map(

  "snapshotIvyHttps" -> Map(
    "0.13" -> """URLRepository(ivy-snapshot-local,Patterns(ivyPatterns=List(https://example.com:443/path/to/artifacts/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=List(https://example.com:443/path/to/artifacts/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(ivy-snapshot-local, Patterns(ivyPatterns=Vector(https://example.com:443/path/to/artifacts/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=Vector(https://example.com:443/path/to/artifacts/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "snapshotIvyCloud" -> Map(
    "0.13" -> """URLRepository(ivy-snapshot-local,Patterns(ivyPatterns=List(https://demo.jfrog.io/artifactory/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=List(https://demo.jfrog.io/artifactory/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(ivy-snapshot-local, Patterns(ivyPatterns=Vector(https://demo.jfrog.io/artifactory/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=Vector(https://demo.jfrog.io/artifactory/ivy-snapshot-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "releaseIvyHttps" -> Map(
    "0.13" -> """URLRepository(ivy-release-local,Patterns(ivyPatterns=List(https://example.com:443/path/to/artifacts/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=List(https://example.com:443/path/to/artifacts/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(ivy-release-local, Patterns(ivyPatterns=Vector(https://example.com:443/path/to/artifacts/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=Vector(https://example.com:443/path/to/artifacts/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "releaseIvyCloud" -> Map(
    "0.13" -> """URLRepository(ivy-release-local,Patterns(ivyPatterns=List(https://demo.jfrog.io/artifactory/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=List(https://demo.jfrog.io/artifactory/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(ivy-release-local, Patterns(ivyPatterns=Vector(https://demo.jfrog.io/artifactory/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), artifactPatterns=Vector(https://demo.jfrog.io/artifactory/ivy-release-local/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)([branch]/)[revision]/[type]s/[artifact](-[classifier]).[ext]), isMavenCompatible=false, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "snapshotMavenHttps" -> Map(
    "0.13" -> """URLRepository(maven-snapshot-local,Patterns(ivyPatterns=List(), artifactPatterns=List(https://example.com:443/path/to/artifacts/maven-snapshot-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(maven-snapshot-local, Patterns(ivyPatterns=Vector(), artifactPatterns=Vector(https://example.com:443/path/to/artifacts/maven-snapshot-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "snapshotMavenCloud" -> Map(
    "0.13" -> """URLRepository(maven-snapshot-local,Patterns(ivyPatterns=List(), artifactPatterns=List(https://demo.jfrog.io/artifactory/maven-snapshot-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(maven-snapshot-local, Patterns(ivyPatterns=Vector(), artifactPatterns=Vector(https://demo.jfrog.io/artifactory/maven-snapshot-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "releaseMavenHttps" -> Map(
    "0.13" -> """URLRepository(maven-release-local,Patterns(ivyPatterns=List(), artifactPatterns=List(https://example.com:443/path/to/artifacts/maven-release-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(maven-release-local, Patterns(ivyPatterns=Vector(), artifactPatterns=Vector(https://example.com:443/path/to/artifacts/maven-release-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  ),

  "releaseMavenCloud" -> Map(
    "0.13" -> """URLRepository(maven-release-local,Patterns(ivyPatterns=List(), artifactPatterns=List(https://demo.jfrog.io/artifactory/maven-release-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false))""",
    "1.0" -> """URLRepository(maven-release-local, Patterns(ivyPatterns=Vector(), artifactPatterns=Vector(https://demo.jfrog.io/artifactory/maven-release-local/[organisation]/[module](_[scalaVersion])(_[sbtVersion])/[revision]/[artifact]-[revision](-[classifier]).[ext]), isMavenCompatible=true, descriptorOptional=false, skipConsistencyCheck=false), false)"""
  )
)
