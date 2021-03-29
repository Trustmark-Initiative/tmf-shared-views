package shared.views

import grails.converters.JSON

class RegistrantController {

    RegistrantService registrantService

    OrganizationService organizationService

    def index() { }

    def insert() { }

    def administer() { }

    def manage() {
        log.debug("manage user ->  ${params.orgid}")
        [organization: organizationService.get(params.orgid) ]
    }

    def activate()  {
        log.debug("user -> ")

        TMRegistrant registrant = registrantService.activate(params.ids)

        withFormat  {
            json {
                render registrant.toJsonMap() as JSON
            }
        }
    }

    def deactivate()  {
        log.debug("user -> ")

        TMRegistrant registrant = registrantService.deactivate(params.ids)

        withFormat  {
            json {
                render registrant.toJsonMap() as JSON
            }
        }
    }

    def pswd()  {
        log.info("pswd -> ${params.id}")

        TMRegistrant registrant = registrantService.pswd(params.id
                , params.pswd0
                , params.pswd1
                , params.pswd2
        )

        withFormat  {
            json {
                render registrant.toJsonMap(false) as JSON
            }
        }
    }

    def add()  {
        log.debug("Registrant add -> ")

        def registrants = []
        TMRegistrant registrant = registrantService.add(params.lname
                , params.fname
                , params.email
                , params.phone
                , params.pswd
                , params.organizationId
        )

        registrants.add(registrant.toJsonMap())
        withFormat  {
            json {
                render registrants as JSON
            }
        }
    }

    def get()  {
        log.debug("Registrant get -> ")

        TMRegistrant registrant = registrantService.get(params.id)

        withFormat  {
            json {
                render registrant.toJsonMap(false) as JSON
            }
        }
    }

    def delete()  {
        log.debug("registrant delete -> ")

        TMRegistrant registrant = registrantService.delete(params.ids)

        withFormat  {
            json {
                render registrant.toJsonMap() as JSON
            }
        }
    }

    def update()  {
        log.debug("Registrant update -> ")

        def registrants = []

        TMRegistrant registrant = registrantService.update(params.id
                , params.lname
                , params.fname
                , params.email
                , params.phone
                , params.tatUrl
                , params.recipientId
                , params.organizationId
                )
        registrants.add(registrant.toJsonMap())
        withFormat  {
            json {
                render registrants as JSON
            }
        }
    }

    def list()  {
        log.debug("Registrant list -> ")

        def registrants = registrantService.list(params.id)

        withFormat  {
            json {
                render registrants as JSON
            }
        }
    }
}
