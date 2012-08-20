name := "trapos"

version := "0.1"

//scalaVersion := "2.10.0-M5"
scalaVersion := "2.9.2"

mainClass in (Compile, run) := Some("whitewerx.com.trapos.App")

libraryDependencies ++=
  "io.netty" % "netty" % "3.5.4.Final" ::
  "com.googlecode.disruptor" % "disruptor" % "2.10.1" ::
  "com.dongxiguo" %% "zero-log" % "0.1.2" ::
  "org.scalatest" % "scalatest_2.9.0" % "1.8" % "test" ::
  "org.mockito" % "mockito-all" % "1.9.5-rc1" % "test" ::
  Nil

// filter out src/main/java
unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil)

// filter out src/test/java
unmanagedSourceDirectories in Test <<= (scalaSource in Test)( _ :: Nil)
