package shared.views


class TMRegistrant {

    TMContact      contact
    boolean      active

    static belongsTo = [
        organization: TMOrganization
    ]

    static constraints = {
        contact nullable: false
    }

    static mapping = {
        table name: 'tm_registrant'
        contact column: 'contact_ref', fetch: 'join'
        active column: 'active'
    }

    Map toJsonMap(boolean shallow = true) {
        def json = [
                id: this.id,
                contact: this.contact?.toJsonMap(true),
                organization: this.organization?.toJsonMap(true),
                active: this.active
        ]
        return json
    }
}
