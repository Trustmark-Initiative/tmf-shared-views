package shared.views

import grails.gorm.transactions.Transactional

@Transactional
class RegistrantService {

    ContactService contactService

    final String ROLE_USER = 'ROLE_USER'

    /**
     * activate a registant
     * @param args
     * @return
     */
    def activate(String... args) {
        log.debug("activate -> ${args[0]}")

        List<String> ids = args[0].split(":")
        TMRegistrant registrant = new TMRegistrant()

        try  {
            ids.forEach({s ->
                if(s.length() > 0)  {
                    registrant = TMRegistrant.get(Integer.parseInt(s))
                    registrant.active = true
                    registrant.save(true)
                }
            })
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Registrant Id!")
        }

        return registrant
    }

    /**
     * activate a registant
     * @param args
     * @return
     */
    def deactivate(String... args) {
        log.debug("deactivate -> ${args[0]}")

        List<String> ids = args[0].split(":")
        TMRegistrant registrant = new TMRegistrant()

        try  {
            ids.forEach({s ->
                if(s.length() > 0) {
                    registrant = TMRegistrant.get(Integer.parseInt(s))
                    registrant.active = false
                    registrant.save(true)
//                    userService.lock(registrant.user)
                }
            })
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Registrant Id!")
        }

        return registrant
    }

    /**
     * add a registant
     * @param args
     * @return
     */
    def add(String... args) {
        log.debug("add -> ${args[0]} ")

        TMOrganization organization = TMOrganization.get(Integer.parseInt(args[5]))

        TMContact contact = contactService.add(args[0], args[1], args[2], args[3], args[5], "ADMINISTRATIVE")

        TMRegistrant registrant = new TMRegistrant(
                organization: organization
                , contact: contact
        )

        registrant.save(true)

        return registrant
    }

    /**
     * add a registant
     * @param args
     * @return
     */
    def add(TMOrganization org, TMContact contact) {
        log.debug("Registrant.add -> ${org.name} ${contact.email}")

        contact.save(true)
        TMRegistrant registrant = TMRegistrant.findByContact(contact)
        if(!registrant)  {
            registrant = new TMRegistrant(
                    organization: org
                    , contact: contact
            )
            registrant.save(true)
        }

        return registrant
    }

    /**
     * get the registrant based on id or email
     * @param args
     * @return
     */
    def get(String... args) {
        log.debug("get -> ${args[0]}")
        try  {
            return TMRegistrant.get(Integer.parseInt(args[0]))
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Registrant Id ${args[0]}")
        }
    }

    /**
     * just updating password for now
     * @param args
     * @return
     */
    def update(String... args) {
        log.debug("update -> ${args[0]}  ${args[1]}  ${args[2]}  ${args[3]}  ${args[4]}")
        TMRegistrant registrant = null
        try  {
            registrant = TMRegistrant.get(Integer.parseInt(args[0]))
            registrant.contact.lastName = args[1]
            registrant.contact.firstName = args[2]
            registrant.contact.email = args[3]
            registrant.contact.phone = args[4]
            registrant.contact.save(true)
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Registrant Id ${args[0]}")
        }
        return registrant
    }

    /**
     * remove registrants based on id
     * @param args
     * @return
     */
    def delete(String... args) {
        log.debug("delete -> ${args[0]}")

        List<String> ids = args[0].split(":")
        TMRegistrant registrant = new TMRegistrant()
        try  {
            ids.forEach({s ->
                if(s.length() > 0)  {
                    registrant = TMRegistrant.get(Integer.parseInt(s))
//                    userService.lock(registrant.user)
                    registrant.delete()
                }
            })
            return registrant

        } catch (NumberFormatException nfe)  {
            log.error("Invalid Registrant Id!")
        }
    }

    /**
     * updating password
     * @param args
     * @return
     */
    def pswd(String... args) {
        log.info("pswd -> ${args[0]}")
        TMRegistrant registrant = null
        try  {
            registrant = TMRegistrant.get(Integer.parseInt(args[0]))
            if (registrant.user.password == args[1])  {
                registrant.user.password = args[2]
                registrant.user.save(true)
            }
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Registrant Id ${args[0]}")
        }
        return registrant
    }

    def list(String... args) {
        log.debug("list -> ${args[0]}")
        def registrants = []
        try  {
            int organizationId = Integer.parseInt(args[0]);
            if(organizationId == 0)  {
                TMRegistrant.findAll().forEach({r -> registrants.add(r.toJsonMap(false))})
            } else {
                TMRegistrant.findAllByOrganization(TMOrganization.get(organizationId)).forEach({r -> registrants.add(r.toJsonMap(false))})
            }
        }  catch (NumberFormatException nfe)  {
            TMRegistrant.findAll().forEach({r -> registrants.add(r.toJsonMap(false))})
        }
        return registrants
    }
}
