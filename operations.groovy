String basePath = 'operations'
String inventoryPath = "\$JENKINS_HOME/agave-deployer/inventory/sandbox.yml"
String playbookDir = "\$JENKINS_HOME/agave-deployer/deploy"


folder(basePath) {
    displayName('Operations')
    description('Contains jobs to run operational tasks on an existing platform deployment')
}

folder(basePath + '/auth') {
    displayName('Auth Stack')
    description('Operational tasks specific to the Auth component stack.')
}


job(basePath + "/auth/auth_bounce") {
    description("Performs a rolling deployment on the Auth component host by rotating the active containers in an A/B deployment.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        //shell("ansible-playbook -i \$JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/auth_rolling_deploy.plbk")

        ansiblePlaybook(playbookDir + "/auth_rolling_deploy.plbk") {
            inventoryPath(inventoryPath)
//            inventoryContent(String content, boolean dynamic = false)
//            ansibleName(String name)
            limit("auth")
//            tags(String tags)
//            skippedTags(String tags)
//            startAtTask(String task)
//            credentialsId(String id)
//            become(boolean become = true)
//            becomeUser(String user = 'root')
//            sudo(boolean sudo = true)
//            sudoUser(String user = 'root')
//            forks(int forks = 5)
//            unbufferedOutput(boolean unbufferedOutput = true)
//            colorizedOutput(boolean colorizedOutput = false)
//            hostKeyChecking(false)
//            additionalParameters(String params)
//            extraVars {
//                extraVar(String key, String value, boolean hidden)
//            }
        }

    }
}

job(basePath + "/auth/auth_update") {
    description("Updates the tenant config on an Auth component host then performs a rolling deployment. No data migration is performed during this operation.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_tenant.plbk") {
            inventoryPath(inventoryPath)
            limit("auth")
        }
    }
}

folder(basePath + '/core') {
    displayName('Core Science APIs')
    description('Operational tasks specific to the Core component stack.')
}


job(basePath + "/core/core_update") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_core.plbk") {
            inventoryPath(inventoryPath)
            limit("core")
        }
    }
}

job(basePath + "/core/core_update") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_core.plbk") {
            inventoryPath(inventoryPath)
            limit("core")
            extraVars {
                extraVar("update_docker_version", "false", false)
                extraVar("update_docker_compose_version", "false", false)
            }
        }
    }
}

folder(basePath + '/web') {
    displayName('Web components')
    description('Operational tasks specific to the Web component stack.')
}

job(basePath + "/web/update_togo") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_togo.plbk") {
            inventoryPath(inventoryPath)
            limit("togo")
            extraVars {
                extraVar("update_docker_version", "false", false)
                extraVar("update_docker_compose_version", "false", false)
            }
        }
    }
}

job(basePath + "/web/update_website") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_website.plbk") {
            inventoryPath(inventoryPath)
            limit("website")
            extraVars {
                extraVar("update_docker_version", "false", false)
                extraVar("update_docker_compose_version", "false", false)
            }
        }
    }
}

job(basePath + "/web/update_support_services") {
    description("Updates the public-facing support services on web component hosts.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_support_services.plbk") {
            inventoryPath(inventoryPath)
            limit("support")
            extraVars {
                extraVar("update_docker_version", "false", false)
                extraVar("update_docker_compose_version", "false", false)
            }
        }
    }
}

job(basePath + "/web/update_docs") {
    description("Updates the agave developer documentation.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        ansiblePlaybook(playbookDir + "/update_docs.plbk") {
            inventoryPath(inventoryPath)
            limit("documentation")
            extraVars {
                extraVar("update_docker_version", "false", false)
                extraVar("update_docker_compose_version", "false", false)
            }
        }
    }
}