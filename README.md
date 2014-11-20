cas-addon-yaml-services-registry
================================

CAS service registry implementation that uses YAML configuration format.

This library is built on top of [SnakeYAML](https://code.google.com/p/snakeyaml/) and [YAMLTag](https://github.com/xrrocha/yamltag) libraries to create flexible and human-readable
YAML configuration format for defining rich CAS' `RegisteredService` collection of objects with all their related collaborating objects.

This library replaces `cas-addons 1.x` JSON ServiceRegistry and serves as an alternative to the new [JSON Service Registry](http://jasig.github.io/cas/development/installation/Service-Management.html#persisting-registered-service-data) which will be available in CAS core version 4.1 which is not yet released.

### Current version
`1.0.0-RC1`

### Supported CAS version
The minimum supported CAS server version is `4.0.0`
