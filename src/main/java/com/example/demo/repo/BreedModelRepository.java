package com.example.demo.repo;

import com.example.demo.entities.BreedModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedModelRepository extends JpaRepository<BreedModel, Long> {
}