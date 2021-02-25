package io.jrouly.sbt.artifactory

import sbt._
import sbt.plugins.JvmPlugin

import scala.language.implicitConversions

object ArtifactoryPlugin extends AutoPlugin {

  override def requires: JvmPlugin.type = sbt.plugins.JvmPlugin

  override def trigger: sbt.PluginTrigger = allRequirements

  object autoImport {

    def artifactoryHttp(hostname: String, path: String = "artifactory"): ArtifactoryUrl =
      ArtifactoryUrl("http", hostname, 80, path)

    def artifactoryHttps(hostname: String, path: String = "artifactory"): ArtifactoryUrl =
      ArtifactoryUrl("https", hostname, 443, path)

    def artifactoryCloud(organization: String): ArtifactoryCloud =
      ArtifactoryCloud(organization)

    final val artifactoryConnection = settingKey[ArtifactoryConnection]("Artifactory connection settings")

    final val artifactoryRealm = settingKey[String]("Artifactory credential realm")

    final val artifactorySnapshotRepository = settingKey[String]("Artifactory snapshot repository")
    final val artifactoryReleaseRepository = settingKey[String]("Artifactory release repository")

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
      realm = artifactoryRealm.value,
      host = artifactoryConnection.value.hostname,
      userName = sys.env.getOrElse("ARTIFACTORY_USER", "username"),
      passwd = sys.env.getOrElse("ARTIFACTORY_PASS", "password")
    )
  )

  private lazy val defaultSettings = Seq(
    artifactoryConnection := artifactoryHttp("localhost", "artifactory"),

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

    artifactorySnapshotResolver := Def.settingDyn {
      val repository = artifactorySnapshotRepository.value
      artifactoryResolver(repository)
    }.value,

    artifactoryReleaseResolver := Def.settingDyn {
      val repository = artifactoryReleaseRepository.value
      artifactoryResolver(repository)
    }.value
  )

  private def artifactoryResolver(repository: String): Def.Initialize[URLRepository] = Def.setting {
    val repositoryUrl = {
      artifactoryConnection.value match {
        case ArtifactoryUrl(protocol, hostname, port, path) =>
          val cleanProtocol = protocol.stripSuffix("://")
          val cleanPath = path.stripPrefix("/").stripSuffix("/")
          s"$cleanProtocol://$hostname:$port/$cleanPath/$repository"
        case cloud @ ArtifactoryCloud(_) =>
          s"https://${cloud.hostname}/artifactory/$repository"
      }
    }

    val isInsecure = repositoryUrl.startsWith("http://")
    val isMaven = Keys.publishMavenStyle.value
    val pattern = if (isMaven) Resolver.mavenStylePatterns else Resolver.ivyStylePatterns
    HandleInsecure(Resolver.url(repository, url(repositoryUrl))(pattern), isInsecure)
  }
}
