package shared.views

import edu.gatech.gtri.trustmark.v1_0.FactoryLoader
import edu.gatech.gtri.trustmark.v1_0.impl.io.IOUtils
import edu.gatech.gtri.trustmark.v1_0.impl.model.TrustInteroperabilityProfileReferenceImpl
import edu.gatech.gtri.trustmark.v1_0.impl.model.TrustmarkDefinitionRequirementImpl
import edu.gatech.gtri.trustmark.v1_0.io.TrustInteroperabilityProfileResolver
import edu.gatech.gtri.trustmark.v1_0.model.AbstractTIPReference
import edu.gatech.gtri.trustmark.v1_0.model.TrustInteroperabilityProfile
import org.json.JSONArray
import org.json.JSONObject

import grails.gorm.transactions.Transactional

@Transactional
class ProviderService {
    static final String TRUSTMARKS_FIND_BY_RECIPIENT_TAT_ENDPOINT = "public/trustmarks/find-by-recipient/"
    static final String SYSTEMS_FIND_BY_TYPE = "public/systems/"

    RegistryService registryService

    /**
     * adds a new Provider to the system
     * @param args
     * @return
     */
    def add(String... args) {
        log.info("add -> ${args[0]} ${args[1]} ${args[2]} ${args[3]} ${args[4]}")

        TMProvider provider = new TMProvider()
        try  {
            TMOrganization org = TMOrganization.get(Integer.parseInt(args[3]))

            provider.providerType = ProviderType.valueOf(args[0])
            provider.organization = org
            provider.name = args[1]
            provider.entityId = args[2]

            if(args.length > 4)  {
                provider.fullyCompliant = Boolean.parseBoolean(args[4])
            }
            provider.save(true)

            org.providers.add(provider)
            org.save(true)

        }  catch (NumberFormatException nfe)  {
            provider = new TMProvider(type: ProviderType.valueOf(args[0])
                                    , name: args[1]
                                    , entityId: args[2])
            provider.save(true)
        }

        return provider
    }

    /**
     * gets a provider by id
     * @param args
     * @return
     */
    def get(String... args) {
        log.info("get -> ${args[0]}")

        TMProvider provider = null
        try  {
            provider = TMProvider.get(Integer.parseInt(args[0]))
        } catch (NumberFormatException nfe)  {
            provider = TMProvider.findByUrl(args[0])
        }
        return provider
    }

    /**
     * updates the provider based on it's id
     * @param args
     * @return
     */
    def update(String... args) {
        log.info("update -> ${args[0]} ${args[1]} ${args[2]} ${args[3]} ${args[4]} ${args[5]}")

        TMProvider provider = TMProvider.get(args[0])
        provider.name = args[1]
        provider.entityId = args[2]
        provider.providerType = ProviderType.valueOf(args[3])
        if(args.length > 5)  {
            provider.fullyCompliant = Boolean.parseBoolean(args[5])
        }

        provider.save(true)

        return provider
    }

    /**
     * deletes a provider by id
     * @param args
     * @return
     */
    def delete(String... args) {
        log.info("delete -> ${args[0]} ${args[1]}")

        List<String> ids = args[0].split(":")

        TMOrganization organization = TMOrganization.get(Integer.parseInt(args[1]))
        try  {
            ids.forEach({s ->
                if(s.length() > 0)  {
                    TMProvider provider = TMProvider.get(Integer.parseInt(s))
                    organization.providers.remove(provider)
                    provider.delete()
                }
            })
            organization.save(true)
        } catch (NumberFormatException nfe)  {
            log.error("Invalid Attribute Id!")
        }
        return organization
    }

    /**
     * returns a list of all provider types
     * @return
     */
    def types()  {
        log.info("list ...")
        def types = ProviderType.values()
        return types
    }

