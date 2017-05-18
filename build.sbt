val scalaV = "2.11.8"
val appV = "0.1"

lazy val core = (project in file("core"))
  .settings(
    name := "scalategy",
    version := appV,
    scalaVersion := scalaV,
    resourceDirectory in Compile := baseDirectory.value / "assets",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx" % "1.9.6"
    )
  )
  .dependsOn(shared)

lazy val desktop = (project in file("desktop"))
  .settings(
    name := "scalategy-desktop",
    version := appV,
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.6",
      "com.badlogicgames.gdx" % "gdx-platform" % "1.9.6" classifier "natives-desktop"
    )
  )
  .dependsOn(core)

lazy val dev = (project in file("dev"))
  .settings(
    name := "scalategy-dev",
    version := appV,
    scalaVersion := scalaV
  )
  .dependsOn(desktop, external)

lazy val external = (project in file("external"))
  .settings(
    name := "scalategy-external",
    version := appV,
    scalaVersion := scalaV,
    resourceDirectory in Compile := baseDirectory.value / "assets"
  )

lazy val server = (project in file("server"))
  .settings(
    name := "scalategy-server",
    version := appV,
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.6"
    )
  )
  .dependsOn(shared, external)

lazy val shared = (project in file("shared"))
  .settings(
    name := "scalategy-shared",
    version := appV,
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "autowire" % "0.2.5",
      "com.lihaoyi" %% "upickle" % "0.4.3"
    )
  )

lazy val tools = (project in file("tools"))
  .settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-tools" % "1.9.6"
    )
  )
  .dependsOn(desktop)

lazy val hiero = (project in file("tools/hiero"))
  .settings(
    scalaVersion := scalaV,
    mainClass in(Compile, run) := Some("com.badlogic.gdx.tools.hiero.Hiero"),
    fork := true
  )
  .dependsOn(tools)

lazy val particleEditor = (project in file("tools/particleEditor"))
  .settings(
    scalaVersion := scalaV,
    mainClass in(Compile, run) := Some("com.badlogic.gdx.tools.particleeditor.ParticleEditor"),
    fork := true
  )
  .dependsOn(tools)

