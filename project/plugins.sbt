// Versions the build.
addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.1.1")

// Use the plugin to publish the plugin.
resolvers += Resolver.url("ivy-release-local", url(s"https://jrouly.jfrog.io/artifactory/ivy-release-local"))(Resolver.ivyStylePatterns)
addSbtPlugin("io.jrouly" % "sbt-artifactory" % "0.2.0")
