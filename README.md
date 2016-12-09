# GeoServer
Predict cases that Geoserver doesn't yet contemplate.

# How to use
### GeoServerStyleManager
Class to predict the use of CSS style on GeoServer extends class GeoServerRESTStyleManager(GeoSolutions).
I've tested on GeoServer 2.10, and add Geoserver CSS plugin.

To use in your project it's necessary include the lib (or dependencies if you use Maven):

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
