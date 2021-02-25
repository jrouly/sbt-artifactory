package io.jrouly.sbt.artifactory

import sbt._
import sbt.plugins.JvmPlugin

object ArtifactoryPlugin extends AutoPlugin {

  override def requires: JvmPlugin.type = sbt.plugins.JvmPlugin

  override def trigger: sbt.PluginTrigger = allRequirements

  object autoImport {
    final val artifactoryPort = settingKey[Int]("Artifactory port (defaults to 443)")
    final val artifactoryProtocol = settingKey[String]("Artifactory protocol (defaults to https)")
    final val artifactoryHostname = settingKey[String]("Artifactory hostname (defaults to localhost)")
    final val artifactoryPath = settingKey[String]("Artifactory URL path (defaults to artifactory)")

    final val artifactorySnapshotRepository = settingKey[String]("Artifactory snapshot repository label")
    final val artifactoryReleaseRepository = settingKey[String]("Artifactory release repository label")

    final val artifactorySnapshotResolver = settingKey[URLRepository]("Artifactory snapshot resolver")
    final val artifactoryReleaseResolver = settingKey[URLRepository]("Artifactory release resolver")
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
      realm = "Artifactory Realm",
      host = artifactoryHostname.value,
      userName = sys.env.getOrElse("ARTIFACTORY_USER", "username"),
      passwd = sys.env.getOrElse("ARTIFACTORY_PASS", "password")
    )
  )

  private lazy val defaultSettings = Seq(
    artifactoryPort := 443,
    artifactoryProtocol := "https",
    artifactoryHostname := "localhost",

    artifactorySnapshotRepository := {
      val isMaven = Keys.publishMavenStyle.value
      if (isMaven) "maven-snapshot-local"
      else "ivy-snapshot-local"
    },

    artifactoryReleaseRepository := {
      val isMaven = Keys.publishMavenStyle.value
      if (isMaven) "maven-release-local"
      else "maven-release-local"
    },

    artifactorySnapshotResolver := {
      val repository = artifactorySnapshotRepository.value
      artifactoryResolver(repository).value
    },

    artifactoryReleaseResolver := {
      val repository = artifactoryReleaseRepository.value
      artifactoryResolver(repository).value
    }
  )

  private def artifactoryResolver(repository: String) = Def.setting {
    val baseUrl = {
      val protocol = artifactoryProtocol.value
      val host = artifactoryHostname.value
      val port = artifactoryPort.value
      val path = artifactoryPath.value.stripPrefix("/").stripSuffix("/")
      url(s"$protocol://$host:$port/$path/$repository")
    }

    val isMaven = Keys.publishMavenStyle.value

    val pattern = if (isMaven) Resolver.mavenStylePatterns else Resolver.ivyStylePatterns
    Resolver.url(repository, baseUrl)(pattern)
  }
}
