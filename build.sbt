name := "trapos"

version := "0.1"

//scalaVersion := "2.10.0-M5"
scalaVersion := "2.9.2"

libraryDependencies ++=
  "com.googlecode.disruptor" % "disruptor" % "2.10.1" ::
  "org.scalatest" % "scalatest_2.9.0" % "1.8" % "test" ::
  "org.mockito" % "mockito-all" % "1.9.5-rc1" % "test" ::
  Nil

// filter out src/main/java
unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil)

// filter out src/test/java
unmanagedSourceDirectories in Test <<= (scalaSource in Test)( _ :: Nil)
