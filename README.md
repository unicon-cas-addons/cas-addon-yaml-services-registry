cas-addon-yaml-services-registry
================================

CAS service registry implementation that uses YAML configuration format.

This library is built on top of [SnakeYAML](https://code.google.com/p/snakeyaml/) and [YAMLTag](https://github.com/xrrocha/yamltag) libraries to create flexible and human-readable
YAML configuration format for defining rich CAS' `RegisteredService` collection of objects with all their related collaborating objects.

This library replaces `cas-addons 1.x` JSON ServiceRegistry and serves as an alternative to the new [JSON Service Registry](http://jasig.github.io/cas/development/installation/Service-Management.html#persisting-registered-service-data) which will be available in CAS core version 4.1 which is not yet released.

## Current version
`1.0.0-GA`

## Supported CAS version
The minimum supported CAS server version is `4.0.0`

## Usage

* ###Define Maven dependency

  ```xml
  <dependency>
    <groupId>net.unicon.cas</groupId>
    <artifactId>cas-addon-yaml-services-registry</artifactId>
    <version>1.0.0-GA</version>
    <scope>runtime</scope>
  </dependency>
  ```
* ###Define registered services
  Create a file `/etc/cas/servicesRegistry.yml` with services definitions. Example services definitions would look like this:

```yaml
---
  services:
    - !serviceWithAttributes
    id: 1
    name: ONE
    description: Google service
    serviceId: https://www.google.com/**
    evaluationOrder: 1
    theme: default
    enabled: false
    ssoEnabled: false
    anonymousAccess: true
    allowedToProxy: false
    allowedAttributes: [uid, mail]
    ignoreAttributes: false
    usernameAttribute: cn
    logoutType: FRONT_CHANNEL
    extraAttributes:
      authzAttributes:
        memberOf: [group1, group2]
        anotherAttr: val
        unauthorizedUrl: http://exammple.com


        - !serviceWithAttributes
        id: 2
        name: TWO
        serviceId: https://yahoo.com
        evaluationOrder: 2
        attributeFilter: !regexAttributeFilter ["https://.+"]

        - !regexServiceWithAttributes
        id: 3
        name: THREE
        serviceId: ^(https?|imaps?)://.*
        evaluationOrder: 3
        attributeFilter: !defaultAttributeFilter []
        requiredHandlers: [handler1, handler2]
```

  This is YAML format as implemented by SnakeYAML and YAMLTag libraries. Each registered services is represented as a new record within a document
  and indicated by special yaml tag that is mapped internally to a class type of CAS objects that gets instantiated at runtime and collected
  in to a List. Every property defined here (basic or tag representing object type) directly map to a property on the
  `RegisteredService` type that is being instantiated. Here are the current object tags and their mappings:

  ```yaml
      !serviceWithAttributes - maps to net.unicon.cas.addon.registeredservices.DefaultRegisteredServiceWithAttributes

      !regexServiceWithAttributes - maps to net.unicon.cas.addon.registeredservices.RegexRegisteredServiceWithAttributes

      !defaultAttributeFilter - maps to org.jasig.cas.services.support.RegisteredServiceDefaultAttributeFilter

      !regexAttributeFilter - maps to org.jasig.cas.services.support.RegisteredServiceRegexAttributeFilter
  ```

  This example `!defaultAttributeFilter []` instantiates mapped object with the default, no-arg constructor, whereas this one
  `!regexAttributeFilter ["https://.+"]` instantiates a corresponding objects with one argument constrcutor with the value of the argument
  in the brakets.

* ###Define beans
  Make sure do delete an existing `serviceRegistryDao` and define the following beans (in `WEB-INF/spring-configuration/serviceRegistry.xml` for example):

```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:task="http://www.springframework.org/schema/task"
    xmlns:cas-reg-svc="http://unicon.net/schema/cas/registered-services"
    xmlns:cas-external="http://unicon.net/schema/cas/yaml-service-registry"
    xmlns:cas-resource="http://unicon.net/schema/cas/spring-resource"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/task
    http://www.springframework.org/schema/task/spring-task.xsd
    http://unicon.net/schema/cas/registered-services
    http://unicon.net/schema/cas/cas-addon-registered-services.xsd
    http://unicon.net/schema/cas/yaml-service-registry
    http://unicon.net/schema/cas/cas-addon-yaml-service-registry.xsd
    http://unicon.net/schema/cas/spring-resource
    http://unicon.net/schema/cas/cas-addon-spring-resource.xsd">




    <cas-reg-svc:disable-default-registered-services-reloading/>

    <task:annotation-driven/>
    <cas-resource:change-detector id="yamlServiceRegistryResourceChangeWatcher"
      watched-resource="file:/etc/cas/servicesRegistry.yml"/>

      <cas-external:yaml-services-registry/>

    </beans>
```

  Bare `<cas-external:yaml-services-registry/>` definition assumes the yaml configuration file is `/etc/cas/servicesRegistry.yml` by deafault.
  This could be changed (if such a need arises) by introducing an additional `config-file` attribute like so:

  ```xml
  <cas-external:yaml-services-registry config-file="file:/opt/my-other-super-registry-definition.yml"/>
  ```
