String basePath = 'tooling/cli'
String playbookDir = "~/agave-deployer/deploy"
String inventoryPath = "playbookDir/inventory/sandbox.yml"



folder(basePath) {
    displayName('CLI')
    description('Contains jobs to run build, test, and release tasks the Agave CLI')
}


job(basePath + "/CLI Unit Tests") {
    description("This job runs the bats test suites for the Agave CLI. The tests are output in TAP format and published with the Jenkins TAP Plugin.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    scm {
        git {
            remote {
                github("agaveplatform/agave-cli")
            }
            branch("*/develop")
        }
    }
    triggers {
        scm("H/5 * * * *") {
            ignorePostCommitHooks(false)
        }
    }
    steps {
        environmentVariables {
            env("AGAVE_USERNAME", "testuser")
            env("AGAVE_PASSWORD", "testuser")
            env("AGAVE_TENANT", "sandbox")
            env("AGAVE_TENANTS_API_BASEURL", "https://sandbox.agaveplatform.org/tenants")
        }

        shell '''
            mkdir -p reports
            bats --tap test/bin > reports/tap-report.tap
      '''.stripIndent().trim()
    }
    publishers {
        archiveJunit("reports/*.tap") {
            healthScaleFactor(1.0)
            allowEmptyResults(false)
        }
    }
    configure {
        it / 'properties' / 'jenkins.model.BuildDiscarderProperty' {
            strategy {
                'daysToKeep'('1')
                'numToKeep'('5')
                'artifactDaysToKeep'('-1')
                'artifactNumToKeep'('-1')
            }
        }
    }
}
