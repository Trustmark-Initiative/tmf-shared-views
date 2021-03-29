package shared.views

import grails.gorm.transactions.Transactional

@Transactional
class EndpointService {

    /**
     * add an endpoint under a provider
     * @param args
     * @return
     */
    def add(String... args) {
        log.debug("add -> ${args[0]} ${args[1]} ${args[2]} ${args[3]}")

        TMEndpoint endpoint = null
        try  {
            TMProvider provider = TMProvider.get(Integer.parseInt(args[3]))

            endpoint = new TMEndpoint(name: args[0]
                    , url: args[1]
                    , binding: args[2]
                    , provider: provider)

            endpoint.save(true)

            provider.endpoints.add(endpoint)

            provider.save(true)

        }  catch (NumberFormatException nfe)  {
            log.error("Bad provider id ${args[3]}")
        }
        return endpoint
    }

    def get(String... args) {
        log.debug("get -> ${args[0]}")

        TMEndpoint endpoint = null
        try  {
            endpoint = TMEndpoint.get(Integer.parseInt(args[0]))
        } catch (NumberFormatException nfe)  {
            endpoint = TMEndpoint.findByUrl(args[0])
        }
        return endpoint
    }

    def update(String... args) {
        log.debug("update -> ${args[0]}")

        TMEndpoint endpoint = TMEndpoint.get(args[0])
        endpoint.save(true)
        return endpoint
    }

    def delete(String... args) {
        log.debug("delete -> ${args[0]} ${args[1]}")

        List<String> ids = args[0].split(":")

        try  {
            ids.forEach({s ->
                if(s.length() > 0)  {
                    TMEndpoint endpoint = TMEndpoint.get(Integer.parseInt(s))
                    endpoint.delete()
                }
            })
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Endpoint Id!")
        }
    }

    def list(String... args) {
        log.debug("list -> ${args[0]}")

        def endpoints = []

        try  {
            TMOrganization organization = TMOrganization.get(Integer.parseInt(args[0]))

            if(organization != null)  {
                if(organization.id == 1)  {  // trustmark org, show all
                    TMEndpoint.findAll().forEach({e -> endpoints.add(e.toJsonMap())})
                } else {
                    List<TMProvider> providers = TMProvider.findAllByOrganization(organization)
                    providers.forEach({p ->
                        TMEndpoint.findAllByProvider(p).forEach({e -> endpoints.add(e.toJsonMap())})
                    })
                }
            }
        } catch (NumberFormatException nfe)  {
            TMEndpoint.findAll().forEach({e -> endpoints.add(e.toJsonMap())})
        }
        return endpoints
    }
}
