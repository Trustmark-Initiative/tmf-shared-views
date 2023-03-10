function initialize(
    entityFindAll,
    entityFindOne,
    entityInsert,
    entityUpdate,
    entityDelete) {

    document.addEventListener("readystatechange", function () {

        if (document.readyState === "complete") {
            onComplete()
        }
    })

    function onComplete() {

        findAll()
    }

    function findAll() {

        fetchGet(entityFindAll)
            .then(response => response.status !== 200 ?
                Promise.resolve() :
                response.json().then(afterFindAll))
    }

    function afterFindAll(entityFindAll) {

        stateReset()

        document.getElementById("entity-action-insert").outerHTML = document.getElementById("entity-action-insert").outerHTML
        document.getElementById("entity-action-delete").outerHTML = document.getElementById("entity-action-delete").outerHTML

        document.getElementById("entity-action-insert").addEventListener("click", () => onInsertOpen())
        document.getElementById("entity-action-delete").addEventListener("click", () => onDeleteSubmit(entityFindAll))

        const entityTBody = document.getElementById("entity-tbody")
        entityTBody.innerHTML = ""

        if (entityFindAll.length === 0) {

            const entityElement = document.getElementById("entity-template-empty").content.cloneNode(true)

            entityTBody.appendChild(entityElement)

        } else {

            entityFindAll.forEach(entity => {

                const entityElement = document.getElementById("entity-template-summary").content.cloneNode(true)

                const entityElementActionUpdate = entityElement.querySelector(".entity-action-update")
                const entityElementActionDeleteQueue = entityElement.querySelector(".entity-action-delete-queue")
                const clientRegistrationElementRequestClientName = entityElement.querySelector(".client-registration-element-request-client-name")
                const clientRegistrationElementRequestClientUri = entityElement.querySelector(".client-registration-element-request-client-uri")
                const clientRegistrationElementResponseClientId = entityElement.querySelector(".client-registration-element-response-client-id")
                const clientRegistrationElementResponseClientIdIssuedAt = entityElement.querySelector(".client-registration-element-response-client-id-issued-at")
                const clientRegistrationElementResponseClientSecretExpiresAt = entityElement.querySelector(".client-registration-element-response-client-secret-expires-at")
                const clientRegistrationElementResponseStatus = entityElement.querySelector(".client-registration-element-response-status")

                entityElementActionUpdate.addEventListener("click", () => onUpdateOpen(entity.id))
                entityElementActionDeleteQueue.dataset.id = entity.id
                clientRegistrationElementRequestClientName.innerHTML = entity.requestClientName === null ? "" : entity.requestClientName
                clientRegistrationElementRequestClientUri.innerHTML = entity.requestClientUri === null ? "" : entity.requestClientUri
                clientRegistrationElementResponseClientId.innerHTML = entity.responseClientId === null ? "" : entity.responseClientId
                clientRegistrationElementResponseClientIdIssuedAt.innerHTML = entity.responseClientIdIssuedAt === null ? "" : moment(entity.responseClientIdIssuedAt).format('MMMM Do YYYY, h:mm:ss A UTC')
                clientRegistrationElementResponseClientSecretExpiresAt.innerHTML = entity.responseClientSecretExpiresAt === null ? "" : moment(entity.responseClientSecretExpiresAt).format('MMMM Do YYYY, h:mm:ss A UTC')


                if (entity.editable == true && entity.responseFailureMessage === null) {

                    clientRegistrationElementResponseStatus.classList.add("bi-question-circle")
                    clientRegistrationElementResponseStatus.classList.add("text-warning")

                } else if (entity.editable == true && entity.responseFailureMessage !== null) {

                    clientRegistrationElementResponseStatus.classList.add("bi-emoji-frown")
                    clientRegistrationElementResponseStatus.classList.add("text-danger")
                    clientRegistrationElementResponseStatus.title = entity.responseFailureMessage
                } else {

                    clientRegistrationElementResponseStatus.classList.add("bi-emoji-smile")
                    clientRegistrationElementResponseStatus.classList.add("text-success")
                }


                entityTBody.appendChild(entityElement)
            })
        }
    }

    function onInsertOpen() {

        stateReset()

        document.getElementById("entity-action-cancel").outerHTML = document.getElementById("entity-action-cancel").outerHTML
        document.getElementById("entity-action-submit-insert").outerHTML = document.getElementById("entity-action-submit-insert").outerHTML

        document.getElementById("entity-action-cancel").addEventListener("click", onCancel)
        document.getElementById("entity-action-submit-insert").addEventListener("click", onInsertSubmit)

        document.getElementById("entity-form").classList.remove("d-none")
        document.getElementById("entity-form-header-insert").classList.remove("d-none")
        document.getElementById("entity-action-submit-insert").classList.remove("d-none")
    }

    function onUpdateOpen(entityId) {

        const entityPromise = fetchGet(entityFindOne + "?" + new URLSearchParams({"id": entityId})).then(response => response.json())

        Promise.all([entityPromise]).then(array => afterFindOne(array[0]))
    }

    function afterFindOne(entity) {

        stateReset()

        document.getElementById("entity-action-cancel").outerHTML = document.getElementById("entity-action-cancel").outerHTML
        document.getElementById("entity-action-submit-update").outerHTML = document.getElementById("entity-action-submit-update").outerHTML

        document.getElementById("entity-action-cancel").addEventListener("click", onCancel)
        document.getElementById("entity-action-submit-update").addEventListener("click", onUpdateSubmit)

        document.getElementById("entity-form").classList.remove("d-none")
        document.getElementById("entity-form-header-update").classList.remove("d-none")
        document.getElementById("entity-action-submit-update").classList.remove("d-none")
        document.getElementById("entity-status").classList.remove("d-none")

        document.getElementById("client-registration-input-id").value = entity.id
        document.getElementById("client-registration-input-request-initial-access-token").value = entity.requestInitialAccessToken
        document.getElementById("client-registration-input-request-client-name").value = entity.requestClientName
        document.getElementById("client-registration-input-request-client-uri").value = entity.requestClientUri
        document.getElementById("client-registration-input-response-client-id").value = entity.responseClientId
        document.getElementById("client-registration-input-response-client-id-issued-at").value = entity.responseClientIdIssuedAt === null ? "" : moment(entity.responseClientIdIssuedAt).format('MMMM Do YYYY, h:mm:ss A UTC')
        document.getElementById("client-registration-input-response-client-secret-expires-at").value = entity.responseClientSecretExpiresAt === null ? "" : moment(entity.responseClientSecretExpiresAt).format('MMMM Do YYYY, h:mm:ss A UTC')
        document.getElementById("client-registration-input-response-registration-access-token").value = entity.responseRegistrationAccessToken
        document.getElementById("client-registration-input-response-registration-client-uri").value = entity.responseRegistrationClientUri
        document.getElementById("client-registration-input-response-failure-message").value = entity.responseFailureMessage

        document.getElementById("client-registration-input-request-initial-access-token").disabled = !entity.editable
        document.getElementById("client-registration-input-request-client-name").disabled = !entity.editable
        document.getElementById("client-registration-input-request-client-uri").disabled = !entity.editable
        document.getElementById("entity-action-submit-update").disabled = !entity.editable
    }

    function onCancel() {

        stateReset()
    }

    function onInsertSubmit() {

        fetchPost(entityInsert, {
            requestInitialAccessToken: document.getElementById("client-registration-input-request-initial-access-token").value,
            requestClientName: document.getElementById("client-registration-input-request-client-name").value,
            requestClientUri: document.getElementById("client-registration-input-request-client-uri").value,
        })
            .then(response => response.status !== 200 ?
                onFailure(response.json()) :
                findAll())
    }

    function onUpdateSubmit() {

        fetchPost(entityUpdate, {
            id: document.getElementById("client-registration-input-id").value,
            requestInitialAccessToken: document.getElementById("client-registration-input-request-initial-access-token").value,
            requestClientName: document.getElementById("client-registration-input-request-client-name").value,
            requestClientUri: document.getElementById("client-registration-input-request-client-uri").value,
        })
            .then(response => response.status !== 200 ?
                onFailure(response.json()) :
                findAll())
    }

    function onDeleteSubmit(entityList) {

        const idList = Array.from(document.querySelectorAll(".entity-action-delete-queue:checked"))
            .map(element => element.dataset.id)

        fetchPost(entityDelete, {
            idList: idList
        })
            .then(response => response.status !== 200 ?
                onFailureForDelete(entityList, idList, response.json()) :
                findAll())
    }

    function onFailure(failureMapPromise) {

        formResetValidation()

        const labelFor = {
            "requestInitialAccessToken": "request-initial-access-token",
            "requestClientName": "request-client-name",
            "requestClientUri": "request-client-uri",
        }

        messageMapShow(failureMapPromise, "client-registration", labelFor)
    }

    function onFailureForDelete(entityList, idList, failureMapPromise) {
    }

    function stateReset() {

        document.getElementById("entity-form").classList.add("d-none")
        document.getElementById("entity-status").classList.add("d-none")
        document.getElementById("entity-form-header-insert").classList.add("d-none")
        document.getElementById("entity-form-header-update").classList.add("d-none")
        document.getElementById("entity-action-submit-insert").classList.add("d-none")
        document.getElementById("entity-action-submit-update").classList.add("d-none")
        document.getElementById("entity-delete-response-failure").classList.add("d-none")
        document.getElementById("entity-delete-response-failure").innerHTML = ""

        formResetValue()
    }

    function formResetValue() {

        document.getElementById("client-registration-input-id").value = ""

        document.getElementById("client-registration-input-request-initial-access-token").value = ""
        document.getElementById("client-registration-input-request-client-name").value = ""
        document.getElementById("client-registration-input-request-client-uri").value = ""
        document.getElementById("client-registration-input-response-client-id").value = ""
        document.getElementById("client-registration-input-response-client-id-issued-at").value = ""
        document.getElementById("client-registration-input-response-client-secret-expires-at").value = ""
        document.getElementById("client-registration-input-response-registration-access-token").value = ""
        document.getElementById("client-registration-input-response-registration-client-uri").value = ""

        document.getElementById("client-registration-input-request-initial-access-token").disabled = false
        document.getElementById("client-registration-input-request-client-name").disabled = false
        document.getElementById("client-registration-input-request-client-uri").disabled = false
        document.getElementById("client-registration-input-response-client-id").disabled = false
        document.getElementById("client-registration-input-response-client-id-issued-at").disabled = false
        document.getElementById("client-registration-input-response-client-secret-expires-at").disabled = false
        document.getElementById("client-registration-input-response-registration-access-token").disabled = false
        document.getElementById("client-registration-input-response-registration-client-uri").disabled = false

        formResetValidation()
    }

    function formResetValidation() {

        document.getElementById("client-registration-input-request-initial-access-token").classList.remove("is-invalid")
        document.getElementById("client-registration-input-request-client-name").classList.remove("is-invalid")
        document.getElementById("client-registration-input-request-client-uri").classList.remove("is-invalid")

        document.getElementById("client-registration-invalid-feedback-request-initial-access-token").innerHTML = ""
        document.getElementById("client-registration-invalid-feedback-request-client-name").innerHTML = ""
        document.getElementById("client-registration-invalid-feedback-request-client-uri").innerHTML = ""
    }
}
