package net.rouly.sbt.artifactory

import sbt._

trait ArtifactoryImplicits extends ArtifactoryPluginKeys {

  implicit class ArtifactoryResolverCompanion(companion: Resolver.type) {

    /** Artifact resolver for JFrog Artifactory. Maven style resolution. */
    def artifactoryRepo(repo: String): Def.Initialize[sbt.URLRepository] = Def.setting {
      implicit val patterns: Patterns = Resolver.mavenStylePatterns
      Artifactory.connection(artifactory.value, repo)
    }

    /** Artifact resolver for JFrog Artifactory. Ivy style resolution. */
    def artifactoryIvyRepo(repo: String): Def.Initialize[sbt.URLRepository] = Def.setting {
      implicit val patterns: Patterns = Resolver.ivyStylePatterns
      Artifactory.connection(artifactory.value, repo)
    }
  }

}
