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
                    docker.withRegistry([credentialsId: 'DOCKER_HUB_CREDENTIALS', url: 'https://hub.docker.com/r/marik561/flask_ngnix']) {
                    
                    docker.image('myflaskapp').push('latest')
                    }//docker.withRegistry('https://hub.docker.com/r/marik561/flask_ngnix', 'marik561','!Marik5678152')
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
                    docker.withRegistry([credentialsId: 'DOCKER_HUB_CREDENTIALS', url: 'https://hub.docker.com/r/marik561/flask_ngnix']) {
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
                docker.image('myflaskapp:latest').remove(force: true)
                docker.image('mynginxapp:latest').remove(force: true)
            }
        }
    }
}
