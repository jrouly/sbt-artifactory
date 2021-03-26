// Versions the build.
addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.1.1")

resolvers += Resolver.url(
  "ivy-release-local",
  url(s"https://jrouly.jfrog.io/artifactory/ivy-release-local")
)(Resolver.ivyStylePatterns)

// For publishing to Artifactory.
addSbtPlugin("net.rouly" % "sbt-artifactory" % "0.3.0")
