package shared.views

import grails.converters.JSON

import java.nio.charset.StandardCharsets

class OrganizationController {

    def springSecurityService

    OrganizationService organizationService

    def index() { }

    def administer()  {

    }

    def manage()  {
        log.debug("manage user -> ${params.orgid}")
        [organization: organizationService.get(params.orgid) ]
    }

    def view() {
        log.debug(params.id)
        TMOrganization organization = organizationService.get(params.id)
        if(params.filename != null)  {
            byte[] buffer = new byte[params.filename.size]
            params.filename.getInputStream().read(buffer)
            String xmlString = new String(buffer, StandardCharsets.UTF_8)
            log.debug("File Name: ${params.filename.originalFilename}  ${params.filename.size}")
            deserializeService.deserialize(xmlString, organization)
        }
        [organization: organization]
    }

    def add()  {
        log.debug("add -> ${params.name}")

        TMOrganization organization = organizationService.add(params.name
                , params.displayName
                , params.siteUrl
                , params.description
        )

        withFormat  {
            json {
                render organization as JSON
            }
        }
    }

    def delete()  {
        log.debug("get -> ${params.ids}")

        TMOrganization organization = organizationService.delete(params.ids)

        withFormat  {
            json {
                render organization as JSON
            }
        }
    }

    def get()  {
        log.debug("get -> ${params.id}")

        TMOrganization organization = organizationService.get(params.id)

        withFormat  {
            json {
                render organization as JSON
            }
        }
    }

    def update()  {
        log.debug("update -> ${params.name}")

        TMOrganization organization = organizationService.update(params.id, params.url, params.desc, params.display)

        withFormat  {
            json {
                render organization as JSON
            }
        }
    }

    def list()  {
        log.debug("list -> ${params.name}")

        def organizations = organizationService.list(params.name)

        withFormat  {
            json {
                render organizations as JSON
            }
        }
    }
}
