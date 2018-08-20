
String basePath = 'provisioning'
String playbookDir = "~/agave-deployer/deploy"
String staticInventoryPath = "$playbookDir/inventory/sandbox.yml"
String dynamicInventoryPath = "$playbookDir/inventory/openstack.yml"



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
    disabled(true)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $staticInventoryPath $playbookDir/os_provision.plbk")

//        ansiblePlaybook(playbookDir + "/os_provision.plbk") {
//            inventoryPath(staticInventoryPath)
//            limit("auth")
//        }
//
//        ansiblePlaybook(playbookDir + "/deploy_agave.plbk") {
//            inventoryPath(dynamicInventoryPath)
//            limit("auth")
//        }
    }
}

job(basePath + '/openstack/os_provision_ci') {
    description("Creates a new VM on an OpenStack cloud and runs the basic configuration playbooks needed for all CI servers.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $staticInventoryPath $playbookDir/os_provision_ci.plbk")

//        ansiblePlaybook(playbookDir + "/os_provision_ci.plbk") {
//            inventoryPath(staticInventoryPath)
//            limit("auth")
//        }
    }
}


//new AnsibleJobBuilder()
//        .name("$basePath/openstack/os_provision")
//        .description("Provisions 4 VM on an OpenStack cloud and runs the basic configuration playbooks needed for all Agave platform servers.")
//        .disabled(true)
//        .jobPlaybook("$playbookDir/os_provision.plbk")
//        .jobInventoryPath(staticInventoryPath)
//        .jobLimit("local")
//        .build(this)
//
//new AnsibleJobBuilder()
//        .name("$basePath/openstack/os_provision_ci")
//        .description("Creates a new VM on an OpenStack cloud and runs the basic configuration playbooks needed for all CI servers.")
//        .disabled(true)
//        .jobPlaybook("$playbookDir/os_provision_ci.plbk")
//        .jobInventoryPath(staticInventoryPath)
//        .jobLimit("local")
//        .build(this)

folder(basePath + '/terraform') {
    displayName('Terraform')
    description('Terraform based provisioning.')
}


job(basePath + '/terraform/tf_apply_training_swarm') {
    description("Deploy a new training swarm on OpenStack")
    keepDependencies(false)
    disabled(true)
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
    disabled(true)
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
    disabled(true)
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