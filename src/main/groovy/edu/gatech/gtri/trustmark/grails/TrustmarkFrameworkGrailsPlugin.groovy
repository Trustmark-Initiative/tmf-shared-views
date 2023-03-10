package edu.gatech.gtri.trustmark.grails

import grails.converters.JSON
import grails.plugins.Plugin

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TrustmarkFrameworkGrailsPlugin extends Plugin {
    def grailsVersion = "4.0.2 > *"
    def profiles = ['web']

    void doWithApplicationContext() {
        initializeJson()
    }

    void initializeJson() {
        JSON.registerObjectMarshaller(LocalDateTime) { final LocalDateTime it ->
            return it?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }
}
