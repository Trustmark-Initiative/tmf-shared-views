package shared.views

class TMGroup {

    String name

    static constraints = {
        name nullable: false
    }
    static mapping = {
        table name: 'tm_group'
        name column: 'name'
    }
}
