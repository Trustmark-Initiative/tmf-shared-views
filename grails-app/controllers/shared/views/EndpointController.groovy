package shared.views

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN","ROLE_ORG_ADMIN", "ROLE_USER"])
class EndpointController {

    EndpointService endpointService

    OrganizationService organizationService

    def index() {
        log.debug("EndPointController.index -> ")
    }

    def administer() {
        log.debug("EndPointController.administer -> ")
    }

    def manage() {
        log.debug("EndPointController.manage ->  ${params.orgid}")
        [organization: organizationService.get(params.orgid) ]
    }

    /**
     * list of endpoints related to a specific organization
     * @return
     */
    def list()  {
        def endpoints = endpointService.list(params.id)

        withFormat {
            json {
                render endpoints as JSON
            }
        }
    }

    /**
     * get a specific endpoint based on id
     * @return
     */
    def get()  {
        log.debug("EndPointController.get -> ")

        TMEndpoint endpoint = endpointService.get(params.id)
        withFormat {
            json {
                render endpoint.toJsonMap() as JSON
            }
        }
    }

    /**
     * takes a string of ids and removes each of them
     * @return
     */
    def delete()  {
        log.debug("EndPointController.delete -> ")

        TMOrganization organization = endpointService.delete(params.ids, params.pid)
        withFormat {
            json {
                render organization.toJsonMap() as JSON
            }
        }
    }

    /**
     * add an endpoint
     * @return
     */
    def add()  {
        log.debug("EndPointController.add -> ")

        TMEndpoint endpoint = endpointService.add(params.name
                                                , params.endptUrl
                                                , params.postBindingUrl
                                                , params.redirectBindingUrl
                                                , params.paosBindingUrl
                                                , params.types
                                                )
        withFormat {
            json {
                render endpoint.toJsonMap() as JSON
            }
        }
    }

    /**
     * update an endpoint
     * @return
     */
    def update()  {
        log.debug("EndPointController.update -> ")

        TMEndpoint endpoint = endpointService.update(params.name
                                                , params.endptUrl
                                                , params.postBindingUrl
                                                , params.redirectBindingUrl
                                                , params.paosBindingUrl
                                                , params.types
                                                )
        withFormat {
            json {
                render endpoint.toJsonMap() as JSON
            }
        }
    }

    /**
     * list all endpoint types
     * @return
     */
    def etypes()  {
        log.debug("EndPointController.etypes -> ")

        List<EndpointType> types = EndpointType.values().toList()
        withFormat {
            json {
                render types as JSON
            }
        }
    }
}
