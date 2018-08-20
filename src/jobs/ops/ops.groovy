String basePath = 'operations'
String repo = 'agaveplatform/jenkins-jobs'


folder(basePath) {
    displayName('Operations')
    description('Contains jobs to run operational tasks on an existing platform deployment')
}

folder(basePath + '/auth') {
    displayName('Auth Stack')
    description('Operational tasks specific to the Auth component stack.')
}


job(basePath + "/auth/auth_deploy") {
    description("Performs a rolling deployment on the Auth component host by rotating the active containers in an A/B deployment.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/auth_rolling_deploy.plbk")

        ansiblePlaybook("\$JENKINS_HOME/agave-deployer/deploy/auth_rolling_deploy.plbk") {
            inventoryPath("\$JENKINS_HOME/agave-deployer/inventory/sandbox.yml")
//            inventoryContent(String content, boolean dynamic = false)
//            ansibleName(String name)
//            limit(String limit)
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
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/update_tenantplbk")
    }
}

job(basePath + "/auth/auth_update") {
    description("Updates the tenant config on an Auth component host then performs a rolling deployment. No data migration is performed during this operation.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/update_tenantplbk")
    }
}