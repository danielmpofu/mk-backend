package com.example.demo.repo;

import com.example.demo.entities.DogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepo extends JpaRepository<DogModel, Long> {
    Optional<DogModel> findByIdAndDeleted(long id, boolean deleted);
    List<DogModel> findAllByDeleted(boolean deleted);
}
