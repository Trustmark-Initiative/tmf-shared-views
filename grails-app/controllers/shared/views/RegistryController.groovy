package shared.views

import grails.converters.JSON

class RegistryController {

    RegistryService registryService

    OrganizationService organizationService

    def index() { }

    def manage() {
        log.debug("manage -> ${params.orgid}")
        [organization: organizationService.get(params.orgid) ]
    }

    def add()  {
        log.info("add -> ${params.name}")

        TMBindingRegistry registry = registryService.add(params.name, params.url, params.desc, params.organizationId)
        render registry as JSON
    }

    def get()  {
        log.info("get -> ${params.id}")
        TMBindingRegistry registry = registryService.get(params.id)
        render registry as JSON
    }

    def delete()  {
        log.info("delete -> ${params.ids}")
        def ids = registryService.delete(params.ids)
        render ids as JSON
    }

    def list()  {
        log.info("list -> ${params.id}")
        render registryService.list(params.id) as JSON
    }

    def update()  {
        log.info("update -> ${params.id}")
        TMBindingRegistry registry = registryService.update(params.id, params.name, params.url, params.desc)
        render registry as JSON
    }
}
