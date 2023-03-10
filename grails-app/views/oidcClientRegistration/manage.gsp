<html>
<head>
    <asset:javascript src="utility.js"/>
    <asset:javascript src="oidc_client_registration_manage.js"/>

    <meta name="layout" content="main"/>

    <script type="text/javascript">
        initialize(
            "${createLink(controller:'oidcClientRegistration', action: 'findAll')}",
            "${createLink(controller:'oidcClientRegistration', action: 'findOne')}",
            "${createLink(controller:'oidcClientRegistration', action: 'insert')}",
            "${createLink(controller:'oidcClientRegistration', action: 'update')}",
            "${createLink(controller:'oidcClientRegistration', action: 'delete')}")
    </script>
</head>

<body>
<div class="container pt-4">
    <h2>OAuth 2.0 Client Registrations</h2>
    <table class="table table-bordered table-striped-hack mb-0">
        <thead>
        <tr>
            <th scope="col">
                <a href="#" class="bi-plus-lg" id="entity-action-insert"></a>

                <div style="width: 17px"></div>
            </th>
            <th scope="col"><a href="#" class="bi-trash" id="entity-action-delete"></a></th>
            <th scope="col" style="width: 20%">Client Name</th>
            <th scope="col" style="width: 20%">API Protected Server URL</th>
            <th scope="col" style="width: 20%">Client ID</th>
            <th scope="col" style="width: 20%">Client ID Issued</th>
            <th scope="col" style="width: 20%">Client Secret Expires</th>
            <th scope="col"><span class="bi-question-circle" title="Client Status"></span></th>
        </tr>
        </thead>
        <template id="entity-template-empty">
            <tr>
                <td colspan="11">(No client registrations.)</td>
            </tr>
        </template>
        <template id="entity-template-summary">
            <tr>
                <td><a href="#" class="bi-pencil entity-action-update"></a></td>
                <td><input type="checkbox" class="form-check-input entity-action-delete-queue"></td>
                <td><div class="client-registration-element-request-client-name"></div></td>
                <td><div class="client-registration-element-request-client-uri"></div></td>
                <td><div class="client-registration-element-response-client-id"></div></td>
                <td><div class="client-registration-element-response-client-id-issued-at"></div></td>
                <td><div class="client-registration-element-response-client-secret-expires-at"></div></td>
                <td class="client-registration-element-response-status"></td>
            </tr>
        </template>
        <tbody id="entity-tbody" class="placeholder-glow">
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        <td><span class="placeholder-hack col-3"></span></td>
        </tbody>
    </table>
</div>

<template id="entity-template-delete-response-failure">
    <div class="alert alert-danger">
        <div class="text-decoration-underline entity-element-name"></div>

        <ul class="pt-2 mb-0 entity-element-message"></ul>
    </div>
</template>

<div class="container pt-4 d-none" id="entity-delete-response-failure">
</div>

<div class="container pt-4 d-none" id="entity-form">
    <div class="border rounded card">
        <input id="client-registration-input-id" name="id" type="hidden"/>

        <div class="card-header fw-bold">
            <div class="row">
                <div class="col-11">
                    <div class="d-none" id="entity-form-header-insert">Add Client Registration</div>

                    <div class="d-none" id="entity-form-header-update">Edit Client Registration</div>
                </div>

                <div class="col-1 text-end">
                    <button id="entity-action-cancel" type="button" class="btn-close p-0 align-middle"></button>
                </div>
            </div>
        </div>

        <div class="card-body">
            <div class="row pb-2">
                <label id="client-registration-label-request-initial-access-token"
                       class="col-3 col-form-label text-end label-required"
                       for="client-registration-input-request-initial-access-token">Initial Access Token</label>

                <div class="col-9">
                    <textarea id="client-registration-input-request-initial-access-token" name="name"
                              class="form-control"
                              aria-describedby="client-registration-invalid-feedback-request-initial-access-token"
                              rows="6"></textarea>

                    <div id="client-registration-invalid-feedback-request-initial-access-token"
                         class="invalid-feedback"></div>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-request-client-name"
                       class="col-3 col-form-label text-end label-required"
                       for="client-registration-input-request-client-name">Client Name</label>

                <div class="col-9">
                    <input type="text" id="client-registration-input-request-client-name" name="name"
                           class="form-control"
                           aria-describedby="client-registration-invalid-feedback-request-client-name"/>

                    <div id="client-registration-invalid-feedback-request-client-name" class="invalid-feedback"></div>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-request-client-uri"
                       class="col-3 col-form-label text-end label-required"
                       for="client-registration-input-request-client-uri">API Protected Server URL</label>

                <div class="col-9">
                    <input type="text" id="client-registration-input-request-client-uri" name="name"
                           class="form-control"
                           aria-describedby="client-registration-invalid-feedback-request-client-uri"/>

                    <div id="client-registration-invalid-feedback-request-client-uri" class="invalid-feedback"></div>
                </div>
            </div>
        </div>

        <div class="card-footer text-start">
            <div class="row">
                <div class="col-3"></div>

                <div class="col-9">
                    <button id="entity-action-submit-insert" type="button" class="btn btn-primary d-none">Add</button>
                    <button id="entity-action-submit-update" type="button" class="btn btn-primary d-none">Save</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container pt-4 d-none" id="entity-status">
    <div class="border rounded card">
        <div class="card-header fw-bold">
            <div class="row">
                <div class="col-11">
                    Client Registration Status
                </div>
            </div>
        </div>

        <div class="card-body">
            <div class="row pb-2">
                <label id="client-registration-label-response-client-id" class="col-3 col-form-label text-end"
                       for="client-registration-input-response-client-id">Client ID</label>

                <div class="col-9">
                    <input type="text" id="client-registration-input-response-client-id" name="response-client-id"
                           class="form-control" readonly>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-response-client-id-issued-at" class="col-3 col-form-label text-end"
                       for="client-registration-input-response-client-id-issued-at">Client ID Issued At</label>

                <div class="col-9">
                    <input type="text" id="client-registration-input-response-client-id-issued-at"
                           name="response-client-id-issued-at" class="form-control" readonly>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-response-client-secret-expires-at"
                       class="col-3 col-form-label text-end"
                       for="client-registration-input-response-client-secret-expires-at">Client Secret Expires At</label>

                <div class="col-9">
                    <input type="text" id="client-registration-input-response-client-secret-expires-at"
                           name="response-client-secret-expires-at" class="form-control" readonly>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-response-registration-access-token"
                       class="col-3 col-form-label text-end"
                       for="client-registration-input-response-registration-access-token">Registration Access Token</label>

                <div class="col-9">
                    <textarea id="client-registration-input-response-registration-access-token"
                              name="response-registration-access-token" class="form-control" readonly
                              rows="6"></textarea>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-response-registration-client-uri"
                       class="col-3 col-form-label text-end"
                       for="client-registration-input-response-registration-client-uri">Registration Client URI</label>

                <div class="col-9">
                    <input type="text" id="client-registration-input-response-registration-client-uri"
                           name="response-registration-client-uri" class="form-control" readonly>
                </div>
            </div>

            <div class="row pb-2">
                <label id="client-registration-label-response-failure-message" class="col-3 col-form-label text-end"
                       for="client-registration-input-response-failure-message">Failure Message</label>

                <div class="col-9">
                    <textarea id="client-registration-input-response-failure-message" name="response-failure-message"
                              class="form-control" readonly rows="4"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
