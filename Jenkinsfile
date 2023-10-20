pipeline {
    agent any
    tools {
        // Specify the name of the Maven installation defined in the Jenkins configuration.
        maven 'Maven'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh 'cd profile-api && mvn clean test'
            }
        }

        stage('Build Docker') {
            steps {
                sh 'cd profile-api && docker build -t profile-api:${env.BUILD_NUMBER} .'
            }
        }

        stage('Deploy Profile Api') {
            steps {

            }
        }
    }

    post {
        success {
            archiveArtifacts(artifacts: 'target/*.jar', allowEmptyArchive: true)
        }
    }
}