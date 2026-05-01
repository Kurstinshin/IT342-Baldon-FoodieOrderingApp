package edu.cit.baldon.foodieorderingapp.repository;

import edu.cit.baldon.foodieorderingapp.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
