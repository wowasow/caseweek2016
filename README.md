# init vertx project - initial step

This step should get you familiar with:
* generation of vertx project using Maven/Gradle
* importing project to your favourite IDE
* Learn about vertx project structure
* Learn about basic vertx concepts


1. Generate project structure
2*. Optional: Change sources to eclipse or intelij sources
3. Import project ot your favourite IDE 

In my case it is eclipse [add screens to presentation] however feel free to use 
the  best IDE that suits all your needs.

4. Describe structure of a newly created project

[print directory structure and eventually add those screens to presentation]
After successful project generation and importing it to the IDE you should be able to display the following project structure:

+---maven 				# maven wrapper jars
+---src 				# project sources
¦   +---main
¦   ¦   +---assembly 			# [0] for maven assembly plugin
¦   ¦   +---java 			# this is where java source files reside
¦   ¦   ¦   +---com
¦   ¦   ¦       +---comarch
¦   ¦   ¦           +---caseweek2016
¦   ¦   +---platform_lib		# [1]
¦   ¦   +---resources			
¦   ¦       +---platform_lib
|   |	    +---mod.json 		# [4]
|   |
¦   +---test 				# directory for all the tests
¦       +---java
¦       ¦   +---com
¦       ¦       +---comarch
¦       ¦           +---caseweek2016
¦       ¦               +---integration
¦       ¦               ¦   +---groovy
¦       ¦               ¦   +---java
¦       ¦               ¦   +---javascript
¦       ¦               ¦   +---python
¦       ¦               ¦   +---ruby
¦       ¦               +---unit
¦       +---resources
|	    +---platform_lib		# [2]
¦           +---integration_tests
¦               +---groovy
¦               +---javascript
¦               +---python
¦               +---ruby
|---pom.xml 				# maven configuration file

The generated project is a sample maven vertx module that listens for ping messages on vertx event bus. 
This is all that we need at the moment.

By default created module contains a simple Java verticle which listens on the event bus and responds to `ping!`
messages with `pong!`.

The example shows how to write a testin mutliply languages groovy, java, javascript, python, ruby.

The fatjar starter knows to add this directory (and any jars/zips inside it) to the Vert.x platform classpath when executing your module as a fatjar. 	

The most important directories and files are in the examples are:
	* src/main/java 		# this is where our sources are going to reside
	* src/main/test/java		# this is where our tests are going to reside
	* pom.xml			# main configuration point for every maven project

Lets take a look at pom.xml. There you should be able to see a bunch of properties: 
	* pullInDeps - (as described) whether module's dependencies are going to be dowloaded during packaing or runtime (when used for the first time)
	* createFatJar - package entire module as a fat jar (which contains moduls binaries and vertx platfor jars) that can be run from command line
			as java -jar ....
	* module.name - self explanatory
 	* mods.directory - where module is going to be assembled,

	* dependencies version - versions of dependencies that are required by out module
	* [maven plugin versions]

The properties are used in maven plugins used to compile/package the project. The most important dependencies used in the project are:
	* vertx-core - Vert.x core contains fairly low level functionality including support for HTTP, TCP, file system access, and various other features. 
		You can use this directly in your own applications, and it's used by many of the other components of Vert.x
	* vertx-platform 
	* vertx-hazelcast - It is the default cluster manager used in the Vert.x distribution, but it can be replaced with another implementation as Vert.x 
		cluster managers are pluggable. This implementation is packaged inside:
	* junit - library used for test suits etc.
	* testtools - testing our asynchronous applications

5. Lets change a couple of things in our pom:
	* [change vertx version] 
	* change java version in maven-compiler plugin to jdk 1.8 so that it can be used with our module.
	* change vertx.createFatJar property to true so that we can build a fat jar and run it independently of our IDE
	* remove all the necessary 
	* note auto-redeploy

5. Lets review vertx main configuraiton file mod.json

5. Run the project by issuing 
	``` mvn clean install ``` - not necessary
	mvn vertx:runMod

TODO: get to know how to run all the verticles
TODO: in the next step we are going to deploy multiply verticles in the one step
	
[COMMAND]

6. Because TDD development let's write a simple test. [maybe a good exercise]

7. Create a verticle that is going to be based on your test 

9. Create a verticle that is used to communicate with this one

8. Run a project again.

notes about directories: 
	[0] The Assembly Plugin for Maven is primarily intended to allow users to aggregate the project output along with its dependencies, modules, site 
		documentation, and other files into a single distributable archive.

	[1] If you want override the default langs.properties, cluster.xml or any other config for the Vert.x platform (i.e.
		not for the module!) then you can add them in here and they will be added to the platform classpath when running
		your module using Gradle.

	[2] If you are using fatjars and you want override the default langs.properties, cluster.xml or any other config for the Vert.x platform (i.e.
		not for the module!) then you can add them in here and they will be added to the platform classpath when running
		your module using Gradle.

	[4] All Vert.x modules contain a mod.json descriptor. This a file containing some JSON which describes the module.
		 Amongst other things it usually contains a main field. This tells Vert.x which verticle to run when the module is deployed.
