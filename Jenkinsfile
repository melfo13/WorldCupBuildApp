pipeline {
    agent none
    environment{
        env.VERSION = "latest"
    }
    stages {
        stage ('deploy into ECS') {
            agent {
                docker { image 'melfo2310/awscli3:latest' }
            }
            environment {
                AWS_REGION='us-east-1'
                AWS_DEFAULT_REGION='us-east-1'
            }
            steps{
                script {
                    sh "export AWS_REGION='us-east-1'"
                    withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId:'my.aws.credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' )]){
                        def rootDir = pwd()
                        def exampleModule = load "${rootDir}@libs/functions.groovy "
                        sh """aws ecr describe-images --repository-name imagebyjenkins | grep -Eo '(\\d+\\.\\d+\\.)+\\d+' >> tags.txt"""
                        env.VERSION = exampleModule.getNextVersion("tags.txt")
                        sh "rm -rf tags.txt"
                    }
                }
               sh 'aws --version'
            }
        }
        stage ('Clone sources & build') {
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
                stash includes: '**/taskdefinition.json', name: 'taskdef'
                sh 'mvn -U clean install'
            }
        }
        stage ('docker publish') {
            agent any
            environment {
                dockerhub=credentials('d025a1cc-0441-411e-8823-d4092171c739') 
            }
            steps {
                git credentialsId: '1669edf4-c89d-4db3-bf06-01bbfb524596', 
                    branch: 'main',
                    url: 'https://github.com/melfo13/worldcupApp.git'
//                 sh 'docker build -t melfo2310/imagebyjenkins:1.0.6 .'
//                 sh 'docker login -u $dockerhub_USR -p $dockerhub_PSW'
//                 sh 'docker push melfo2310/imagebyjenkins:latest'
                unstash 'taskdef'
                sh 'cat taskdefinition.json'
                script {
                    docker.withRegistry('https://541973109241.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:my.aws.credentials') {
                        def customImage = docker.build("imagebyjenkins:${env.VERSION}")
                        customImage.push()
                    }
                }
            }
        }
        stage ('deploy into ECS') {
            agent {
                docker { image 'melfo2310/awscli3:latest' }
            }
            environment {
                AWS_REGION='us-east-1'
                AWS_DEFAULT_REGION='us-east-1'
            }
            steps{
                script {
                    sh "export AWS_REGION='us-east-1'"
                    unstash 'taskdef'
                    withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId:'my.aws.credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' )]){
                        sh "sed -i 's/VERSION/${env.VERSION}/g' taskdefinition.json"
                        sh 'aws ecs register-task-definition --family worldCupApp --cli-input-json file://taskdefinition.json >> tdef.txt && grep -hnr "revision" tdef.txt >> out.txt && grep -Eo "[0-9]{2}" out.txt | tail -1 >> version.txt'
                        env.LS = sh(script:'tail -1 version.txt', returnStdout: true).trim()
                        sh 'rm -rf tdef.txt out.txt version.txt '
                        sh "aws ecs update-service --service worldCupApp-service --cluster worldCupApp --task-definition worldCupApp:${env.LS}"
//                         sh 'aws ecs run-task --task-definition arn:aws:ecs:us-east-1:541973109241:task-definition/worldCupApp:1 --cluster arn:aws:ecs:us-east-1:541973109241:cluster/worldCup-cluster'
                    }
                }
               sh 'aws --version'
            }
        }
    }
}
