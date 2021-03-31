name := "sbt-artifactory"
organization := "net.rouly"
description := "Streamlines JFrog Artifactory integration."

homepage := Some(url("https://github.com/jrouly/sbt-artifactory"))
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

artifactory := artifactoryCloud("jrouly")

scriptedBufferLog := false
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-server", "-Dplugin.version=" + version.value)

Global / onChangedBuildSource := ReloadOnSourceChanges

enablePlugins(SbtPlugin)

crossSbtVersions := List(
  "0.13.18",
  "1.1.6" // https://github.com/sbt/sbt/issues/5049
)
