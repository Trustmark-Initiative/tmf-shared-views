package shared.views

import grails.gorm.transactions.Transactional

@Transactional
class ContactService {

    def add(TMOrganization org, String... args) {
        log.debug("add -> ${args[0]} ${args[2]}")

        TMContact contact = TMContact.findByEmail(args[2])
        if(contact == null) {
            log.debug("new contact added -> ${args[0]} ${args[1]} ${args[2]} ${args[3]} ${args[4]}")
                contact = new TMContact(
                        lastName: args[0]
                        , firstName: args[1]
                        , email: args[2]
                        , phone: args[3]
                        , type: ContactType.valueOf(args[4])
                        , organization: org
                )
                contact = contact.save(true)
        }

        try  {
            if(args[5] != null)  {
                TMProvider provider = TMProvider.get(Integer.parseInt(args[5]))
                if(contact != null)  {
                    provider.contacts.add(contact)
                    provider.save(true)
                }
            }
        } catch (ArrayIndexOutOfBoundsException e)  {
            log.error("No provider id.")
        }

        return contact
    }

    /**
     * attempt to find by id first, then email
     * @param args
     * @return
     */
    def get(String... args) {
        log.debug("get -> ${args[0]}")

        TMContact contact = null

        try  {
            contact = TMContact.get(Integer.parseInt(args[0]))
        } catch (NumberFormatException nfe) {
            contact = TMContact.findByEmailLike("%" + args[0] + "%")
        }
        return contact
    }

    /**
     * get a contact, used to check for existing contacts
     * @param contact
     * @return
     */
    def get(TMContact contact) {
        log.info("get -> ${contact}")

        return TMContact.find(contact)
    }

    /**
     * find contact by userid
     * @param userid
     * @return
     */
    def get(Integer userid) {
        log.info("ContactService.get -> ${userid}")

        return TMContact.findByUserid(userid)
    }

    def update(String... args) {
        log.debug("update -> ${args[0]} ${args[1]} ${args[2]} ${args[3]} ${args[4]} ${args[5]}")

        TMContact contact = null
        try  {
            contact = TMContact.get(Integer.parseInt(args[0]))
            contact.lastName = args[1]
            contact.firstName = args[2]
            contact.email = args[3]
            contact.phone = args[4]
            contact.type = ContactType.valueOf(args[5])
            contact.save(true)
        } catch (NumberFormatException nfe) {
            contact = TMContact.findByEmailLike("%" + args[0] + "%")
            contact.lastName = args[1]
            contact.firstName = args[2]
            contact.phone = args[3]
            contact.type = ContactType.valueOf(args[4])
            contact.save(true)
        }
        return contact
    }

    def delete(String... args) {
        log.debug("delete -> ${args[0]}")

        TMContact contact = null

        try  {
            contact = TMContact.get(Integer.parseInt(args[0]))
        } catch (NumberFormatException nfe) {
            contact = TMContact.findByEmailLike("%" + args[0] + "%")
        }
        if(contact != null)  {
            contact.delete()
        }
        return contact
    }

    def list(String... args) {
        log.debug("list -> ${args[0]}")
        def contacts = []

        try  {
            int orgId = Integer.parseInt(args[0])
            if (orgId == 0)  {
                TMContact.findAll().forEach({c -> contacts.add(c.toJsonMap())})
            }  else  {
                TMContact.findAllByOrganization(TMOrganization.get(orgId)).forEach({c -> contacts.add(c.toJsonMap())})
            }
        } catch (NumberFormatException nfe)  {
            TMContact.findAll().forEach({c -> contacts.add(c.toJsonMap())})
        }
        return contacts
    }

    def types() {
        log.debug("types")
        def types = ContactType.values()
        return types
    }
}
