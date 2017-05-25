lazy val root =
  (project in file("."))
    .settings(
	  name         := "pedcbimport",
	  version      := "0.1.0",
	  scalaVersion := "2.11.8")

lazy val enumeratumVersion = "1.5.1"
lazy val playVersion       = "2.5.10"
lazy val jacksonVersion    = "2.8.2"
lazy val commonsVersion    = "3.5"

libraryDependencies ++=
	Seq(
		"com.typesafe.play" %% "play-json" % "2.5.10" withSources() withJavadoc(),
		
		"com.beachape" %% "enumeratum" %           enumeratumVersion withSources() withJavadoc(),
		"com.beachape" %% "enumeratum-play-json" % enumeratumVersion withSources() withJavadoc(),
		
		"com.fasterxml.jackson.core"       % "jackson-databind"          % jacksonVersion withSources() withJavadoc(),
		"com.fasterxml.jackson.module"     % "jackson-module-scala_2.11" % jacksonVersion withSources() withJavadoc(),
		"com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml"   % jacksonVersion withSources() withJavadoc(),
		
		"org.apache.commons" % "commons-math3" % commonsVersion,
		"org.apache.commons" % "commons-lang3" % commonsVersion,
		
		// tests
		"junit" % "junit" % "4.12" % "test",
		"com.novocode" % "junit-interface" % "0.11" % "test" /* see https://github.com/sbt/junit-interface */)

mainClass in (Compile, run) := Some("pedcbimport.PedCBImport")

