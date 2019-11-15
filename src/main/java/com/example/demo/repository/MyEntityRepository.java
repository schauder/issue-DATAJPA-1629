package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.MyEntity;

public interface MyEntityRepository extends CrudRepository<MyEntity, UUID> {

}
