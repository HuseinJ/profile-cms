pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'cd profile-api'
                sh 'mvn clean package'
            }
        }
    }

    post {
        success {
            archiveArtifacts(artifacts: 'target/*.jar', allowEmptyArchive: true)
        }
    }
}