def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {

        //sh 'docker build -t soaden/bootcamp-m9:2.0 .'
        sh 'docker buildx build --platform linux/arm64 -t soaden/bootcamp-m9:2.0 .'

        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push soaden/bootcamp-m9:2.0'
    }
}

def deployApp() {
    echo 'deploying the application to AWS...'
    def dockerComposeCmd = 'docker compose -f docker-compose.yaml up --detach'
    def targetIP = '63.179.53.73'
    sshagent(['aws-webserver-ssh']) {
        sh "scp docker-compose.yaml ec2-user@${targetIP}:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@${targetIP} ${dockerComposeCmd}"
    }
}

return this
