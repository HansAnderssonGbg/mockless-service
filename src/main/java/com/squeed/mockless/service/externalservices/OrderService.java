package com.squeed.mockless.service.externalservices;

import com.squeed.mockless.service.api.model.Order;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/orders")
@RegisterRestClient(configKey="order-api")
@ApplicationScoped
public interface OrderService {

    @GET
    @Path("{id}")
    List<Order> getByCustomerId(@PathParam("id") Long customerId);

}
