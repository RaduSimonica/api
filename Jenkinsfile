pipeline {
    agent any

    stages {
        stage('TODO: Add tests!!!') {
            steps {
                script {
                    sh "echo Still no tests?"
                }
            }
        }
        stage('Build jar') {
            steps {
                script {
                    sh "chmod +x gradlew"
                    sh "./gradlew build --no-tests"
                }
            }
        }
        stage('Build Docker image') {
            steps {
                script {
                    def imageTag = "${env.BRANCH_NAME}".toString() == "master" ? "$BUILD_NUMBER" : "SNAPSHOT_$BUILD_NUMBER"
                    def latestTag = "${env.BRANCH_NAME}".toString() == "master" ? "latest" : "SNAPSHOT_latest"
                    sh "docker build -t radusimonica/api:$imageTag ."
                    sh "docker build -t radusimonica/api:$latestTag ."
                }
            }
        }
        stage('Publish to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh "docker login -u $USERNAME -p $PASSWORD"

                        def imageTag = "${env.BRANCH_NAME}".toString() == "master" ? "$BUILD_NUMBER" : "SNAPSHOT_$BUILD_NUMBER"
                        def latestTag = "${env.BRANCH_NAME}".toString() == "master" ? "latest" : "SNAPSHOT_latest"
                        sh "docker push radusimonica/api:$imageTag"
                        sh "docker push radusimonica/api:$latestTag"
                    }
                }
            }
        }
    }
}