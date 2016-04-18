I this step we learn how to:
* externalize vertx configuraiton

1. 
* Vertx configuration is set using JSON format
* it can be passed wither through command line or through api

2. Externalize http server host and port with the usage of:
	``` config().getInteger(...)```

3. Set custom configs when deploying verticle in App.java class ("http.host", "http.port")
	* DeployOptions object lets us specify a bunch of deployment options	

4. Choose a random port 

5. Add external configuration under src/main/conf/application-config.json
