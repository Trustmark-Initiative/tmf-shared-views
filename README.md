# Trustmark Framework Shared Views 
This repository holds the source for shared views that are used across all of the Trustmark Tools Grails Applications.

## How to Build

gradle clean build install will build and install the shared views
Note that shared-views uses the tmf-api, version 1.4.2 at a minimum

for uploading attachments, make sure that application.yml settings for

spring:
   servlet:
        multipart:
            enabled: true
            max-file-size: 10MB
            max-request-size: 10MB

are included, you may need to modify max-file-size and max-request-size

to include the shared-views plugin in your application

add compile "shared.views:tmf-shared-views:0.1" to your build.gradle file

