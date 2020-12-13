package com.squeed.mockless.service.api;

import com.squeed.mockless.service.api.model.Customer;
import com.squeed.mockless.service.api.model.Order;
import com.squeed.mockless.service.db.CustomerDB;
import com.squeed.mockless.service.externalservices.OrderService;
import com.squeed.mockless.service.mappers.CustomerMapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.mapstruct.factory.Mappers;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/customers")
@Produces({"application/json"})
public class CustomerApi {

    CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @RestClient
    OrderService orderService;

    @GET
    public List<Customer> getCustomers() {
        return mapper.toApi(CustomerDB.findAll().list());
    }

    @GET
    @Path("{id}")
    public Customer getCustomer(@PathParam("id") Long id) {
        CustomerDB customer = CustomerDB.findById(id);
        return mapper.toApi(customer);
    }

    @GET
    @Path("{id}/orders")
    public List<Order> getOrders(@PathParam("id") Long id) {
        return orderService.getByCustomerId(id);
    }

    @POST
    @Consumes("application/json")
    @Transactional
    public Customer createCustomer(Customer customer) {
        CustomerDB customerDB = mapper.toDB(customer);
        customerDB.persist();
        return mapper.toApi(customerDB);
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Transactional
    public Customer updateCustomer(@PathParam("id") Long id, Customer customer) {
        CustomerDB customerDB = CustomerDB.findById(id);
        mapper.updateDB(customerDB, customer);
        customerDB.persist();
        return mapper.toApi(customerDB);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        CustomerDB.deleteById(id);
    }

}
