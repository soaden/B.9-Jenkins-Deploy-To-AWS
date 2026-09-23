def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t soaden/demo-repo-1:jma-2.0 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push soaden/demo-repo-1:jma-2.0'
    }
}

def deployApp() {
    echo 'deploying the application to AWS...'
    def dockerCmd = 'docker run -p 3080:3080 -d soaden/bootcamp-m9:1.0'
    def targetIP = '63.179.53.73'
    sshagent(['aws-webserver-ssh']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@${targetIP} ${dockerCmd}"
    }
}

return this
