pipeline {
    agent any
    tools {
        // Specify the name of the Maven installation defined in the Jenkins configuration.
        maven 'Maven'
        dockerTool 'docker'
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
                sh "cd profile-api && docker build -t profile-api:${env.BUILD_NUMBER} ."
            }
        }

        stage('Deploy Profile Blog') {
            steps {
                script {
                    def apiPort = "8081"
                    def baseAdminUrl = "https://admin.hjusic.com"
                    def adminEmail = "husein.jusic@icloud.com"

                    withEnv(["API_PORT=${apiPort}",
                            "BASE_ADMIN_URL=${baseAdminUrl}
                            "ADMIN_EMAIL=${adminEmail}"]) {
                        sh "docker-compose up -d"
                    }
                }
                sh "docker compose up -d"
            }
        }
    }

    post {
        success {
            archiveArtifacts(artifacts: 'target/*.jar', allowEmptyArchive: true)
        }
    }
}