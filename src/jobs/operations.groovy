String basePath = 'operations'
String playbookDir = "~/agave-deployer/deploy"
String inventoryFilePath = playbookDir + "/inventory/sandbox.yml"

String playbookPath = ""

folder(basePath) {
    displayName('Operations')
    description('Contains jobs to run operational tasks on an existing platform deployment')
}

folder(basePath + '/auth') {
    displayName('Auth Components')
    description('Operational tasks specific to the Auth component stack.')
}


job(basePath + "/auth/auth_bounce") {
    description("Performs a rolling deployment on the Auth component host by rotating the active containers in an A/B deployment.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/auth_rolling_deploy.plbk")

        ansiblePlaybook(playbookDir + "/auth_rolling_deploy.plbk") {
            inventoryPath(inventoryFilePath)
            limit("auth")
        }

    }
}

job(basePath + "/auth/Update Auth Stack") {
    description("Updates the tenant config on an Auth component host then performs a rolling deployment. No data migration is performed during this operation.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/update_auth.plbk -e update_docker_version=False -e update_docker_compose_version=False")
//        ansiblePlaybook(playbookDir + "/update_tenant.plbk") {
//            inventoryPath(inventoryFilePath)
//            limit("auth")
//        }
    }
}

folder(basePath + '/core') {
    displayName('Core Components')
    description('Operational tasks specific to the Core component stack.')
}

job(basePath + "/core/Update Science APIs") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)


    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/update_core.plbk -e update_docker_version=False -e update_docker_compose_version=False")

//        ansiblePlaybook(playbookDir + "/update_core.plbk")  {
//            inventoryPath(inventoryFilePath)
//            limit("core")
//            extraVars {
//                extraVar("update_docker_version", "false", false)
//                extraVar("update_docker_compose_version", "false", false)
//            }
//        }
    }
}

folder(basePath + '/db') {
    displayName('Database Components')
    description('Operational tasks specific to the Database component stack.')
}


job(basePath + "/db/Migrate Core DB") {
    description("Runs the default migrations on the Core science API DB. note: This is automatically run when updating the Core services.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/load_core_migrations.plbk")

    }
}

job(basePath + "/db/Backup Auth MySQL") {
    description("Dumps the APIM DB to the /tmp/mysql-backup/auth/latest folder on the auth db host, rotating the previous dump to a timestamped folder.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/backup_auth_mysql.plbk")

    }
}

job(basePath + "/db/Backup Core MySQL") {
    description("Dumps the Core Science API DB to the /tmp/mysql-backup/core/latest folder on the core db host, rotating the previous dump to a timestamped folder.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/backup_core_mysql.plbk")

    }
}

job(basePath + "/db/Restore Auth MySQL") {
    description("Restores the APIM DB from the default backup location, /tmp/mysql-backup/auth/latest, on the auth db host.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/restore_auth_mysql.plbk")

    }
}

job(basePath + "/db/Restore Core MySQL") {
    description("Restores the Core Science API DB from the default backup location, /tmp/mysql-backup/auth/latest, on the auth db host.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/restore_core_mysql.plbk")

    }
}

folder(basePath + '/web') {
    displayName('Web Components')
    description('Operational tasks specific to the Web component stack.')
}

job(basePath + "/web/Update Agave ToGo") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(false)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/update_togo.plbk -e update_docker_version=False -e update_docker_compose_version=False")
//        ansiblePlaybook(playbookDir + "/update_togo.plbk") {
//            inventoryPath(inventoryFilePath)
//            limit("togo")
//            extraVars {
//                extraVar("update_docker_version", "false", false)
//                extraVar("update_docker_compose_version", "false", false)
//            }
//        }
    }
}

job(basePath + "/web/Update Agave Website") {
    description("Updates the core science API config on all core hosts. This proceeds one host at a time, stopping upon failure.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/update_website.plbk -e update_docker_version=False -e update_docker_compose_version=False")
//        ansiblePlaybook(playbookDir + "/update_website.plbk") {
//            inventoryPath(inventoryFilePath)
//            limit("website")
//            extraVars {
//                extraVar("update_docker_version", "false", false)
//                extraVar("update_docker_compose_version", "false", false)
//            }
//        }
    }
}

job(basePath + "/web/Update Support Services") {
    description("Updates the public-facing support services on web component hosts.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/update_support_services.plbk -e update_docker_version=False -e update_docker_compose_version=False")
//        ansiblePlaybook(playbookDir + "/update_support_services.plbk") {
//            inventoryPath(inventoryFilePath)
//            limit("support")
//            extraVars {
//                extraVar("update_docker_version", "false", false)
//                extraVar("update_docker_compose_version", "false", false)
//            }
//        }
    }
}

job(basePath + "/web/Update Developer Documentation") {
    description("Updates the self-hosted Agave developer documentation.")
    keepDependencies(false)
    disabled(true)
    concurrentBuild(false)
    steps {
        shell("ansible-playbook -i $inventoryFilePath $playbookDir/update_docs.plbk -e update_docker_version=False -e update_docker_compose_version=False")
//        ansiblePlaybook(playbookDir + "/update_docs.plbk") {
//            inventoryPath(inventoryFilePath)
//            limit("documentation")
//            extraVars {
//                extraVar("update_docker_version", "false", false)
//                extraVar("update_docker_compose_version", "false", false)
//            }
//        }
    }
}