pipeline {
    agent any

    stages {
        stage('Setup SSH') {
            steps {
                sshagent(credentials: ['983e013d-c597-4ad2-a37b-06ba50117b81']) {
                    sh 'scp target/your-web-app.war root@192.168.0.64:/opt/tomcat/webapps'
                }
            }
        }

        stage('Checkout') {
            steps {
                git 'https://github.com/IlyaGospodarev/postgres-project.git'
            }
        }

        stage('Build') {
            steps {
                sh '/usr/share/maven/bin/mvn clean package' // Replace with your build command
            }
        }

        stage('Deploy') {
            steps {
                sh 'scp target/your-web-app.war root@192.168.0.64:/opt/tomcat/webapps' // Replace with appropriate SCP command
            }
        }
    }
}
