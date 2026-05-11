pipeline {
    agent any

    tools {
        maven 'Maven 3.9.15'
        jdk 'JDK_21'
    }

    stages {
        stage('Compile Project') {
            steps {
                withMaven(maven: 'Maven 3.9.15') {
                    bat 'mvn clean compile'
                }
            }
        }

        //stage('Validate Checkstyle') {
            //steps {
                //withMaven(maven: 'Maven 3.9.15') {
                    //bat 'mvn checkstyle:check'
                //}
            //}
        //}

        stage('Validate Unit Tests') {
            steps {
                withMaven(maven: 'Maven 3.9.15') {
                    bat 'mvn test'
                }
            }
        }

        stage('Validate Test Coverage') {
            steps {
                withMaven(maven: 'Maven 3.9.15') {
                    bat 'mvn jacoco:report'
                }
            }
        }

        stage('Package Application') {
            steps {
                withMaven(maven: 'Maven 3.9.15') {
                    bat 'mvn package'
                }
            }
        }
    }
}