    /**
     * returns a list of providers based on organization, if no organization, returns all providers
     * @param args
     * @return
     */
    def list(String... args) {
        log.info("list -> ${args[0]}")

        def providers = []

        try  {
            int organizationId = Integer.parseInt(args[0])
            if(organizationId == 1)  {
                TMProvider.findAll().forEach({e -> providers.add(e.toJsonMap())})
            }  else {
                TMProvider.findAllByOrganization(TMOrganization.get(organizationId)).forEach({e -> providers.add(e.toJsonMap())})
            }
        } catch (NumberFormatException nfe)  {
            TMProvider.findAll().forEach({e -> providers.add(e.toJsonMap())})
        }
        return providers
    }

    /**
     * fetch provider service types from know binding registries
     *
     * @param args
     * @return
     */
    def listByType(String... args)  {
        log.info("listByType -> ${args[0]} ${args[1]}")

        def registries = registryService.list(args[0])   // get registries for this organization
        def providers = []

        registries.forEach({r ->
            String url = r.url.endsWith("/") ? r.url : r.url + "/"+SYSTEMS_FIND_BY_TYPE+args[1]
            log.info(url)
            JSONArray tbrProviders = new JSONArray(getUrl(url))
            tbrProviders.forEach({e ->
                String type = e.providerType.name
                if(type == "IDENTITY_PROVIDER")  {
                    type = "SAML_IDP"
                } else {
                    type = "SAML_SP"
                }
                TMOrganization organization = new TMOrganization(
                        name: e.organization.name
                        , siteUrl: e.organization.siteUrl
                        , description: e.organization.description
                        , displayName: e.organization.displayName
                )
                TMProvider provider = new TMProvider(
                        name: e.name
                        , entityId: e.entityId
                        , id: e.id
                        , providerType: ProviderType.valueOf(type)
                        , organization: organization
                )
                providers.add(provider)
            })
        })
        return providers
    }

    /**
     * returns a list of partner endpoint tips for this provider id
     * @param args
     * @return
     */
    def targetTips(String... args) {
        log.info("targetTips -> ${args[0]}")

        def tips = []

        try  {
            TMProvider provider = TMProvider.get(Integer.parseInt(args[0]))

            if(provider)  {
                provider.conformanceTargetTips.forEach({e -> tips.add(e)})
            }
        } catch (NumberFormatException nfe)  {
            log.error("Non numeric TMProvider id ${args[0]}")
        }
        return tips
    }

    /**
     * adds a partner endpoint tip for this provider id
     * @param args
     * @return
     */
    def addTargetTip(String... args) {
        log.info("targetTips -> ${args[0]} ${args[1]} ${args[2]}")

        def tips = []

        try  {
            TMProvider provider = TMProvider.get(Integer.parseInt(args[0]))

            if(provider)  {
                TMConformanceTargetTip conformanceTargetTip = TMConformanceTargetTip.findByProviderAndConformanceTargetTipIdentifier(provider, args[1])
                if(!conformanceTargetTip)  {
                    conformanceTargetTip = new TMConformanceTargetTip()
                    conformanceTargetTip.provider = provider
                    conformanceTargetTip.conformanceTargetTipIdentifier = args[1]
                    conformanceTargetTip.requireFullCompliance = Boolean.parseBoolean(args[2])
                    conformanceTargetTip.save(true)
                }
                tips.add(conformanceTargetTip)
            }
        } catch (NumberFormatException nfe)  {
            log.error("Non numeric TMProvider id ${args[0]}")
        }
        return tips
    }

