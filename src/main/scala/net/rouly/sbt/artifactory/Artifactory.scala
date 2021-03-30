package net.rouly.sbt.artifactory

import sbt.{Patterns, Resolver, URLRepository, url}

sealed trait Artifactory {

  def hostname: String
}

object Artifactory {

  def resolver(
    connection: Artifactory,
    repository: String
  )(implicit patterns: Patterns): URLRepository = {
    val repositoryUrl: String = {
      connection match {
        case ArtifactoryUrl(protocol, hostname, port, path) =>
          val cleanProtocol = protocol.stripSuffix("://")
          val cleanPath = path.stripPrefix("/").stripSuffix("/")
          s"$cleanProtocol://$hostname:$port/$cleanPath/$repository"
        case cloud @ ArtifactoryCloud(_) =>
          s"https://${cloud.hostname}/artifactory/$repository"
      }
    }

    Resolver.url(repository, url(repositoryUrl))
  }
}

case class ArtifactoryUrl(
  protocol: String,
  hostname: String,
  port: Int,
  path: String
) extends Artifactory

case class ArtifactoryCloud(
  organization: String
) extends Artifactory {

  override lazy val hostname: String = s"$organization.jfrog.io"
}
