name := "HaikuMatcher"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.twitter4j" % "twitter4j-core" % "4.0.2"
)     

play.Project.playJavaSettings
