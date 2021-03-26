package net.rouly.sbt.artifactory

import sbt._

private object Artifactory {

  def connection(
    connection: ArtifactoryConnection,
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
