pipeline {
    agent any

    environment {
        GIT_REPO = "https://github.com/aymenEspritTN/5SE4_G5_SkiStation"
        BRANCH = "KhiariAymen_5SE4_G5"
    }
    tools {
        jdk 'jdk17'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                echo "getting project from git";
                git branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Maven Clean') {
            steps {
                echo "Cleaning the project...";
                script {
                    sh 'mvn clean'
                }
            }
        }

        stage('Maven Compile') {
            steps {
                echo "Compiling the project...";
                script {
                    sh 'mvn compile'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "running SonarQube to check code quality...";
                withSonarQubeEnv(installationName: 'sq1') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Mockito Tests') {
            steps {
                echo "running tests...";
                script {
                    sh 'mvn test -Dtest=SkierServicesTest'
                }
            }
        }

        stage('Nexus deploy') {
            steps {
                echo "Deploying to Nexus...";
                script {
                    sh 'mvn deploy -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image...";
                script {
                    sh 'docker build -t KhiariAymen_5SE4_G5_SkiStation:1.0.0 .'
                }
            }
        }
        
        stage('Docker Compose Up') {
            steps {
                echo "Starting containers using Docker Compose..."
                script {
                    sh 'docker-compose up -d' // Runs in detached mode
                }
            }
        }

    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        failure {
            echo 'Pipeline failed. Check the logs for details.'
        }
    }
}
