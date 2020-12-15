# mockless-service 

The aim for this service is to showcase a good way to write unit tests without using any mocks. 

## Test components
### [HoverflyResource.java](https://github.com/HansAnderssonSqueed/mockless-service/blob/main/src/test/java/com/squeed/mockless/service/testutils/HoverflyResource.java)
Starts hoverfly

### [EmbeddedPostgresResource.java](https://github.com/HansAnderssonSqueed/mockless-service/blob/main/src/test/java/com/squeed/mockless/service/testutils/EmbeddedPostgresResource.java)
Starts postgres server

### [OrderServiceSimulation.java](https://github.com/HansAnderssonSqueed/mockless-service/blob/main/src/test/java/com/squeed/mockless/service/testutils/OrderServiceSimulation.java)
Configures endpoints for the order service.

### [KafkaResource.java](https://github.com/HansAnderssonSqueed/mockless-service/blob/kafka/src/test/java/com/squeed/mockless/service/testutils/KafkaResource.java)
Starts a Kafka cluster.

## Resource Usage
By annotating a testClass with `@QuarkusTestResources` and specify the above resources
```java
@QuarkusTest
@QuarkusTestResource(EmbeddedPostgresResource.class)
@QuarkusTestResource(HoverflyResource.class)
@QuarkusTestResource(KafkaResource.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class When_an_action_is_taken {
    
}
```
the specified services for `PostgreSQL` and `Hoverfly` will start.

These resources fulfill the `QuarkusTestResourceLifecycleManager` interface which has a `start()` method.
`start()` returns a `Map<String, String>` which will be set in the application context.

### Hoverfly dsl
`HoverflyResource` starts hoverfly and `OrderServiceSimulation` provides a configurable interface that can be used with `RestAssured`
Excerpt from [When_customer_is_created.java](https://github.com/HansAnderssonSqueed/mockless-service/blob/main/src/test/java/com/squeed/mockless/service/When_customer_is_created.java)
```java
given()
    .spec(externalServices(
        orderService().getOrders(customer.getId()).successfullyReturns(
            order(1,
                row().sku("1001-A").description("Shirt").quantity(2.0),
                row().sku("1023-B").description("Pants").quantity(1.0)
            ),
            order(2)
        )
    ))
    .pathParam("id", customer.getId())
.when()
    .get("/customers/{id}/orders")
.then()
    .assertThat()
        .statusCode(200)
```
## Running the tests

```shell script
./mvnw compile test
```
