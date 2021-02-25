package io.jrouly.sbt.artifactory

import sbt._

private object HandleInsecure {
  def apply(resolver: URLRepository, allowInsecure: Boolean): URLRepository = {
    resolver.withAllowInsecureProtocol(allowInsecure)
  }
}
