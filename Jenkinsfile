pipeline {
    agent none
    stages {
        stage ('Clone sources') {
            agent {
                docker { image 'maven:3.8.3-openjdk-17' }
            }
            steps {
                git credentialsId: '1669edf4-c89d-4db3-bf06-01bbfb524596', 
                    branch: 'main',
                    url: 'https://github.com/melfo13/worldcupApp.git'
                sh 'java -version'
                sh 'mvn -v'
                sh 'pwd'
                sh 'mvn -U clean install'
            }
        }
        stage ('docker publish') {
            agent any
            steps {
                git credentialsId: '1669edf4-c89d-4db3-bf06-01bbfb524596', 
                    branch: 'main',
                    url: 'https://github.com/melfo13/worldcupApp.git'
                sh 'docker build -t imagebyjenkins .'
                docker.withRegistry("", "d025a1cc-0441-411e-8823-d4092171c739") {
                    def image = docker.image("${worldcupapp}");
                    image.push()
                }
            }
        }
    }
}