    /**
     * deletes a set of partner endpoint tips
     * note that the set passed in are the remaining tips, and those not included are removed
     * @param args
     * @return
     */
    def deleteTargetTip(String... args) {
        log.info("targetTips -> ${args[0]} ${args[1]}")

        def remainingTips = []

        List<String> ids = args[1].split(":")
        try  {
            TMProvider provider = TMProvider.get(Integer.parseInt(args[0]))
            List<TMConformanceTargetTip> currentTips = TMConformanceTargetTip.findAllByProvider(provider)
            ids.forEach({s ->
                TMConformanceTargetTip keepTip = TMConformanceTargetTip.get(Integer.parseInt(s))
                if(keepTip)  {
                    remainingTips.add(keepTip)
                }
            })
            currentTips.removeAll(remainingTips)
            currentTips.forEach({t ->
                t.delete()
            })
        } catch (NumberFormatException nfe)  {
            log.error("Non numeric TMProvider id ${args[0]}")
        }
        return remainingTips
    }

    /**
     * binds trustmarks to this provider id
     * @param id
     * @return
     */
    def bindTrustmarks(String id) {
        log.info("Starting ${this.getClass().getSimpleName()}...")
        long overallStartTime = System.currentTimeMillis()

        try {
            Integer providerId = Integer.parseInt(id)

            TMProvider provider = TMProvider.get(providerId)

            TMOrganization org = provider.organization

            // Get the conformance targe tips
            def conformanceTargetTips = provider.conformanceTargetTips

            // process each conformance target tip
            if (conformanceTargetTips.size() > 0) {

                // Get assessment tool URLs
                def assessmentToolUrls = org.assessmentRepos

                // Get Trustmark Recipient Identifiers
                def recipientIdentifiers = org.trustmarkRecipientIdentifier

                Set<String> processedTipsSet = new HashSet<String>()

                // collection of unique TDs for all conformance target tips
                Set<String> tdSet = new HashSet<String>()

                // The one and only TIP resolver
                TrustInteroperabilityProfileResolver resolver = FactoryLoader.getInstance(TrustInteroperabilityProfileResolver.class)

                // collect TDs recursively for all conformance target TIPS
                conformanceTargetTips.each {
                    String conformanceTargetTipIdentifier = it.conformanceTargetTipIdentifier

                    // get all TDs  for the conformance target TIP
                    resolveTip(resolver, conformanceTargetTipIdentifier, tdSet, processedTipsSet)
                }


                // For each Assessment tool url,
                //      for each recipient identifier,
                //           get all trustmarks and store them in a global set?

                ArrayList<JSONArray> trustmarks = new ArrayList<JSONArray>()

                assessmentToolUrls.each {
                    String tatUrl = ensureTrailingSlash(it.repoUrl)
                    tatUrl += TRUSTMARKS_FIND_BY_RECIPIENT_TAT_ENDPOINT


                    recipientIdentifiers.each {
                        // encode the recipient id url
                        String recipientId = it.trustmarkRecipientIdentifierUrl
                        String recipientIdBase64 = Base64.getEncoder().encodeToString(recipientId.getBytes())
                        String encodedRecipientId = encodeURIComponent(recipientIdBase64)

                        // append the recipient id encoded url
                        tatUrl += encodedRecipientId

                        // get the trustmarks from the TAT
                        JSONObject trustmarksJson = IOUtils.fetchJSON(tatUrl);
                        JSONArray trustmarksJsonArray = trustmarksJson.getJSONArray("trustmarks");

                        trustmarks.add(trustmarksJsonArray)
                    }
                }

                // cross map the Conformance target TIP TDs to the collection of trustmarks
                List<JSONObject> bindingTrustmarks = new ArrayList<JSONObject>();

                // iterate through each TAT's json response
                for (int i = 0; i < trustmarks.size(); i++) {
                    JSONArray trustmarksJsonArray = trustmarks.get(i)
                    for (int j = 0; j < trustmarksJsonArray.length(); j++) {
                        JSONObject trustmark = trustmarksJsonArray.getJSONObject(j);

                        // add if trustmark definitions match
                        if (tdSet.contains(trustmark.getString("trustmarkDefinitionURL")) == true) {
                            bindingTrustmarks.add(trustmark);
                        }
                    }
                }

                log.info("Binding Trustmarks total: " + bindingTrustmarks.size());

                for (int i = 0; i < bindingTrustmarks.size(); i++) {
                    JSONObject trustmark = bindingTrustmarks.get(i);

                    // save to db
                    TMTrustmark tm = new TMTrustmark()
                    tm.name = trustmark.get("name")
                    tm.provider = provider
                    tm.status = trustmark.get("trustmarkStatus")
                    tm.url = trustmark.get("identifierURL")
                    tm.provisional = trustmark.get("hasExceptions")
                    tm.assessorComments = trustmark.get("assessorComments")

                    tm.save(true)
                }

                log.info("Successfully bound " + bindingTrustmarks.size() + " trustmarks to provider: " + provider.name)
            }


        }
        catch(Throwable t) {
            log.error("Error encountered during the trustmark binding process!", t);
        }

        long overallStopTime = System.currentTimeMillis()
        log.info("Successfully Executed ${this.getClass().getSimpleName()} in ${(overallStopTime - overallStartTime)}ms.")

    }//end bindTrustmarks()

