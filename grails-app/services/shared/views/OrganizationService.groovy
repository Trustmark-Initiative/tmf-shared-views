package shared.views

import grails.gorm.transactions.Transactional

@Transactional
class OrganizationService {

    def add(String... args) {
        log.debug("add -> ${args[0]}")

        TMOrganization organization = new TMOrganization(
                name: args[0]
                , displayName: args[1]
                , siteUrl: args[2]
                , description: args[3]
        )
        organization.save()
        return organization
    }

    /**
     * get the organization by it's id
     * @param args
     * @return
     */
    def get(String... args) {
        log.debug("get -> ${args[0]}")

        return TMOrganization.get(Integer.parseInt(args[0]))
    }

    def update(String... args) {
        log.debug("update -> ${args[0]} ${args[1]} ${args[2]} ${args[3]}")

        TMOrganization organization = TMOrganization.get(Integer.parseInt(args[0]))

        if(organization != null)  {
            organization.siteUrl = args[1]
            organization.description = args[2]
            organization.displayName = args[3]
            organization.save(true)
        }

        return organization
    }

    def delete(String... args) {
        log.debug("delete -> ${args[0]}")

        List<String> ids = args[0].split(":")
        TMOrganization organization = new TMOrganization()

        try  {
            ids.forEach({s ->
                if(s.length() > 0)  {
                    organization = TMOrganization.get(Integer.parseInt(s))
                    organization.delete()
                }
            })
            return organization

        } catch (NumberFormatException nfe)  {
            log.error("Invalid organization Id!")
        }
    }

    def list(String... args) {
        log.debug("list -> ${args[0]}")
        def organizations = []

        if(args[0] == "ALL")  {
            TMOrganization.findAll().forEach({o -> organizations.add(o.toJsonMap())})
            organizations.sort( {o1, o2 ->
                o1.name <=> o2.name
            })
        } else {
            TMOrganization.findById(Integer.parseInt(args[0])).forEach({o -> organizations.add(o.toJsonMap())})
            organizations.sort( {o1, o2 ->
                o1.name <=> o2.name
            })
        }
        return organizations
    }
}
