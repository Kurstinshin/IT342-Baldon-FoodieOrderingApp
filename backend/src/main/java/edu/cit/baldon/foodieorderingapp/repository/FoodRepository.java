package edu.cit.baldon.foodieorderingapp.repository;

import edu.cit.baldon.foodieorderingapp.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
}
