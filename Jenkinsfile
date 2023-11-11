pipeline {
    agent none
    environment {
          QODANA_TOKEN=credentials('qodana-token')
       }
    tools {
        // Specify the name of the Maven installation defined in the Jenkins configuration.
        maven 'Maven'
        dockerTool 'docker'
    }
    stages {
        stage('Checkout') {
        agent any
            steps {
                checkout scm
            }
        }

        stage('Qodana') {
        agent {
            docker {
              args '''
              -v "${WORKSPACE}":/data/project
              --entrypoint=""
              --baseline qodana.sarif.json
              '''
              image 'jetbrains/qodana-jvm'
            }
        }
          steps {
            sh '''qodana'''
          }
        }

        stage('Test') {
        agent any
            steps {
                sh 'cd profile-api && mvn clean test'
            }
        }

        stage('Build Docker') {
        agent any
            steps {
                sh "cd profile-api && docker build -t profile-api:${env.BUILD_NUMBER} ."
            }
        }
    }

    post {
        success {
            archiveArtifacts(artifacts: 'target/*.jar', allowEmptyArchive: true)
        }
    }
}