
job("ops/auth/auth_deploy") {
    description("Performs a rolling deployment on the Auth component host by rotating the active containers in an A/B deployment.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/auth_rolling_deploy.plbk")
    }
}

job("ops/auth/auth_update") {
    description("Updates the tenant config on an Auth component host then performs a rolling deployment. No data migration is performed during this operation.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/update_tenantplbk")
    }
}

job("ops/auth/auth_update") {
    description("Updates the tenant config on an Auth component host then performs a rolling deployment. No data migration is performed during this operation.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml $JENKINS_HOME/agave-deployer/deploy/update_tenantplbk")
    }
}