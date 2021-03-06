name := "sbt-artifactory"
organization := "io.jrouly"
description := "Streamlines JFrog Artifactory integration."

licenses += ("The Apache Software License, Version 2.0", url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/jrouly/sbt-artifactory"))

developers += Developer(
  id = "jrouly",
  name = "Jean Michel Rouly",
  email = "michel@rouly.net",
  url = url("https://github.com/jrouly")
)

scmInfo := Some(
  ScmInfo(
    browseUrl = url("https://github.com/jrouly/sbt-artifactory"),
    connection = "scm:git:git://github.com/jrouly/sbt-artifactory.git",
    devConnection = "scm:git:ssh://git@github.com:jrouly/sbt-artifactory.git"
  )
)

enablePlugins(SbtPlugin)
sbtPlugin := true
crossSbtVersions := List("0.13.18", "1.4.6")

publishMavenStyle := false
artifactoryConnection := artifactoryCloud("jrouly")

scriptedBufferLog := false
scriptedLaunchOpts := scriptedLaunchOpts.value ++ Seq("-Xmx1024M", "-server", "-Dplugin.version=" + version.value)

Global / onChangedBuildSource := ReloadOnSourceChanges
