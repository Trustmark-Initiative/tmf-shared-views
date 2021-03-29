package shared.views

class TMAssessmentRepository {

    String repoUrl

    static belongsTo = [
        organization: TMOrganization
    ]

    static hasMany = [
        trustmark: TMTrustmark
    ]

    static constraints = {
        repoUrl nullable: false
        trustmark nullable: true
    }

    static mapping = {
        table name: 'tm_assessment_repo'
        repoUrl column: 'repo_url'
    }
}
