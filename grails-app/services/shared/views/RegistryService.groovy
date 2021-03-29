package shared.views

import grails.gorm.transactions.Transactional

@Transactional
class RegistryService {

    def add(String... args)  {
        log.info("add -> ${args[0]}")
        TMOrganization organization = TMOrganization.get(Integer.parseInt(args[3]))

        TMBindingRegistry registry = new TMBindingRegistry (
                name: args[0]
                , url: args[1]
                , description: args[2]
                , organization: organization
        )
        registry.save(true)
        return registry
    }

    def get(String... args)  {
        log.info("get ->  ${args[0]}")
        return TMBindingRegistry.get(args[0])
    }

    def delete(String... args)  {
        log.info("delete ->  ${args[0]}")
        List<String> ids = args[0].split(":")
            try  {
                ids.forEach({s ->
                    if(s.length() > 0)  {
                        TMBindingRegistry registry = TMBindingRegistry.get(Integer.parseInt(s))
                        registry.delete()
                    }
                })
            } catch (NumberFormatException nfe)  {
                log.error("Invalid Attribute Id!")
            }
        return ids
    }

    def list(String... args)  {
        log.info("list ->  ${args[0]}")

        TMOrganization organization = TMOrganization.get(Integer.parseInt(args[0]))

        def registries = []
        if(organization.id == 1)  {
            TMBindingRegistry.findAll().forEach({r ->
                registries.add(r.toJsonMap())
            })
        } else {
            TMBindingRegistry.findAllByOrganization(organization).forEach({r ->
                registries.add(r.toJsonMap())
            })
        }
        return registries
    }

    def update(String... args)  {
        log.info("update ->  ${args[0]}")

        TMBindingRegistry registry = TMBindingRegistry.get(args[0])

        registry.name = args[1]
        registry.url = args[2]
        registry.description = args[3]

        registry.save(true)

        return registry
    }
}
