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