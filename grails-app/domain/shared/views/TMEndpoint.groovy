package shared.views

class TMEndpoint {

    final String SERVICE_NAME = "ServiceName"

    String       name
    String       url
    String       binding
    EndpointType type
    boolean      published = false

    static belongsTo = [
            provider: TMProvider
    ]

    static hasMany = [
        attributes: TMAttribute
    ]

    static constraints = {
        name nullable: false
        url nullable: true
        binding nullable: true
        attributes nullable: true
        type nullable: true
    }

    static mapping = {
        table name: 'tm_endpoint'
    }

    def getServiceName()  {
        return findAttribute(SERVICE_NAME)
    }

    def findAttribute(String name)  {
        String value = ""
        if(attributes != null)  {
            attributes.forEach({ a ->
                if(a.name == name)
                    value += a.value
            })
        }
        return value
    }

    Map toJsonMap(boolean shallow = true) {
        def json = [
               id : this.id,
               name : this.name,
               url : this.url,
               binding : this.binding,
               attributes : this.attributes,
               type : this.type,
               serviceName: this.getServiceName(),
               published : this.published
        ]
        return json
    }
}
