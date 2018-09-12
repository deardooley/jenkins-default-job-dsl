String basePath = 'tooling'
String playbookDir = "~/agave-deployer/deploy"
String inventoryPath = "playbookDir/inventory/sandbox.yml"



folder(basePath) {
    displayName('Tooling')
    description('Contains jobs to run operational tasks on Agave tooling')
}
