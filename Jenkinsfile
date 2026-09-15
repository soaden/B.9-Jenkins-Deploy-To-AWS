def gv

pipeline {   
    agent any
    tools {
        maven 'maven-3.9.16'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            when {
        branch 'master'
            }
            steps {
                script {
                    gv.buildJar()

                }
            }
        }

        stage("build image") {
            when {
        branch 'master'
            }
            steps {
                script {
                    gv.buildImage()
                }
            }
        }

        stage("deploy") {
            when {
        branch 'master'
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }               
    }
} 
