package com.ktds.batch.pcbs.demo.repository;

import com.ktds.batch.pcbs.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
