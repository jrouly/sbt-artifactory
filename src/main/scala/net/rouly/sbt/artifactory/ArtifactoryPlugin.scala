package net.rouly.sbt.artifactory

import sbt._
import sbt.plugins.JvmPlugin

import scala.language.implicitConversions

object ArtifactoryPlugin extends AutoPlugin {

  override def requires: JvmPlugin.type = sbt.plugins.JvmPlugin

  override def trigger: sbt.PluginTrigger = allRequirements

  object autoImport extends ArtifactoryPluginKeys with ArtifactoryImplicits {

    def artifactoryHttp(hostname: String, path: String = "artifactory"): ArtifactoryUrl =
      ArtifactoryUrl("http", hostname, 80, path)

    def artifactoryHttps(hostname: String, path: String = "artifactory"): ArtifactoryUrl =
      ArtifactoryUrl("https", hostname, 443, path)

    def artifactoryCloud(organization: String): ArtifactoryCloud =
      ArtifactoryCloud(organization)

  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = defaultSettings ++ Seq(
    Keys.publishTo := {
      val isSnapshot = Keys.isSnapshot.value

      val snapshotResolver = Some(artifactorySnapshotResolver.value)
      val releaseResolver = Some(artifactoryReleaseResolver.value)

      if (isSnapshot) snapshotResolver
      else releaseResolver
    },
    Keys.credentials += Credentials(
      realm = artifactoryRealm.value,
      host = artifactory.value.hostname,
      userName = sys.env.getOrElse("ARTIFACTORY_USER", "username"),
      passwd = sys.env.getOrElse("ARTIFACTORY_PASS", "password")
    )
  )

  private lazy val defaultSettings = Seq(
    artifactory := artifactoryHttp("localhost", "artifactory"),
    artifactoryRealm := "Artifactory Realm",
    artifactorySnapshotRepository := {
      val isMaven = Keys.publishMavenStyle.value
      if (isMaven) "maven-snapshot-local"
      else "ivy-snapshot-local"
    },
    artifactoryReleaseRepository := {
      val isMaven = Keys.publishMavenStyle.value
      if (isMaven) "maven-release-local"
      else "ivy-release-local"
    },
    artifactorySnapshotResolver := {
      val repository = artifactorySnapshotRepository.value
      val connection = artifactory.value
      if (Keys.publishMavenStyle.value) Resolver.artifactoryRepo(connection, repository)
      else Resolver.artifactoryIvyRepo(connection, repository)
    },
    artifactoryReleaseResolver := {
      val repository = artifactoryReleaseRepository.value
      val connection = artifactory.value
      if (Keys.publishMavenStyle.value) Resolver.artifactoryRepo(connection, repository)
      else Resolver.artifactoryIvyRepo(connection, repository)
    }
  )
}
