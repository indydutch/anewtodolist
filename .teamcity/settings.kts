import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.python
import jetbrains.buildServer.configs.kotlin.triggers.finishBuildTrigger
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

    vcsRoot(HttpsGithubComIndydutchCheckBuildSequences0gitRefsHeadsMain)
    vcsRoot(HttpsGithubComIndydutchCheckBuildSequences1git)
    vcsRoot(HttpsGithubComIndydutchCheckBuildSequences2gitRefsHeadsMain)

    buildType(Step2)
    buildType(Step1)
    buildType(Build1)
    buildType(Build)
    buildType(BuildAll)
}

object Build : BuildType({
    name = "Build"

    params {
        checkbox("DryRun", "", description = "If enabled, does not perform any publish, only reports the actions it would take.", display = ParameterDisplay.PROMPT,
                  checked = "-d")
        text("PublishVersion", "", description = """Publish Version we are uploading. Typically the shortened version, for example 12.0 or 11.2.4, must match the start of the version in the files provided as input. Set "preview" to publish preview build.""", display = ParameterDisplay.PROMPT, allowEmpty = false)
        text("Confirmation", "test", description = "WARNING: This will upload the installers to the web site. Please review the Dependencies tab for what versions will be published.", display = ParameterDisplay.PROMPT,
              regex = """\biamauthorizedtorunthisbuild\b""", validationMessage = "Please contact the product director before continuing.")
        password("TeamCity_Token", "credentialsJSON:f7bc0850-b012-4008-968d-7c4abbaf922e", label = "TeamCity token", description = "Enter your TeamCity token to use for the tagging. NOTE: You can generate it on personal profile page - https://tc.accusoft.com/profile.html?item=accessTokens#", display = ParameterDisplay.PROMPT)
    }

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
            enabled = false
        }
        finishBuildTrigger {
            enabled = false
            buildType = "${Step2.id}"
        }
        finishBuildTrigger {
            enabled = false
            buildType = "${Build1.id}"
            successfulOnly = true
        }
    }

    dependencies {
        snapshot(Build1) {
        }
        snapshot(Step2) {
        }
    }
})

object Build1 : BuildType({
    name = "Step 2.1"

    params {
        checkbox("DryRun", "", description = "If enabled, does not perform any publish, only reports the actions it would take.", display = ParameterDisplay.PROMPT,
                  checked = "-d")
        text("PublishVersion", "", description = """Publish Version we are uploading. Typically the shortened version, for example 12.0 or 11.2.4, must match the start of the version in the files provided as input. Set "preview" to publish preview build.""", display = ParameterDisplay.PROMPT, allowEmpty = false)
        text("Confirmation", "", description = "WARNING: This will upload the installers to the web site. Please review the Dependencies tab for what versions will be published.", display = ParameterDisplay.PROMPT,
              regex = """\biamauthorizedtorunthisbuild\b""", validationMessage = "Please contact the product director before continuing.")
        password("TeamCity_Token", "credentialsJSON:f7bc0850-b012-4008-968d-7c4abbaf922e", label = "TeamCity token", description = "Enter your TeamCity token to use for the tagging. NOTE: You can generate it on personal profile page - https://tc.accusoft.com/profile.html?item=accessTokens#", display = ParameterDisplay.PROMPT)
    }

    vcs {
        root(HttpsGithubComIndydutchCheckBuildSequences2gitRefsHeadsMain)
    }

    steps {
        python {
            command = file {
                filename = "step2-1.py"
            }
        }
    }

    triggers {
        finishBuildTrigger {
            enabled = false
            buildType = "${Step1.id}"
        }
    }

    dependencies {
        snapshot(Step1) {
        }
    }
})

