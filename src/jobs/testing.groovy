String basePath = 'tests'
String playbookDir = "~/agave-deployer/deploy"
String staticInventoryPath = "$playbookDir/inventory/sandbox.yml"

folder('tests') {
    displayName('Tests')
    description('Contains jobs to run various test suites grouped by platform component')
}

folder('tests/auth') {
    displayName('Auth')
    description('Contains jobs to run various tests against the platform auth components: profiles, clients, and admin apis, apim, user registration app, tenant configuration, etc.')
}

job("tests/auth/admin-api-tests") {
    description("This job runs the Django test suite for the Agave admin services.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(true)
    steps {
        shell '''
		  ansible-playbook -i $staticInventoryPath $playbookDir/run_admin_tests.plbk
      '''.stripIndent().trim()
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

folder('tests/db') {
    displayName('DB')
    description('Contains jobs to run various tests against the platform database components: mongodb, mysql, beanstalkd, redis, etc.')
}


folder('tests/core') {
    displayName('Core')
    description('Contains jobs to run various tests against the platform core science api components: REST API, workers, notifications, etc.')
}


job("tests/core/core-unit-tests") {
    description("This job compiles the science apis, runs the unit tests, and builds the snapshot Docker images.")
    keepDependencies(false)
    disabled(true)
    scm {
        git {
            remote {
                github("agaveplatform/science-apis")
            }
            branch("*/develop")
        }
    }
    triggers {
        scm("H/5 * * * *") {
            ignorePostCommitHooks(false)
        }
    }
    concurrentBuild(true)
    steps {
        shell '''
		      docker-compose -f docker-compose.test.yml run --rm junit
      '''.stripIndent().trim()

        shell '''
		      # config/testbed/onerun-simulate-jenkins.sh `pwd` transforms "mysql mongodb beanstalkd pushpin" agaveapi/slave-mvn-runner:1.0 \$HOME
		  '''.stripIndent().trim()
    }
    publishers {
        archiveJunit("testreports/files-testng.xml/surefire-reports/*.xml") {
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

job("tests/core/core-integration-tests") {
    description("This job runs the integration tests for the science APIs, validating support for schedulers, notifications services, data protocols, authentication mechanisms, etc.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(true)
    steps {
        shell '''
		      docker-compose -f docker-compose.test.yml run --rm integration
      '''.stripIndent().trim()
    }
    publishers {
        archiveJunit("testreports/files-testng.xml/surefire-reports/*.xml") {
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

job("tests/core/core-postman-tests") {
    description("This job runs the maven goals of compile, jar and install to local maven repository. The end result is a set of jars available for all services to satisfy the dependencies for further testing and docker image building.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(true)
    steps {
        shell '''
		      ansible-playbook -i $staticInventoryPath $playbookDir/run_postman_tests.plbk
      '''.stripIndent().trim()
    }
    publishers {
        archiveJunit("~/agave-deployer/deploy/tmp/agave_postman_tester*/reports/*.xml") {
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


