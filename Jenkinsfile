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
                    docker.withRegistry('hub.docker.com/r/marik561/flask_ngnix', DOCKER_HUB_CREDENTIALS) {
                    docker.image('myflaskapp').push('latest')
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
                    docker.build('mynginxapp', '.')
                    docker.withRegistry('hub.docker.com/r/marik561/flask_ngnix', DOCKER_HUB_CREDENTIALS) {
                    docker.image('mynginxapp').push('latest')
                    }
                }
            }
        }
        stage('Run Docker Containers and Test Communication') {
            steps {
                script {
                    docker.image('myflaskapp').run('-p 5000:5000', 'myflaskapp')
                    docker.image('mynginxapp').run('-p 80:80', 'mynginxapp')
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
                 docker.image('myflaskapp').remove(force: true)
                 docker.image('mynginxapp').remove(force: true)
                 
            }
        }
    }
}
