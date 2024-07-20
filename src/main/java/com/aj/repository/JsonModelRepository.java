package com.aj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.aj.entity.JsonEntity;

//@Repository
@Component
public interface JsonModelRepository extends JpaRepository<JsonEntity, Long> {
}
