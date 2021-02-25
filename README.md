# sbt-artifactory

[![Scala CI](https://github.com/jrouly/sbt-artifactory/workflows/Scala%20CI/badge.svg?branch=master)](https://github.com/jrouly/sbt-artifactory/actions?query=workflow%3A%22Scala+CI%22)

A tiny sbt plugin to streamline publishing to [JFrog Artifactory](https://jfrog.com/artifactory/).
Motivated by the [sunsetting of Bintray](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/).

# Usage

Add to your `project/plugins.sbt`

```sbt
resolvers += Resolver.url("ivy-release-local", url(s"https://jrouly.jfrog.io/artifactory/ivy-release-local"))(Resolver.ivyStylePatterns)
addSbtPlugin("io.jrouly" % "sbt-artifactory" % "version")
```

## Credentials

The plugin anticipates credentials in the environment variables `ARTIFACTORY_USER` and `ARTIFACTORY_PASS`.

You can also set your own [credentials file](https://www.scala-sbt.org/1.x/docs/Publishing.html#Credentials).

## Configuration

### Artifactory connection

Set the host name for your Artifactory install.
You can also set the port, protocol, and a URL path prefix.

For example
```sbt
artifactoryHostname := "jrouly.jfrog.io" // defaults to localhost
artifactoryPort := 80 // defaults to 443
artifactoryProtocol := "http" // defaults to https
artifactoryPath := "artifactory/foo/bar" // defaults to artifactory
```

#### JFrog Artifactory cloud

If you are a JFrog Artifactory cloud user (`*.jfrog.io`), you can instead just set your cloud organization name
```sbt
artifactoryCloudOrganization := Some("jrouly") // defaults to None
```

### Repository names

By default, the plugin will anticipate the repository names `ivy-{snapshot|release}-local` and `maven-{snapshot|release}-local` based on the value of `publishMavenStyle`.

If these don't work for you, you can override the settings
```sbt
artifactorySnapshotRepository := "sbt-snapshot"
artifactoryReleaseRepository := "sbt-release"
```

If you use a single repository for both snapshots and releases, just set the keys to the same value.

## sbt keys

| Key | Type | Description |
| --- | ---- | ----------- |
| `artifactoryPort` | `Int` | Artifactory URL port. Defaults to `443`. |
| `artifactoryProtocol` | `String` | Artifactory URL protocol. Defaults to `https`. |
| `artifactoryHostname` | `String` | Artifactory hostname. Defaults to `localhost`. |
| `artifactoryPath` | `String` | Artifactory URL path prefix. Defaults to `artifactory`. |
| `artifactoryCloudOrganization` | `Option[String]` | Artifactory cloud organization name. If set, the hostname is overridden to `$org.jfrog.io`. |
| `artifactorySnapshotRepository` | `String` | Artifactory snapshot repository label. Defaults to `ivy-snapshot-local` or `maven-snapshot-local`. |
| `artifactoryReleaseRepository` | `String` | Artifactory release repository label. Defaults to `ivy-release-local` or `maven-release-local`. |

The next two keys are derived from the previous table.
You probably don't want to override these, but go ahead if you need to.

| Key | Type | Description |
| --- | ---- | ----------- |
| `artifactorySnapshotResolver` | `URLRepository` | Artifactory snapshot resolver. |
| `artifactoryReleaseResolver` | `URLRepository` | Artifactory release resolver. |
