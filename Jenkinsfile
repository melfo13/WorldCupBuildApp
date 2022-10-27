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
                git credentialsId: '1669edf4-c89d-4db3-bf06-01bbfb524596', 
                    branch: 'main',
                    url: 'https://github.com/melfo13/worldcupApp.git'
                sh 'pwd'
                sh 'mvn -U clean install'
            }
        }
    }
}
