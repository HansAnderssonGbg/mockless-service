package com.squeed.mockless.service.db;

import io.quarkus.hibernate.orm.panache.PanacheEntity;;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class CustomerDB extends PanacheEntity {

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

}
