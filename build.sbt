enablePlugins(GatlingPlugin)
enablePlugins(AssemblyPlugin)

scalaVersion := "2.12.8"
scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

val gatlingVersion = "3.2.0"

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % gatlingVersion % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.4" % "test"

val gatlingJavaOptions = Seq("-XX:-MaxFDLimit")

javaOptions ++= gatlingJavaOptions

fullClasspath in assembly := (fullClasspath in Gatling).value
mainClass in assembly := Some("io.gatling.app.Gatling")
assemblyMergeStrategy in assembly := {
  case path if path.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case path => {
    val currentStrategy = (assemblyMergeStrategy in assembly).value
    currentStrategy(path)
  }
}
test in assembly := {}