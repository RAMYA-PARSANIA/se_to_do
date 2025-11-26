pipeline {
    agent any
    
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE_NAME = 'ramyaparsania/task-manager'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Pull Code from GitHub') {
            steps {
                echo 'Pulling code from GitHub...'
                git branch: 'main',
                    url: 'https://github.com/RAMYA-PARSANIA/se_to_do.git'
            }
        }
        
        stage('Build the Code') {
            steps {
                echo 'Building the application...'
                script {
                    // Build Java application using Maven daemon
                    bat '''
                        "D:\\Downloads\\maven-mvnd-1.0.3-windows-amd64\\maven-mvnd-1.0.3-windows-amd64\\bin\\mvnd.cmd" clean compile
                    '''
                }
            }
        }
        
        stage('Test the Code') {
            steps {
                echo 'Running tests...'
                script {
                    bat '"D:\\Downloads\\maven-mvnd-1.0.3-windows-amd64\\maven-mvnd-1.0.3-windows-amd64\\bin\\mvnd.cmd" test'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging the application...'
                script {
                    bat '"D:\\Downloads\\maven-mvnd-1.0.3-windows-amd64\\maven-mvnd-1.0.3-windows-amd64\\bin\\mvnd.cmd" package'
                }
            }
        }
        
        stage('Create Docker Image') {
            when {
                expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo 'Building Docker image...'
                script {
                    bat """
                        docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                        docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest
                    """
                }
            }
        }
        
        stage('Push to Docker Hub') {
            when {
                expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                script {
                    bat """
                        echo %DOCKER_HUB_CREDENTIALS_PSW% | docker login -u %DOCKER_HUB_CREDENTIALS_USR% --password-stdin
                        docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                        docker push ${DOCKER_IMAGE_NAME}:latest
                        docker logout
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            bat 'docker images'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            echo 'Cleaning up...'
            bat 'docker system prune -f || exit 0'
        }
    }
}
