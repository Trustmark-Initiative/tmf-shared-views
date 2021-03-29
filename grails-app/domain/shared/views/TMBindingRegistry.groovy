package shared.views

class TMBindingRegistry {

    String name
    String url
    String description

    static belongsTo = [
        organization: TMOrganization
    ]

    static constraints = {
        name nullable: false
        url nullable: false
        description nullable: true
    }

    static mapping = {
        table name: 'tm_binding_registry'
        name column: 'name'
        url column: 'url'
        description column: 'description', type: 'text'
    }

    Map toJsonMap(boolean shallow = true) {
        def json = [
                id : this.id,
                name : this.name,
                url : this.url,
                organization: this.organization,
                description: this.description
        ]
        return json
    }
}
