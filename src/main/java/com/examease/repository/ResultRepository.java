
package com.examease.repository;

import com.examease.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByRollNumber(String rollNumber);
}

