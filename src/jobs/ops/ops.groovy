String basePath = 'operations'
String repo = 'agaveplatform/jenkins-jobs'


folder(basePath) {
    displayName('Operations')
    description('Contains jobs to run operational tasks on an existing platform deployment')
}

folder(basePath + '/openstack') {
    displayName('OpenStack')
    description('OpenStack specific provisioning.')
}

job(basePath + '/openstack/os_provision') {
    description("Provisions 4 VM on an OpenStack cloud and runs the basic configuration playbooks needed for all Agave platform servers.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml -l localhost $JENKINS_HOME/agave-deployer/deploy/os_provision.plbk")
    }
}

job(basePath + '/openstack/os_provision_ci') {
    description("Creates a new VM on an OpenStack cloud and runs the basic configuration playbooks needed for all CI servers.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/sandbox.yml -l localhost $JENKINS_HOME/agave-deployer/deploy/os_provision_ci.plbk")
    }
}
