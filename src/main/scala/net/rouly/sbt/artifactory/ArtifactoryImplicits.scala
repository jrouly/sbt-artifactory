package net.rouly.sbt.artifactory

import sbt._

trait ArtifactoryImplicits {

  implicit class ArtifactoryResolverCompanion(companion: Resolver.type) {

    /** Artifact resolver for JFrog Artifactory. Maven style resolution. */
    def artifactoryRepo(artifactory: Artifactory, repo: String): URLRepository = {
      implicit val patterns: Patterns = Resolver.mavenStylePatterns
      Artifactory.resolver(artifactory, repo)
    }

    /** Artifact resolver for JFrog Artifactory. Ivy style resolution. */
    def artifactoryIvyRepo(artifactory: Artifactory, repo: String): URLRepository = {
      implicit val patterns: Patterns = Resolver.ivyStylePatterns
      Artifactory.resolver(artifactory, repo)
    }
  }

}
