name := "HaikuMatcher"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.webjars" % "bootstrap" % "3.0.0",
  "org.webjars" %% "webjars-play" % "2.2.0"
)     

play.Project.playJavaSettings

resolvers += "webjars" at "http://webjars.github.com/m2"