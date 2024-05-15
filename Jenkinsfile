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
                        sh "docker tag myflaskapp:latest marik561/flask_ngnix:latest_myflaskapp"
                        sh "docker push marik561/flask_ngnix:latest_myflaskapp"
                    }
                }
            }
        }
        stage('Modify, Build, and Push Nginx Docker Image') {
            steps {
                script {
                   // Append the proxy_pass directive to nginx.conf
                    sh 'echo "proxy_pass http://myflaskapp:5000/;" >> nginx.conf'

                    // Append the add_header directive to nginx.conf
                    sh 'echo "add_header X-Forwarded-For \$remote_addr;" >> nginx.conf'     
                    docker.build('mynginxapp', '.')
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        sh "docker tag mynginxapp:latest marik561/nginx:latest_NGINX"
                        sh "docker push marik561/nginx:latest_NGINX"
                    }
                }
            }
        }
        stage('Run Docker Containers and Test Communication') {
            steps {
                script {
                    
                    //sh "docker network create mynetwork"
                    sh "docker run -d -p 5000:5000 --network mynetwork --name myflaskapp marik561/flask_ngnix:latest_myflaskapp python app.py"
                    
                    sh "docker run -d -p 80:80 --network mynetwork --name mynginxapp marik561/nginx:latest_NGINX"
                   

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
                sh "docker rmi marik561/flask_ngnix:latest_myflaskapp"
                sh "docker rmi marik561/nginx:latest_NGINX"
             
            }
        }
    }
}
