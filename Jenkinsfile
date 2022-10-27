pipeline {
    agent {
        docker { image 'maven:3.8.3-openjdk-17' }
    }
    stages {
        stage ('Test') {
            steps {
                sh 'java -version'
                sh 'mvn -v'
            }
        }
        stage ('Clone sources') {
            steps {
                git url: 'https://github.com/melfo13/worldcupApp.git'
            }
        }
        stage ('Maven Build') {
            steps {
                sh 'mvn -U clean install'
            }
        }
    }
}
