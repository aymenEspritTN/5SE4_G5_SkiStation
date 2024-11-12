pipeline {
    agent any

    environment {
        GIT_REPO = "https://github.com/aymenEspritTN/5SE4_G5_SkiStation"
        BRANCH = "KhiariAymen_5SE4_G5"
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
                script {
                    // Run tests (make sure test class is in the correct directory)
                    sh 'mvn test -Dtest=SkierServicesTest'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
    }
}
