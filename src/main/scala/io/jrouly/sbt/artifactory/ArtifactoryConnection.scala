package io.jrouly.sbt.artifactory

sealed trait ArtifactoryConnection {
  def hostname: String
}

case class ArtifactoryUrl(
  protocol: String,
  hostname: String,
  port: Int,
  path: String
) extends ArtifactoryConnection

case class ArtifactoryCloud(
  organization: String
) extends ArtifactoryConnection {
  override lazy val hostname: String = s"$organization.jfrog.io"
}
