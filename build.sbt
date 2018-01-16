import com.typesafe.sbt.SbtGit.GitKeys
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import com.typesafe.sbt.git.DefaultReadableGit

import scalariform.formatter.preferences._

name := "netty-in-action-scala"

version := "1.0.0"

scalaVersion := "2.12.4"

scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.8", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint")

javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-XDignore.symbol.file")

resolvers += "akka" at "https://dl.bintray.com/akka/maven/"

enablePlugins(ParadoxSitePlugin)

enablePlugins(GhpagesPlugin)

enablePlugins(AutomateHeaderPlugin)

git.remoteRepo := "https://github.com/ReactivePlatform/netty-in-action-scala.git"

GitKeys.gitReader in ThisProject := baseDirectory(base => new DefaultReadableGit(base)).value

excludeFilter in ghpagesCleanSite :=
  new FileFilter{
    def accept(f: File) = (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath
  } || "versions.html"

enablePlugins(ParadoxMaterialThemePlugin)

ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox)


paradoxProperties in Compile ++= Map(
  "project.name" -> "NettyInAction",
  "github.base_url" -> "https://github.com/ReactivePlatform/netty-in-action-scala"
)

paradoxMaterialTheme in Compile ~= {
  _.withColor("red", "pink")
    .withLogoIcon("cloud")
    .withCopyright("Copyleft © 2017 netty.reactiveplatform.xyz")
    .withRepository(uri("https://github.com/ReactivePlatform/netty-in-action-scala.git"))
    .withSearch(tokenizer = "[\\s\\-\\.]+")
    .withSocial(
      uri("https://github.com/hepin1989")
    )
}

organizationName := "netty.reactiveplatform.xyz"
startYear := Some(2018)
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

def setPreferences(preferences: IFormattingPreferences): IFormattingPreferences = preferences
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentConstructorArguments, false)
  .setPreference(DoubleIndentMethodDeclaration, false)
  .setPreference(DanglingCloseParenthesis, Preserve)
  .setPreference(NewlineAtEndOfFile, true)

ScalariformKeys.preferences := setPreferences(ScalariformKeys.preferences.value)
ScalariformKeys.preferences in Compile := setPreferences(ScalariformKeys.preferences.value)
ScalariformKeys.preferences in Test := setPreferences(ScalariformKeys.preferences.value)

libraryDependencies += "io.netty" % "netty-all" % "4.1.19.Final"
libraryDependencies += "junit" % "junit" % "4.12"

scalafixSettings

scalafixConfigure(Compile)