object BuildAll : BuildType({
    name = "Build All"

    params {
        checkbox("env.DRYRUN", "", description = "If enabled, does not perform any publish, only reports the actions it would take.", display = ParameterDisplay.PROMPT,
                  checked = "-d")
        password("env.TEAMCITY_TOKEN", "credentialsJSON:f7bc0850-b012-4008-968d-7c4abbaf922e", label = "TeamCity token", description = "Enter your TeamCity token to use for the tagging. NOTE: You can generate it on personal profile page - https://tc.accusoft.com/profile.html?item=accessTokens#", display = ParameterDisplay.PROMPT)
        text("env.CONFIRMATION", "", description = "WARNING: This will upload the installers to the web site. Please review the Dependencies tab for what versions will be published.", display = ParameterDisplay.PROMPT,
              regex = """\biamauthorizedtorunthisbuild\b""", validationMessage = "Please contact the product director before continuing.")
        text("env.PUBLISHVERSION", "", description = """Publish Version we are uploading. Typically the shortened version, for example 12.0 or 11.2.4, must match the start of the version in the files provided as input. Set "preview" to publish preview build.""", display = ParameterDisplay.PROMPT, allowEmpty = false)
    }

    dependencies {
        snapshot(Build) {
        }
    }
})

object Step1 : BuildType({
    name = "Step 1"

    params {
        checkbox("DryRun", "", description = "If enabled, does not perform any publish, only reports the actions it would take.", display = ParameterDisplay.PROMPT,
                  checked = "-d")
        text("PublishVersion", "", description = """Publish Version we are uploading. Typically the shortened version, for example 12.0 or 11.2.4, must match the start of the version in the files provided as input. Set "preview" to publish preview build.""", display = ParameterDisplay.PROMPT, allowEmpty = false)
        text("Confirmation", "", description = "WARNING: This will upload the installers to the web site. Please review the Dependencies tab for what versions will be published.", display = ParameterDisplay.PROMPT,
              regex = """\biamauthorizedtorunthisbuild\b""", validationMessage = "Please contact the product director before continuing.")
        password("TeamCity_Token", "credentialsJSON:f7bc0850-b012-4008-968d-7c4abbaf922e", label = "TeamCity token", description = "Enter your TeamCity token to use for the tagging. NOTE: You can generate it on personal profile page - https://tc.accusoft.com/profile.html?item=accessTokens#", display = ParameterDisplay.PROMPT)
    }

    vcs {
        root(HttpsGithubComIndydutchCheckBuildSequences0gitRefsHeadsMain)
    }

    steps {
        python {
            command = file {
                filename = "step1.py"
            }
        }
    }
})

object Step2 : BuildType({
    name = "Step 2"

    params {
        checkbox("DryRun", "", description = "If enabled, does not perform any publish, only reports the actions it would take.", display = ParameterDisplay.PROMPT,
                  checked = "-d")
        text("PublishVersion", "", description = """Publish Version we are uploading. Typically the shortened version, for example 12.0 or 11.2.4, must match the start of the version in the files provided as input. Set "preview" to publish preview build.""", display = ParameterDisplay.PROMPT, allowEmpty = false)
        text("Confirmation", "", description = "WARNING: This will upload the installers to the web site. Please review the Dependencies tab for what versions will be published.", display = ParameterDisplay.PROMPT,
              regex = """\biamauthorizedtorunthisbuild\b""", validationMessage = "Please contact the product director before continuing.")
        password("TeamCity_Token", "credentialsJSON:f7bc0850-b012-4008-968d-7c4abbaf922e", label = "TeamCity token", description = "Enter your TeamCity token to use for the tagging. NOTE: You can generate it on personal profile page - https://tc.accusoft.com/profile.html?item=accessTokens#", display = ParameterDisplay.PROMPT)
    }

    vcs {
        root(HttpsGithubComIndydutchCheckBuildSequences1git)
    }

    steps {
        python {
            command = file {
                filename = "step2.py"
            }
        }
    }

    triggers {
        finishBuildTrigger {
            enabled = false
            buildType = "${Step1.id}"
        }
    }

    dependencies {
        snapshot(Step1) {
        }
    }
})

object HttpsGithubComIndydutchCheckBuildSequences0gitRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/indydutch/check-build-sequences-0.git#refs/heads/main"
    url = "https://github.com/indydutch/check-build-sequences-0.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "indydutch"
        password = "credentialsJSON:41aaa045-ee81-4936-b334-9b0cdafe9818"
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

object HttpsGithubComIndydutchCheckBuildSequences2gitRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/indydutch/check-build-sequences-2.git#refs/heads/main"
    url = "https://github.com/indydutch/check-build-sequences-2.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "indydutch"
        password = "credentialsJSON:41aaa045-ee81-4936-b334-9b0cdafe9818"
    }
})
