package shared.views

class TMOrganization {

    String name
    String displayName
    String siteUrl
    String description
    boolean trustmarkProvider

    static hasMany = [
            registrants: TMRegistrant
            , providers: TMProvider
            , assessmentRepos: TMAssessmentRepository
    ]

    static constraints = {
        name nullable: false
        siteUrl nullable: false
        displayName nullable: false
        description nullable: true
        registrants nullable: true
        providers nullable: true
        assessmentRepos nullable: true
    }

    static mapping = {
        table name: 'tm_organization'
        name column: 'name'
        siteUrl column: 'site_url'
        displayName column: 'display_name'
        trustmarkProvider column: 'trustmark_provider'
        description column: 'description', type: 'text'
    }

    Map toJsonMap(boolean shallow = true) {
        def json = [
                id: this.id,
                name: this.name,
                displayName: this.displayName,
                description: this.description,
                siteUrl: this.siteUrl,
                trustmarkProvider: this.trustmarkProvider,
        ]
        return json;
    }
}
