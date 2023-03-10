# Trustmark Framework Shared Views 
This repository holds the source for shared views that are used across all of the Trustmark Tools Grails Applications.

## How to Build

gradle publishToMavenLocal will build and install the shared views
Note that shared-views uses the tmf-api, version 1.4.2 at a minimum

for uploading attachments, make sure that application.yml settings for

spring:
   servlet:
        multipart:
            enabled: true
            max-file-size: 10MB
            max-request-size: 10MB

are included, you may need to modify max-file-size and max-request-size

To include the shared-views plugin in your application for versions up to 0.7:
  add implementation "shared.views:tmf-shared-views:0.5" (or whatever is the latest version) to your build.gradle file

To include the shared-views plugin in your application for verisons 0.8 to current, for example:
  add implementation "edu.gatech.gtri.trustmark:tf-grails-shared-views:0.9.1"

