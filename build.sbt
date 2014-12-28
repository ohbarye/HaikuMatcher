name := "HaikuMatcher"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.webjars" % "bootstrap" % "3.0.0",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7"
)     

play.Project.playJavaSettings

resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"
