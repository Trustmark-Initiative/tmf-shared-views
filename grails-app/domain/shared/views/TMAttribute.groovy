package shared.views

class TMAttribute {

    String name
    String value

    static constraints = {
        name nullable: false
        value nullable: false
    }

    static mapping = {
        table name: 'tm_attribute'
        name column: 'name'
        value type: 'text'
    }
}
