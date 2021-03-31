package net.rouly.sbt.artifactory

import sbt._

trait ArtifactoryPluginKeys {

  final val artifactory = settingKey[Artifactory]("Artifactory connection settings")
  final val artifactoryRealm = settingKey[String]("Artifactory credential realm")
  final val artifactoryCredentials = settingKey[Credentials]("Artifactory connection credentials")

  final val artifactorySnapshotRepository = settingKey[String]("Artifactory snapshot repository")
  final val artifactoryReleaseRepository = settingKey[String]("Artifactory release repository")

  final val artifactorySnapshotResolver = settingKey[Resolver]("Artifactory snapshot resolver")
  final val artifactoryReleaseResolver = settingKey[Resolver]("Artifactory release resolver")

  final val artifactoryResolver = settingKey[Resolver]("Artifactory publishTo resolver")

}
