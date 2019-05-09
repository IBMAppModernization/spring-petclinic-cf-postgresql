// Basic Pipeline

pipeline {
    environment {
         RESOURCE_GROUP = "default"
         REGION = "us-south"
         API_ENDPOINT= "https://cloud.ibm.com"
    }

    tools {
        maven 'Apache Maven 3.5.2'
        jdk 'Open JDK 8'
    }

    agent any

    stages {
         stage ('Initialize') {
            steps {
                sh '''
                  #!/bin/bash
                 echo "APP_NAME = ${APP_NAME}"
                 echo "DB_SERVICE_NAME = ${DB_SERVICE_NAME}"
                 echo "ORGANIZATION = ${ORGANIZATION}"
                 echo "SPACE = ${SPACE}"
                 echo "DOMAIN = ${DOMAIN}"
                 oldroutes=djc-spring-petclinic-timely-reedbuck.mybluemix.net
                 for i in \${oldroutes\/\/,\/ }
                 do

                    echo \$i
                 done
                '''
            }
         }

        stage('Build application jar file') {
          steps {
              checkout scm
              sh 'mvn clean package'
          }
        }

       stage('Deploy to CF with new name and new route ') {
          steps {

             sh """
             #!/bin/bash
             ibmcloud login -a ${env.API_ENDPOINT} --apikey ${API_KEY} -r ${env.REGION} -g ${env.RESOURCE_GROUP} -o ${ORGANIZATION} -s ${SPACE}
             route=\$(ibmcloud cf app ${APP_NAME} | grep "routes:" | cut -d ':' -f 2 | xargs | cut -d ',' -f 1)
             host=\$(echo \${route%.$DOMAIN})
             ibmcloud cf push ${APP_NAME}-snapshot-${env.BUILD_NUMBER} -f manifest-pipeline.yml --hostname \${host}-snapshot-${env.BUILD_NUMBER} --no-start
             ibmcloud cf bind-service ${APP_NAME}-snapshot-${env.BUILD_NUMBER} ${DB_SERVICE_NAME}
             ibmcloud cf start ${APP_NAME}-snapshot-${env.BUILD_NUMBER}
             """

           }
       }

       stage('System integration test') {
         steps {
             sh 'echo "System integration test of new app + new route would go here"'
         }
       }

        stage('Blue/Green deploy') {
            steps {
                echo 'Blue/green deployment....'

                sh """
                #!/bin/bash
                newroute=\$(ibmcloud cf app ${APP_NAME}-snapshot-${env.BUILD_NUMBER}  | grep "routes:" | cut -d ':' -f 2 | xargs | cut -d ',' -f 1)
                newhost=\$(echo \${newroute%.$DOMAIN})
                oldroutes=\$(ibmcloud cf app ${APP_NAME} | grep "routes:" | cut -d ':' -f 2 | xargs)

                # Map all routes from previous version to new version
                for i in \${oldroutes//,/ }
                do
                   host=\$(echo \${i%.$DOMAIN})
                   ibmcloud cf map-route ${APP_NAME}-snapshot-${env.BUILD_NUMBER} ${DOMAIN} -n \${host}
                   sleep 1
                   ibmcloud cf unmap-route ${APP_NAME} ${DOMAIN} -n \${host}
                done
                # Unmap temporary route from new version
                ibmcloud cf unmap-route ${APP_NAME}-snapshot-${env.BUILD_NUMBER} ${DOMAIN} -n \${newhost}

                # Delete previous version
                ibmcloud cf delete ${APP_NAME} -f

                # Rename new version
                ibmcloud cf rename ${APP_NAME}-snapshot-${env.BUILD_NUMBER} ${APP_NAME}
                """


            }
        }
    }
}
