pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIALS')
    }

    stages {
        stage('Build and Push Flask Docker Image') {
            steps {
                script {
                    docker.build('myflaskapp', '-f myflaskapp .')
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        sh "docker tag myflaskapp:latest marik561/flask_ngnix:latest"
                        sh "docker push marik561/flask_ngnix:latest"
                    }
                }
            }
        }
        stage('Modify, Build, and Push Nginx Docker Image') {
            steps {
                script {
                    sh 'echo "ENTRYPOINT proxy_pass http://myflaskapp:5000/;" >> /etc/nginx/nginx.conf && \
                        echo "add_header X-Forwarded-For $remote_addr;" >> /etc/nginx/nginx.conf'
                    sh 'echo "ENTRYPOINT proxy_pass http://myflaskapp:5000/;" >> /etc/nginx/Dockerfile '//  && \ echo "add_header X-Forwarded-For $remote_addr;" >> /etc/nginx/Dockerfile'        
                    docker.build('mynginxapp', '.')
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        sh "docker tag mynginxapp:latest marik561/nginx:latest"
                        sh "docker push marik561/nginx:latest"
                    }
                }
            }
        }
        stage('Run Docker Containers and Test Communication') {
            steps {
                script {
                    docker.image('myflaskapp:latest').run('-p 5000:5000', 'myflaskapp')
                    docker.image('mynginxapp:latest').run('-p 80:80', 'mynginxapp')
                    sleep 60 // Wait for containers to start
                    sh 'curl http://localhost/' // Perform request to Nginx
                }
            }
        }
    }

    post {
        success {
            echo 'All stages completed successfully!'
        }
        failure {
            echo 'One or more stages failed!'
        }
        always {
            script {
                docker.image('myflaskapp:latest').remove(force: true)
                docker.image('mynginxapp:latest').remove(force: true)
            }
        }
    }
}
