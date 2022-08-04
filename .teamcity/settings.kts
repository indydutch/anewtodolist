import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.04"

project {

    vcsRoot(HttpsGithubComIndydutchCheckBuildSequences1git)

    buildType(Step2)
    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
        }
    }
})

object Step2 : BuildType({
    name = "Step 2"

    vcs {
        root(HttpsGithubComIndydutchCheckBuildSequences1git)
    }
})

object HttpsGithubComIndydutchCheckBuildSequences1git : GitVcsRoot({
    name = "https://github.com/indydutch/check-build-sequences-1.git"
    url = "https://github.com/indydutch/check-build-sequences-1.git"
    branch = "main"
    authMethod = password {
        userName = "indydutch"
        password = "credentialsJSON:41aaa045-ee81-4936-b334-9b0cdafe9818"
    }
})
