package ops

String basePath = 'provisioning'
String repo = 'agaveplatform/jenkins-default-job-dsl'


folder(basePath) {
    displayName('Provisioning')
    description('Contains jobs to provision infrastructure for platform operations, training clusters, etc.')
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

        shell("#ansible-playbook -i $JENKINS_HOME/agave-deployer/inventory/openstack.yml $JENKINS_HOME/agave-deployer/deploy/deploy_agave.plbk")
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

folder(basePath + '/terraform') {
    displayName('Terraform')
    description('Terraform based provisioning.')
}


job(basePath + '/terraform/tf_apply_training_swarm') {
    description("Deploy a new training swarm on OpenStack")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    parameters {
        stringParam('BRANCH', 'master')
    }
    scm {
        git {
            remote {
                github("git@github.com:agaveplatform/terraform-training-swarm.git", "ssh")
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
        shell("docker run -it --rm -w /data -v \$(pwd):/data -v \$(pwd)/keys:/keys hashicorp/terraform:full apply -auto-approve")
    }
}

job(basePath + '/terraform/tf_plan_training_swarm') {
    description("Deploy a new training swarm on OpenStack")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    parameters {
        stringParam('BRANCH', 'master')
    }
    scm {
        git {
            remote {
                github("git@github.com:agaveplatform/terraform-training-swarm.git", "ssh")
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
        shell("docker run -it --rm -w /data -v \$(pwd):/data -v \$(pwd)/keys:/keys hashicorp/terraform:full plan")
    }
}

job(basePath + '/terraform/tf_destroy_training_swarm') {
    description("Deploy a new training swarm on OpenStack")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    parameters {
        stringParam('BRANCH', 'master')
    }
    scm {
        git {
            remote {
                github("git@github.com:agaveplatform/terraform-training-swarm.git", "ssh")
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
        shell("docker run -it --rm -w /data -v \$(pwd):/data -v \$(pwd)/keys:/keys hashicorp/terraform:full destroy -force")
    }
}