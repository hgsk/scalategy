val scalaV = "2.11.8"

lazy val core = (project in file("core"))
  .settings(
    name := "scalategy",
    version := "0.1",
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.6",
      "com.badlogicgames.gdx" % "gdx" % "1.9.6"
    )
  )

lazy val desktop = (project in file("desktop"))
  .settings(
    name := "scalategy-desktop",
    version := "0.1",
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-platform" % "1.9.6" classifier "natives-desktop"
    )
  )
  .dependsOn(core)
