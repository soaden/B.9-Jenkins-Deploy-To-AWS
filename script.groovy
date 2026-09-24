def incrementVersion() {
    echo 'incrementing app version...'
    sh 'mvn build-helper:parse-version versions:set \
                                -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                                versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}

def buildJar() {
    echo 'building the application...'
    sh 'mvn clean package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {

        //sh 'docker build -t soaden/bootcamp-m9:2.0 .'
        sh "docker buildx build --platform linux/arm64 -t soaden/bootcamp-m9:${IMAGE_NAME} ."

        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push soaden/bootcamp-m9:${IMAGE_NAME}"
    }
}

def deployApp() {
    echo 'deploying the application to AWS...'
    def dockerCmd = "docker run -p 8080:8080 -d soaden/bootcamp-m9:${IMAGE_NAME}"
    def targetIP = '63.179.53.73'
    sshagent(['aws-webserver-ssh']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@${targetIP} ${dockerCmd}"
    }
}

def pushVersionUpdate() {
    withCredentials([usernamePassword(credentialsId: 'credentials-github', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "git remote set-url origin https://${USER}:${PASS}@github.com/soaden/B.9-Jenkins-Deploy-To-AWS.git"
        sh 'git add .'
        sh 'git commit -m "ci: version bump"'
        sh 'git push origin HEAD:feature/increment-build-version'
    }
}

return this
