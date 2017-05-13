val scalaV = "2.11.8"
val appV = "0.1"

lazy val core = (project in file("core"))
  .settings(
    name := "scalategy",
    version := appV,
    scalaVersion := scalaV,
    resourceDirectory in Compile := baseDirectory.value / "assets",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.6",
      "com.badlogicgames.gdx" % "gdx" % "1.9.6"
    )
  )

lazy val desktop = (project in file("desktop"))
  .settings(
    name := "scalategy-desktop",
    version := appV,
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
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
    scalaVersion := scalaV
  )
  .dependsOn(external)

lazy val shared = (project in file("shared"))
  .settings(
    name := "scalategy-shared",
    version := appV,
    scalaVersion := scalaV
  )
