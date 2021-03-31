import com.jsuereth.sbtpgp.PgpKeys.publishSigned
import sbt._

commands += Command.command("multiPublish") { state =>
  val extracted = Project.extract(state)

  val artifactorySettings = List(
    publishMavenStyle := false,
    credentials := Seq(artifactoryCredentials.value),
    publishTo := Some(artifactoryResolver.value)
  )

  val sonatypeCredentials = Credentials(
    realm = "Sonatype Nexus Repository Manager",
    host = "s01.oss.sonatype.org",
    userName = sys.env.getOrElse("SONATYPE_USER", "username"),
    passwd = sys.env.getOrElse("SONATYPE_PASS", "password")
  )

  val sonatypeResolver = Def.task {
    val nexus = "https://s01.oss.sonatype.org/"
    if (isSnapshot.value) "snapshots" at nexus + "content/repositories/snapshots"
    else "releases" at nexus + "service/local/staging/deploy/maven2"
  }

  val sonatypeSettings = List(
    publishMavenStyle := true,
    credentials := Seq(sonatypeCredentials),
    publishTo := Some(sonatypeResolver.value)
  )

  Project.runTask(
    publishSigned in Compile,
    extracted.appendWithSession(sonatypeSettings, state),
    checkCycles = true
  )

  Project.runTask(
    publishSigned in Compile,
    extracted.appendWithSession(artifactorySettings, state),
    checkCycles = true
  )

  state
}
