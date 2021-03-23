package net.rouly.sbt.artifactory

import sbt.{Resolver, settingKey}

trait ArtifactoryPluginKeys {

  final val artifactory = settingKey[ArtifactoryConnection]("Artifactory connection settings")

  final val artifactoryRealm = settingKey[String]("Artifactory credential realm")

  final val artifactorySnapshotRepository = settingKey[String]("Artifactory snapshot repository")
  final val artifactoryReleaseRepository = settingKey[String]("Artifactory release repository")

  final val artifactorySnapshotResolver = settingKey[Resolver]("Artifactory snapshot resolver")
  final val artifactoryReleaseResolver = settingKey[Resolver]("Artifactory release resolver")

}
