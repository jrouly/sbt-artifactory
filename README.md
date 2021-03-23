# sbt-artifactory

[![Scala CI](https://github.com/jrouly/sbt-artifactory/workflows/Scala%20CI/badge.svg?branch=master)](https://github.com/jrouly/sbt-artifactory/actions?query=workflow%3A%22Scala+CI%22)
[![version](https://img.shields.io/badge/version-0.3.0-blue)](https://github.com/jrouly/sbt-artifactory/releases)

A tiny sbt plugin to streamline resolving against [JFrog Artifactory](https://jfrog.com/artifactory/).
Motivated by the [sunsetting of Bintray](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/).

This plugin [uses itself to publish itself](project/plugins.sbt#L4-L6).

# Usage

Add to your `project/plugins.sbt`

```sbt
resolvers += Resolver.url("ivy-release-local", url(s"https://jrouly.jfrog.io/artifactory/ivy-release-local"))(Resolver.ivyStylePatterns)
addSbtPlugin("net.rouly" % "sbt-artifactory" % "version")
```

## Credentials

The plugin anticipates credentials in the environment variables `ARTIFACTORY_USER` and `ARTIFACTORY_PASS`.

You can also set your own [credentials file](https://www.scala-sbt.org/1.x/docs/Publishing.html#Credentials).

### Realm

The default credential realm is set to `Artifactory Realm` but this can be overridden with `artifactoryRealm`.

## Configuration

### Artifactory connection

Configure the connection for your Artifactory install.

```sbt
artifactory := artifactoryCloud("mycompany") // https://mycompany.jfrog.io
artifactory := artifactoryHttps("artifacts.mycompany.biz")
artifactory := artifactoryHttps("artifacts.mycompany.biz", "/custom/path/to/artifactory")
artifactory := artifactoryHttp("insecure.artifacts.mycompany.biz")
artifactory := artifactoryHttp("insecure.artifacts.mycompany.biz", "/custom/path/to/artifactory")
```

#### A note about http

It is recommended to use `https` for artifact resolution.
No guarantee is made that `http` will work for sbt versions `0.13` and below.

### Repository names

By default, the plugin will anticipate the repository names `ivy-{snapshot|release}-local` and `maven-{snapshot|release}-local` based on the value of `publishMavenStyle`.

If these don't work for you, you can override the settings.

```sbt
artifactorySnapshotRepository := "sbt-snapshot"
artifactoryReleaseRepository := "sbt-release"
```

If you use a single repository for both snapshots and releases, just set the keys to the same value.

### Publishing

`publishTo` is automagically configured for you.
It is aware of snapshot/release and ivy/maven settings by default.

You can override `publishTo` if the defaults don't work for you.

### Resolving

You can also add custom Artifactory resolvers for dependency resolution.

```sbt
resolvers += Resolver.artifactoryIvyRepo(artifactory.value, "ivy-release")
resolvers += Resolver.artifactoryRepo(artifactory.value, "maven-release")
```

## sbt keys

| Key | Type | Description |
| --- | ---- | ----------- |
| `artifactory` | `ArtifactoryConnection` | Artifactory connection configuration. |
| `artifactoryRealm` | `String` | Artifactory credential realm. Defaults to `Artifactory Realm`. |
| `artifactorySnapshotRepository` | `String` | Artifactory snapshot repository label. Defaults to `ivy-snapshot-local` or `maven-snapshot-local`. |
| `artifactoryReleaseRepository` | `String` | Artifactory release repository label. Defaults to `ivy-release-local` or `maven-release-local`. |

The next two keys are derived from the previous table.
You probably don't want to override these, but go ahead if you need to.

| Key | Type | Description |
| --- | ---- | ----------- |
| `artifactorySnapshotResolver` | `URLRepository` | Artifactory snapshot resolver. |
| `artifactoryReleaseResolver` | `URLRepository` | Artifactory release resolver. |
