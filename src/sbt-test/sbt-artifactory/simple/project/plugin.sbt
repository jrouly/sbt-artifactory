sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("io.jrouly" % "sbt-artifactory" % x)
  case _ => sys.error("The system property 'plugin.version' is not defined.")
}