    /**
     * resolves a TIP by url
     * @param resolver
     * @param tipUrl
     * @param tdSet
     * @param processedTipsSet
     * @throws Exception
     */
    private void resolveTip(TrustInteroperabilityProfileResolver resolver, String tipUrl,
                            Set<String> tdSet, Set<String> processedTipsSet) throws Exception {
        log.info("Resolving TIP: " + tipUrl);

        if (!processedTipsSet.contains(tipUrl)) {

            processedTipsSet.add(tipUrl)

            URL url = new URL(tipUrl + "?format=xml");

            TrustInteroperabilityProfile tip = resolver.resolve(url);

            for (AbstractTIPReference abstractRef : tip.getReferences()) {

                // Resolve contained TIPs
                if (abstractRef.isTrustInteroperabilityProfileReference()) {
                    TrustInteroperabilityProfileReferenceImpl tipRef = (TrustInteroperabilityProfileReferenceImpl) abstractRef;

                    URI tipRefIdentifier = tipRef.getIdentifier();
                    log.info("TIP Reference Identifier: " + tipRefIdentifier);

                    URL tipRefIdentifierUrl = new URL(tipRefIdentifier.toString());

                    // recurse over contained TIPs
                    resolveTip(resolver, tipRefIdentifierUrl.toString(), tdSet, processedTipsSet);

                // Collect TDs
                } else if (abstractRef.isTrustmarkDefinitionRequirement()) {

                    TrustmarkDefinitionRequirementImpl tfReqImpl = (TrustmarkDefinitionRequirementImpl) abstractRef;

                    URI childTipRefIdentifier = tfReqImpl.getIdentifier();
                    log.info("TD Requirement Identifier: " + childTipRefIdentifier);
                    log.info("TD Requirement Name: " + tfReqImpl.getName());

                    tdSet.add(childTipRefIdentifier.toString());
                }
            }
        }
    }

    private String ensureTrailingSlash(String url) {
        return url.endsWith("/") ? url : url + "/";
    }

    private String encodeURIComponent(String s)
    {
        String result = null;

        try
        {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        }
        // This exception should never occur.
        catch (UnsupportedEncodingException e)
        {
            result = s;
        }

        return result;
    }

    private String getUrl(String urlString)  {
        log.info("getUrl ${urlString}")
        try {
            URL url = new URL(urlString)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection()
            conn.setRequestMethod("GET")
            int responseCode = conn.getResponseCode()
            log.info("Response Code ${responseCode}")
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader rdr = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                String inputLine
                StringBuffer response = new StringBuffer()

                while ((inputLine = rdr.readLine()) != null) {
                    response.append(inputLine)
                }
                rdr.close()
                return response.toString()
            } else {
                System.out.println("GET request not worked")
            }
        } catch(MalformedURLException me) {
            throw new UnsupportedOperationException("URL["+urlString+"] is not a valid URL", me)
        }
        return "[]"
    }
}
