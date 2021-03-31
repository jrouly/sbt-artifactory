name := "sbt-artifactory"
organization := "net.rouly"
description := "Streamlines JFrog Artifactory integration."

artifactory := artifactoryCloud("jrouly")

scriptedBufferLog := false
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-server", "-Dplugin.version=" + version.value)

Global / onChangedBuildSource := ReloadOnSourceChanges

enablePlugins(SbtPlugin)

crossSbtVersions := List(
  "0.13.18",
  "1.1.6" // https://github.com/sbt/sbt/issues/5049
)
