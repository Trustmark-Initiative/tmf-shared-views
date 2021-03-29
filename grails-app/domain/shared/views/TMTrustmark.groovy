package shared.views

class TMTrustmark {

    String name
    String url
    String status
    boolean provisional
    String assessorComments

    static belongsTo = [
        provider: TMProvider
    ]

    static constraints = {
        name nullable: false
        url nullable: false
        status nullable: false
        provisional nullable: false
        assessorComments nullable: true
    }

    static mapping = {
        table name: 'tm_trustmark'
    }
}
