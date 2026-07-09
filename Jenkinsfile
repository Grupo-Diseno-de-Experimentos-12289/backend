pipeline {
    agent any

    tools {
        maven 'MAVEN_3_9_15'
        jdk 'JAVA_HOME'
    }

    environment {
            REGISTRY_USER = "pierotm"
            IMAGE_NAME = "travel-match"
            TAG        = "${env.BUILD_NUMBER}"
        }

    stages {
        stage('Compile Project') {
            steps {
                withMaven(maven: 'MAVEN_3_9_15') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Validate Checkstyle') {
            steps {
                withMaven(maven: 'MAVEN_3_9_15') {
                    sh 'mvn checkstyle:check'
                }
            }
        }

        stage('Validate Unit Tests') {
            steps {
                withMaven(maven: 'MAVEN_3_9_15') {
                    sh 'mvn test'
                }
            }
        }

        stage('Validate Test Coverage') {
            steps {
                withMaven(maven: 'MAVEN_3_9_15') {
                    sh 'mvn jacoco:report'
                }
            }
        }

        stage('Package Application') {
            steps {
                withMaven(maven: 'MAVEN_3_9_15') {
                    sh 'mvn package'
                }
            }
        }

        stage('SonarQube Analysis'){
            steps{
                withSonarQubeEnv('TravelSonarServer') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectkey=travelMatch -Dsonar.javascript.exclusions=**/*'
                }

                script {
                    timeout(time:10, unit: 'MINUTES'){
                        def qg = waitForQualityGate()

                        if(qg.status != 'OK'){
                            error "El pipeline se ha detenido porque no se superó el Quality Gate. Estado: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('Construir y Publicar Imagen Docker') {
                    steps {
                        withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                            script {
                                echo "Iniciando sesión en Docker Hub..."
                                sh "echo '${DOCKER_PASS}' | docker login -u '${DOCKER_USER}' --password-stdin"

                                echo "Construyendo imagen optimizada AMD64..."
                                sh "docker buildx build --platform linux/amd64 -t ${REGISTRY_USER}/${IMAGE_NAME}:${TAG} -t ${REGISTRY_USER}/${IMAGE_NAME}:latest --push ."
                            }
                        }
                    }
        }
    }
}