package com.squeed.mockless.service.mappers;

import com.squeed.mockless.service.api.model.Customer;
import com.squeed.mockless.service.db.CustomerDB;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface CustomerMapper {

    Customer toApi(CustomerDB customer);

    List<Customer> toApi(List<CustomerDB> customer);

    CustomerDB toDB(Customer customer);

    void updateDB(@MappingTarget CustomerDB toUpdate, Customer newValues);

}
