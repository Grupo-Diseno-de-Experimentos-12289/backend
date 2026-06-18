pipeline {
    agent any

    tools {
        maven 'MAVEN_3_9_15'
        jdk 'JAVA_HOME'
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
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectkey=TravelMatch'
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

        stage('Construir Imagen Docker') {
                    steps {
                        script {
                            echo "Iniciando la construcción de la imagen de Docker: ${IMAGE_NAME}:${TAG}"

        					echo "Construyendo imagen híbrida/compatible con servidores de producción (AMD64)..."
        					sh "docker buildx build --platform linux/amd64 -t ${IMAGE_NAME}:${TAG} --load ."
        					sh "docker buildx build --platform linux/amd64 -t ${IMAGE_NAME}:latest --load ."

                            echo "Imagen construida exitosamente."
                        }
                    }
                }
    }
}