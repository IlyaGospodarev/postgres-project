pipeline {
    agent any

    stages {
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
