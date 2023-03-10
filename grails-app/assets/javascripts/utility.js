function fetchGet(input) {
    return fetch(input, {
        method: "GET",
        headers: {
            "Accept": "application/json, text/javascript",
            "Content-Type": "application/json; charset=utf-8",
            "X-Requested-With": "XMLHttpRequest"
        }
    })
}

function fetchPost(input, data) {
    return fetch(input, {
        method: "POST",
        headers: {
            "Accept": "application/json, text/javascript",
            "Content-Type": "application/json; charset=utf-8",
            "X-Requested-With": "XMLHttpRequest"
        },
        body: JSON.stringify(data)
    })
}

function fetchPut(input, data) {
    return fetch(input, {
        method: "PUT",
        headers: {
            "Accept": "application/json, text/javascript",
            "Content-Type": "application/json; charset=utf-8",
            "X-Requested-With": "XMLHttpRequest"
        },
        body: JSON.stringify(data)
    })
}

function fetchDelete(input, data) {
    return fetch(input, {
        method: "DELETE",
        headers: {
            "Accept": "application/json, text/javascript",
            "Content-Type": "application/json; charset=utf-8",
            "X-Requested-With": "XMLHttpRequest"
        },
        body: JSON.stringify(data)
    })
}

function fetchUpload(input, data) {
    return fetch(input, {
        method: "POST",
        body: data
    })
}

function messageMap(failureMap, labelFor, messageFor) {

    function messageForInner(failureKey, failureElement, labelFor) {
        switch (failureElement.type) {
            case "ValidationMessageMustBeEmpty":
                return `The number of associated ${labelFor(failureKey)} must be 0; it is ${failureElement.size}.`
            case "ValidationMessageMustBeLengthGreaterThanOrEqual":
                return `Length of ${labelFor(failureKey)} must be greater than or equal to ${failureElement.lengthMinimumInclusive}; it is ${failureElement.length}.`
            case "ValidationMessageMustBeLengthLessThanOrEqual":
                return `Length of ${labelFor(failureKey)} must be less than or equal to ${failureElement.lengthMaximumInclusive}; it is ${failureElement.length}.`
            case "ValidationMessageMustBeNonNull":
                return `${labelFor(failureKey)} is required.`
            case "ValidationMessageMustBeNumeric":
                return `${labelFor(failureKey)} must be numeric.`
            case "ValidationMessageMustBeReference":
                return `${labelFor(failureKey)} must be a reference.`
            case "ValidationMessageMustBeDistinct":
                return `${labelFor(failureKey)} must be unique.`
            case "ValidationMessageMustBeUnique":
                return `${labelFor(failureKey)} must be unique.`
            case "ValidationMessageMustBeEqual":
                return `${labelFor(failureElement.field1)} and ${labelFor(failureElement.field2)} must be equal.`
            case "ValidationMessageMustBePattern":
                return `${labelFor(failureKey)} must meet the following criteria: ${failureElement.description}`
            default :
                return `${labelFor(failureKey)} invalid.`
        }
    }

    return Object.entries(failureMap).reduce(
        (messageMapInner, failureEntry) => {
            messageMapInner[failureEntry[0]] = failureEntry[1].reduce((messageArrayInner, failureElement) => {
                    if (failureElement.type === undefined) {

                        return messageArrayInner.concat([Object.entries(failureElement).reduce((messageMapInnerInner, failureElementEntry) => {

                                messageMapInnerInner[failureElementEntry[0]] = messageMap(failureElementEntry[1], labelFor, messageFor)

                                return messageMapInnerInner
                            },
                            {})])

                    } else {

                        return messageArrayInner.concat([messageForInner(failureEntry[0], failureElement, labelFor)])
                    }
                },
                [])

            return messageMapInner
        },
        {})
}

function messageMapShow(failureMapPromise, entityName, labelFor) {

    failureMapPromise.then(failureMap => {

        Object
            .entries(messageMap(failureMap, field => document.getElementById(`${entityName}-label-${labelFor[field]}`).innerHTML))
            .forEach(entry => {
                const fieldName = entry[0]
                const messageArray = entry[1]

                document.getElementById(`${entityName}-input-${labelFor[fieldName]}`).classList.add("is-invalid")
                messageArray.forEach(element => {
                    const div = document.createElement("div")
                    div.innerHTML = element
                    document
                        .getElementById(`${entityName}-invalid-feedback-${labelFor[fieldName]}`)
                        .appendChild(div);
                })
            })
    })
}
