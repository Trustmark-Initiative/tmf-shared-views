package shared.views

class TMMailParameter {

    public static final String SMTP_ADDRESS = "smtp_address"
    public static final String SMTP_USER = "smtp_user"
    public static final String SMTP_HOST = "smtp_host"
    public static final String SMTP_PORT = "smtp_port"
    public static final String SMTP_AUTH = "smtp_authenticate"
    public static final String SMTP_PSWD = "smtp_pswd"

    public static final String MAIL_ENABLED = "mail_enabled"


    String name
    String value
    Date lastUpdated

    static String find(String name){
        String theVal = null

        withTransaction {
            TMMailParameter prop = TMMailParameter.findByName(name)
            if( prop )
                theVal = prop.value
        }
        return theVal
    }


    static void save(String name, Object value){
        withTransaction {
            TMMailParameter prop = TMMailParameter.findByName(name);
            if( !prop )
                prop = new TMMailParameter(name:name, value: value?.toString())
            else
                prop.value = value?.toString()
            prop.save(failOnError: true)
        }
    }

    static void delete(String name){
        withTransaction {
            TMMailParameter prop = findByName(name);
            if( prop )
                prop.delete()
        }
    }

    static constraints = {
        name(nullable: false, blank: false, maxSize: 254)
        value(nullable: true, blank: true, maxSize: 65535)
        lastUpdated(nullable: true)
    }

    static mapping = {
        table 'mail_parameter'
        name column: 'field_name'
        value type: 'text', column: 'field_value'
        lastUpdated column: 'last_updated'
    }

}
