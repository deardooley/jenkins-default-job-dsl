String basePath = 'tooling/python-sdk'
String playbookDir = "~/agave-deployer/deploy"
String inventoryPath = "playbookDir/inventory/sandbox.yml"



folder(basePath) {
    displayName('Python SDK')
    description('Contains jobs to run build, test, and release tasks the Agave Python SDK')
}


job(basePath + "/Python SDK Unit Tests") {
    description("This job runs the bats test suites for the Agave CLI. The tests are output in TAP format and published with the Jenkins TAP Plugin.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    scm {
        git {
            remote {
                github("agaveplatform/python-sdk")
            }
            branch("*/master")
        }
    }
    triggers {
        scm("H/5 * * * *") {
            ignorePostCommitHooks(false)
        }
    }
    steps {
        wrappers {
            credentialsBinding {
                usernamePassword('AGAVE_USERNAME', 'AGAVE_PASSWORD', 'python-sdk-test-credentials')
                usernamePassword('AGAVE_ADMIN_USERNAME', 'AGAVE_ADMIN_PASSWORD', 'python-sdk-test-admin-credentials')
                string("AGAVE_TENANT", "python-sdk-test-tenant")
                string("AGAVE_TENANTS_API_BASEURL", "python-sdk-test-apiserver")

                file('TEST_CREDENTIALS', 'python-sdk-test-credentials')
                file('ADMIN_CREDENTIALS', 'python-sdk-test-admin-credentials')
                string('LARGE_FILE', 'python-sdk-large-file-path')
                //
                //            string("AGAVE_SYSTEM", "python-sdk-test-system")
                //            string("AGAVE_SYSTEM_KEY", "python-sdk-test-system-key")
                //            string("AGAVE_EXECUTION", "python-sdk-test-execution")
                //            string("AGAVE_EXECUTION_KEY", "python-sdk-test-execution-key")
                //            string("AGAVE_APP_NAME", "python-sdk-test-app-name")
                //            string("AGAVE_DEPLOYMENT_PATH", "python-sdk-test-deployment-path")
                //            string("AGAVE_JWT", "python-sdk-test-jwt")
                //            string("AGAVE_JWT_HEADER", "python-sdk-test-jwt-header")
                //            string("AGAVE_VERIFY_CERTS", "false")
            }

            environmentVariables {
                env("AGAVE_SYSTEM", "python-sdk-test-storage")
                env("AGAVE_SYSTEM_KEY", "")
                env("AGAVE_EXECUTION", "python-sdk-test-execution")
                env("AGAVE_EXECUTION_KEY", "")
                env("AGAVE_APP_NAME", "python-sdk-test-app-name")
                env("AGAVE_DEPLOYMENT_PATH", "python-sdk-test-deployment-path")
                env("AGAVE_JWT", "")
                env("AGAVE_JWT_HEADER", "X-JWT-HEADER-sandbox")
                env("AGAVE_VERIFY_CERTS", "false")
            }



            shell '''
                # copy credentials into the source directory that we will be copying into position
                ln -s $TEST_CREDENTIALS agavepy/tests/test_credentials.json
                ln -s $ADMIN_CREDENTIALS agavepy/tests/test_credentials_admin_tests.json
                ln -s $LARGE_FILE agavepy/tests/test_largefile_upload_python_sdk
            '''.stripIndent().trim()


            shell '''
                docker-compose run --rm tox
            '''.stripIndent().trim()
        }
    }
    publishers {
        archiveJunit("agavepy/test/reports/*.xml") {
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
