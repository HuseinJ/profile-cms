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
                sh 'mvn test'
            }
        }
    }

    post {
        success {
            archiveArtifacts(artifacts: 'target/*.jar', allowEmptyArchive: true)
        }
    }
}