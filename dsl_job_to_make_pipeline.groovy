// Import necessary classes
import javaposse.jobdsl.dsl.*;
//credentials('my-git-credentials-id') needed to be next to the url
// Define the DSL script
def jobDslScript = '''
pipelineJob('MyPipelineJob') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/Gmarik561/flask-ngnix')
                        
                    }
                    branch('main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
    triggers {
        cron('H */4 * * 1-5') // Run every 4 hours on weekdays
    }
}
'''

// Create the Job DSL script
def jobDsl = new GroovyShell().parse(jobDslScript)

// Execute the Job DSL script
jobDsl.run()

// Save the updated configuration
jobDsl.save()
