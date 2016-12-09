# GeoServer
Predict cases not expected

# How to use
### GeoServerStyleManager
Class to predict using the [GeoServer REST Style Manager of GeoSolutions] (https://github.com/geosolutions-it/geoserver-manager) with CSS.

I've tested on [GeoServer 2.10](https://sourceforge.net/projects/geoserver/files/GeoServer/2.10.0/), and add you need to add [Geoserver CSS plugin](https://sourceforge.net/projects/geoserver/files/GeoServer/2.10.0/extensions/geoserver-2.10.0-css-plugin.zip/download) at your geoserver.

To use in your project it's necessary include the [lib](http://maven.geo-solutions.it/it/geosolutions/geoserver-manager/1.7.0/geoserver-manager-1.7.0.jar) or dependencies if you use Maven:

```xml
   <repository>
      <id>GeoSolutions</id>
      <url>http://maven.geo-solutions.it/</url>
   </repository>
```

```xml
  <dependency>
    <groupId>it.geosolutions</groupId>
    <artifactId>geoserver-manager</artifactId>
    <version>1.7.0</version>
  </dependency>
```
