package shared.views

class TMContact {

    String      firstName
    String      lastName
    String      email
    String      phone
    ContactType type

    static belongsTo = [
            organization: TMOrganization
    ]

    static constraints = {
        lastName nullable: false
        firstName nullable: false
        email nullable: false
        phone nullable: true
        type nullable: true
    }

    static mapping = {
        table name: 'tm_contact'
        firstName column: 'first_name'
        lastName column: 'last_name'
        email column: 'email'
        phone column: 'phone'
        type column: 'type'
    }

    Map toJsonMap(boolean shallow = true) {
        def json = [
                id: this.id,
                lastName: this.lastName,
                firstName: this.firstName,
                type: this.type,
                email: this.email,
                organization: this.organization,
                phone: this.phone
        ]
        return json
    }//end toJsonMap
}
