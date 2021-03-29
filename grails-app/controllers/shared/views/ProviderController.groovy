package shared.views

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.nio.charset.StandardCharsets

@Secured(["ROLE_ADMIN","ROLE_ORG_ADMIN", "ROLE_USER"])
class ProviderController {

    OrganizationService organizationService

    ProviderService providerService

    DeserializeService deserializeService

    /**
     * redirects to index.gsp
     */
    def index() { }

    /**
     * redirects to administer.gsp
     * @return
     */
    def administer() {
        log.debug("administer -> ")
    }

    /**
     * redirects to manage.gsp
     * @return
     */
    def manage()  {
        log.debug("manage -> ${params.orgid}")
        [organization: organizationService.get(params.orgid) ]
    }

    /**
     * redirects to dashboard.gsp
     * @return
     */
    def dashboard()  {
        log.debug("dashboard -> ${params.id} , ${params.orgid}")

        [organization: organizationService.get(params.orgid), provider: providerService.get(params.id)]
    }

    /**
     * redirects to view.gsp
     * @return
     */
    def view()  {
        log.info(params.id)
        TMProvider provider = providerService.get(params.id)
        [provider: provider]
    }

    /**
     * redirects to signCertificate.gsp
     * @return
     */
    def signCertificate()  {
        log.info(params.id)
        TMProvider provider = providerService.get(params.id)
        [provider: provider]
    }

    /**
     * redirects to encryptCertificate.gsp
     * @return
     */
    def encryptCertificate()  {
        log.info(params.id)
        TMProvider provider = providerService.get(params.id)
        [provider: provider]
    }

    /**
     * takes an upload set of files, parses and store in the database
     * @return
     */
    def upload() {
        log.info("upload -> ")
        if(params.filename != null)  {
            byte[] buffer = new byte[params.filename.size]
            params.filename.getInputStream().read(buffer)
            String xmlString = new String(buffer, StandardCharsets.UTF_8)
            log.info("File Name: ${params.filename.originalFilename}  ${params.filename.size}")
            deserializeService.deserialize(xmlString)
        }
        [user: user]
    }

    /**
     * AJAX method for adding a provider
     * @return
     */
    def add()  {
        log.info("add ->  ${params.name} ${params.entityId} ${params.fullyComplant}")

        TMProvider provider = providerService.add(params.type
                , params.name
                , params.entityId
                , params.organizationId
                , params.fullyCompliant
                )
        render provider as JSON
    }

    /**
     * AJAX method to remove a provider
     * @return
     */
    def delete()  {
        log.info("delete -> ")

        TMOrganization organization = providerService.delete(params.ids, params.oid)
        render organization as JSON
    }

    /**
     * AJAX method to get a provider by id
     * @return
     */
    def get()  {
        log.info("get -> ")

        TMProvider provider = providerService.get(params.id)
        render provider as JSON
    }

    /**
     * AJAX method to return a list of providers by organization id
     * @return
     */
    def list()  {
        log.info("list -> ")

        def providers = providerService.list(params.id)
        render providers as JSON
    }

    /**
     * proxy method to query the TBR for matching endpoints
     */
    def listByType() {
        log.info("listByType -> ${params.id} ${params.type}")

        def providers = providerService.listByType(params.id, params.type)
        render providers as JSON
    }

    /**
     * AJAX method to return a list of all provider types
     * @return
     */
    def types()  {
        log.info("types -> ")

        def providerTypes = providerService.types()

        render providerTypes as JSON
    }

    /**
     * AJAX method to update a provider by id
     * @return
     */
    def update()  {
        log.info("update -> ${params.id} ${params.name} ${params.entityId} ${params.fullyCompliant}")

        TMProvider provider = providerService.update(params.id
                , params.name
                , params.entityId
                , params.type
                , params.organizationId
                , params.fullyCompliant
        )
        render provider as JSON
    }

    /**
     * AJAX method to return a list of partner endpoint tips by provider id
     * @return
     */
    def targetTips()  {
        log.info("targetTips -> ${params.id}")

        def tips = providerService.targetTips(params.id)
        render tips as JSON
    }

    /**
     * AJAX method to add a partner endpoint type to provider id
     * @return
     */
    def addTargetTip()  {
        log.info("addtargetTips -> ${params.id}  ${params.url} ${params.compliance}")

        def tips = providerService.addTargetTip(params.id, params.url, params.compliance)
        render tips[0] as JSON
    }

    /**
     * AJAX method to remove a list of partner endpoint TIPs by id
     * @return
     */
    def deleteTargetTip()  {
        log.info("deletetargetTips -> ${params.id} ${params.tids}")

        def tips = providerService.deleteTargetTip(params.id, params.tids)
        render tips as JSON
    }
}
