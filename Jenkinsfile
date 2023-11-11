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

        stage('Test') {
        agent any
            steps {
                sh 'cd profile-api && mvn clean test'
            }
        }

        stage('Qodana') {
                agent {
                    docker {
                      args '''
                      -v "${WORKSPACE}":/data/project
                      --entrypoint=""
                      '''
                      image 'jetbrains/qodana-jvm:2023.2'
                    }
                }
                  steps {
                    sh '''
                    qodana \
                    --fail-threshold 5 \
                    --baseline /qodana.sarif.json
                    '''
                  }
                }

        stage('Build Docker') {
        agent any
            steps {
                sh "cd profile-api && docker build -t profile-api:${env.BUILD_NUMBER} ."
            }
        }
    }
}