I this step we learn how to:

1. Add maven dependency to vertx-web, and check that everything builds nicely

2. Create a router using
``` Route.router() ```

Router is responsible for dispatching request to the correct handlers. Each request is processed by
handlers which in turn can be easily chained

3. Add router handler replacing old httpServer.requestHandler
``` router.route ```

4. Pass router to http server by using its request handler router::accept

5. Exposing static resources. Create a index.html under src/main/resources/assets (or just copy it from end example)
 
6. Add handler for serving static resources.
``` router.route("/assets/*").handler(StaticHandler.create("assets")); ```

7. Lets create a data item simple bean class (POJO)

8. Create a map in HttpServerVerticle and populate it with some items (e.g. in constructor)

## Reading all items
9. Create a getAll() method that is going to return a list of all items from the map

## Creating an item 
10. Add handler for accessing body and and serving post resource

11. Create a handler for deleting an item

12. Create a handler for updating and getting one item

13. Test, to be continued...
