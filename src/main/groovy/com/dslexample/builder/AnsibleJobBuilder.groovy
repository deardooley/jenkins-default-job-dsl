package com.dslexample.builder

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job



/**
 * Example Class for creating a Gradle build
 */
@Builder(builderStrategy = SimpleStrategy, prefix = '')
class AnsibleJobBuilder {

    String name
    String description
    String ownerAndProject
    String gitBranch = 'master'
    String pollScmSchedule = '@daily'
    Boolean disabled = false
    String jobPlaybook
    String jobInventoryPath
    String jobLimit
    List<String> emails = []

    Job build(DslFactory dslFactory) {
        dslFactory.job(name) {
            it.description this.description
            disabled(disabled)
            logRotator {
                numToKeep 5
            }
            scm {
                if (this.ownerAndProject) {
                    github this.ownerAndProject, gitBranch
                }
            }
            triggers {
                if (pollScmSchedule) {
                    scm pollScmSchedule
                }
            }
            steps {

                ansbilePlaybook(jobPlaybook) {
                    inventoryPath(jobInventoryPath)
                    if (jobLimit) {
                        limit(jobLimit)
                    }
                }
            }
        }
    }
}
