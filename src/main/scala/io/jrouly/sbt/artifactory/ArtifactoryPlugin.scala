package io.jrouly.sbt.artifactory

import sbt._
import sbt.plugins.JvmPlugin

object ArtifactoryPlugin extends AutoPlugin {

  override def requires: JvmPlugin.type = sbt.plugins.JvmPlugin

  override def trigger: sbt.PluginTrigger = allRequirements

  object autoImport {
    final val artifactoryBaseUrl = settingKey[URL]("Artifactory base URL")
    final val artifactoryCloudOrganization = settingKey[Option[String]]("Artifactory Cloud organization name")

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
      host = artifactoryBaseUrl.value.getHost,
      userName = sys.env.getOrElse("ARTIFACTORY_USER", "username"),
      passwd = sys.env.getOrElse("ARTIFACTORY_PASS", "password")
    )
  )

  private lazy val defaultSettings = Seq(
    artifactoryBaseUrl := {
      artifactoryCloudOrganization.value match {
        case Some(org) => url(s"https://$org.jfrog.io/artifactory")
        case None => url("http://localhost:80/artifactory")
      }
    },

    artifactoryCloudOrganization := None,

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
    val repositoryUrl = s"${artifactoryBaseUrl.value}/$repository"
    val isMaven = Keys.publishMavenStyle.value

    val pattern = if (isMaven) Resolver.mavenStylePatterns else Resolver.ivyStylePatterns
    Resolver.url(repository, url(repositoryUrl))(pattern)
  }
}
