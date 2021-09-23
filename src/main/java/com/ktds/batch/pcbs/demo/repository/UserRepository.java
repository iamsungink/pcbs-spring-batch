package com.ktds.batch.pcbs.demo.repository;

import com.ktds.batch.pcbs.demo.model.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {

    Collection<User> findAllByUpdatedDate(LocalDate updatedDate);
}
