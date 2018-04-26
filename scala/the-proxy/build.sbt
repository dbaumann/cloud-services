name := "the-proxy"
organization := "com.summitcove"
version := "0.0.1"
scalaVersion := "2.12.3"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val ficusV        = "1.4.2"
  val scalaMockV    = "3.6.0"
  val lambdaUtilsV  = "0.0.3"
  val lambdaEventsV = "2.0.1"
  val sttpV         = "1.1.13"

  Seq(
    "io.github.yeghishe"      %% "scala-aws-lambda-utils"      % lambdaUtilsV,
    "com.iheart"              %% "ficus"                       % ficusV,
    "com.amazonaws"           %  "aws-lambda-java-events"      % lambdaEventsV,
    "org.scalamock"           %% "scalamock-scalatest-support" % scalaMockV % "it,test",
    "com.softwaremill.sttp"   %% "core"                        % sttpV,
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings
coverageEnabled := false

initialCommands := """
  import io.github.yeghishe._
  import io.github.yeghishe.lambda._
  import scala.concurrent._
  import scala.concurrent.duration._
""".stripMargin

assemblyJarName in assembly := s"${name.value}.jar"
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _ *) => MergeStrategy.discard
  case _                              => MergeStrategy.first
}

enablePlugins(S3Plugin)
mappings in s3Upload := Seq((file(s"target/scala-2.12/${name.value}.jar"), s"${name.value}.jar"))
s3Host in s3Upload := "lambdatest.s3.amazonaws.com"
s3Progress in s3Upload := true
//s3Upload <<= s3Upload dependsOn assembly
//s3Upload := {
//  assembly.value
//}
