# IDP-backend
## Build State

* branch master: [![Build Status](https://travis-ci.org/Krymnos/IDP-backend.svg?branch=master)](https://travis-ci.org/Krymnos/IDP-backend)

### This component acts as an intermediary between the provenance Database and the frontend. The frontend will be issuing the GET requests to the API endpoints defined. These endpoints are listed below:

#### Cluster topology
**/v1/cluster/topology**
**Example URI:** http://122.129.79.67:5000/v1/cluster/topology
#### Cluster stats
**/v1/cluster/stats**
**Example URI :** http://122.129.79.67:5000/v1/cluster/stats
#### Node level provenance
**/v1/cluster/{Node ID}/provenance?size={Number of records}**
**Example URI :** http://122.129.79.67:5000/v1/cluster/bf11fa/provenance?size=50
#### Datapoint provenance 
**/v1/provenance/{Datapoint ID}?structure={structure}**
**Example URI :**  http://122.129.79.67:5000/v1/provenance/5a6830db0000b6badfb2?structure=linear
**Example URI :**  http://122.129.79.67:5000/v1/provenance/5a6830db0000b6badfb2?structure=recursive
#### Download Datapoint provenance
**/v1/provenance/{Datapoint ID}/static?structure={structure}**
**Example URI :** http://122.129.79.67:5000/v1/provenance/5a6830db0000b6badfb2/static?structure=linear
**Example URI :** http://122.129.79.67:5000/v1/provenance/5a6830db0000b6badfb2/static?structure=recursive

## Building

Execute `mvn clean install` to build, and create jar.

## Deploy

Execute `java -jar \path\to\jar\file.jar` to deploy jar.
