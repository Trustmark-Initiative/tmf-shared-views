package shared.views

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN","ROLE_ORG_ADMIN"])
class ContactController {

    ContactService contactService

    OrganizationService organizationService

    def index() { }

    def administer() {
        log.debug("administer -> ")
    }

    def manage()  {
        log.debug("manage -> ${params.orgid}")
        [organization: organizationService.get(params.orgid) ]
    }

    def add()  {
        log.debug("add -> ")

        TMOrganization org = organizationService.get(params.organizationId)

        TMContact contact = contactService.add(org
                , params.lname
                , params.fname
                , params.email
                , params.phone
                , params.type
        )

        withFormat  {
            json {
                render contact as JSON
            }
        }
    }

    def get()  {
        log.debug("get -> ")

        TMContact contact = contactService.get(params.id)

        withFormat  {
            json {
                render contact as JSON
            }
        }
    }

    def update()  {
        log.debug("update -> ")

        TMContact contact = contactService.update(params.id, params.lname, params.fname, params.email, params.phone, params.type)

        withFormat  {
            json {
                render contact as JSON
            }
        }
    }

    def list()  {
        log.debug("list ->")

        def contacts = contactService.list(params.id)

        withFormat  {
            json {
                render contacts as JSON
            }
        }
    }

    def types()  {
        log.debug("ContactController.types ->")

        def types = contactService.types()

        withFormat  {
            json {
                render types as JSON
            }
        }
    }
}
