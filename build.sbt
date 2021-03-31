name := "sbt-artifactory"
organization := "net.rouly"
description := "Streamlines JFrog Artifactory integration."

artifactory := artifactoryCloud("jrouly")

scriptedBufferLog := false
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-server", "-Dplugin.version=" + version.value)

Global / onChangedBuildSource := ReloadOnSourceChanges

enablePlugins(SbtPlugin)

crossScalaVersions := Seq("2.10.7", "2.12.10")

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.10" => "0.13.18"
    case "2.12" => "1.1.6" // https://github.com/sbt/sbt/issues/5049
  }
}
