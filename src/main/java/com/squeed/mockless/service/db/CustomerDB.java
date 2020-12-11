package com.squeed.mockless.service.db;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

;

@Entity
@Table(name = "mockless.customer")
@Getter
@Setter
public class CustomerDB extends PanacheEntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

}
