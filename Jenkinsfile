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
    }

    post {
            success {
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                emailext (
                    body: """
                    <p>Hello Team,</p>
                    <p>The build was successful. Here are the details:</p>
                    <ul>
                        <li>Project: ${env.JOB_NAME}</li>
                        <li>Build Number: ${env.BUILD_NUMBER}</li>
                        <li>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></li>
                        <li>Duration: ${currentBuild.durationString}</li>
                        <li>Built By: ${currentBuild.getBuildCauses()}</li>
                    </ul>
                    <p>Please review the build artifacts and logs for more details.</p>
                    <p>Regards,<br>Jenkins</p>
                    """,
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                    subject: "SUCCESS: Build ${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                    mimeType: 'text/html'
                )
            }
            failure {
                emailext (
                    body: """
                    <p>Hello Team,</p>
                    <p>The build has failed. Here are the details:</p>
                    <ul>
                        <li>Project: ${env.JOB_NAME}</li>
                        <li>Build Number: ${env.BUILD_NUMBER}</li>
                        <li>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></li>
                        <li>Duration: ${currentBuild.durationString}</li>
                        <li>Built By: ${currentBuild.getBuildCauses()}</li>
                    </ul>
                    <p>Please review the build logs to diagnose the issue.</p>
                    <p>Regards,<br>Jenkins</p>
                    """,
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                    subject: "FAILURE: Build ${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                    mimeType: 'text/html'
                )
            }
        }
}