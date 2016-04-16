#!/bin/bash

#maven properties
MVN_VERSION=3.3.1

# archetype specific - vertx 2.0
# INTERACTIVE_MODE=false
# ARCHETYPE_GROUP_ID=io.vertx
# ARCHETYPE_ARTIFACT_ID=vertx-maven-archetype

INTERACTIVE_MODE=false
ARCHETYPE_GROUP_ID=org.apache.maven.archetypes
ARCHETYPE_ARTIFACT_ID=maven-archetype-quickstart


# project specific
GROUP_ID=com.comarch
ARTIFACT_ID=caseweek2016-vertx-3.0
VERSION=0.1-SNAPSHOT
PACKAGE=com.comarch.caseweek

function generateMvnProject() {
	mvn -X -e archetype:generate \
		-DarchetypeGroupId=$ARCHETYPE_GROUP_ID \
		-DarchetypeArtifactId=$ARCHETYPE_ARTIFACT_ID \
		-DgroupId=$GROUP_ID \
		-DartifactId=$ARTIFACT_ID \
       		-Dversion=$VERSION \
		-Dpackage=$PACKAGE \
		-DinteractiveMode=$INTERACTIVE_MODE 

	cd $ARTIFACT_ID
}

function generateMvnWrapper() {
	cd $ARTIFACT_ID

 	mvn -N com.rimerosolutions.maven.plugins:wrapper-maven-plugin:0.0.4:wrapper  \
		-DmavenVersion=$MVN_VERSION
}

function convertToGradle() {
 	gradle init --type pom
}

function removeMvnLeftOvers() {
	rm -r pom.xml maven* mvn*
}

function convertToEclipseProject() {
	mvn eclipse:eclipse
}

function convertToIdeaProject() {
	mvn idea:idea
}


generateMvnProject
generateMvnWrapper

#convertToEclipseProject
#convertToIdeaProject
#convertToGradle
#removeMvnLeftOvers

