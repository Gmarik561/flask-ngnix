pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIALS')
    }

    stages {
        stage('Build and Push Flask Docker Image') {
            steps {
                script {
                    sudo docker.build('myflaskapp')
                    sudo docker.withRegistry('https://hub.docker.com/r/marik561/flask_ngnix', DOCKER_HUB_CREDENTIALS) {
                        sudo docker.image('myflaskapp').push('latest')
                    }
                }
            }
        }
        stage('Modify, Build, and Push Nginx Docker Image') {
            steps {
                script {
                    sh 'cp /path/to/default/nginx/Dockerfile .'
                    sh 'echo "proxy_pass http://myflaskapp:5000/;" >> Dockerfile'
                    sh 'echo "add_header X-Forwarded-For $remote_addr;" >> Dockerfile'
                    sudo docker.build('mynginxapp', '.')
                    sudo docker.withRegistry('https://hub.docker.com/r/marik561/flask_ngnix', DOCKER_HUB_CREDENTIALS) {
                        sudo docker.image('mynginxapp').push('latest')
                    }
                }
            }
        }
        stage('Run Docker Containers and Test Communication') {
            steps {
                script {
                    sudo docker.image('myflaskapp').run('-p 5000:5000', 'myflaskapp')
                    sudo docker.image('mynginxapp').run('-p 80:80', 'mynginxapp')
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
                sudo docker.image('myflaskapp').remove(force: true)
                sudo docker.image('mynginxapp').remove(force: true)
            }
        }
    }
}
