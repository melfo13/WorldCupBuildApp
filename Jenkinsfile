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
    }
}
