package edu.cit.baldon.foodieorderingapp.feature.food;

import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import edu.cit.baldon.foodieorderingapp.shared.SliceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Food Feature — Service
 */
@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public ApiResponse<List<Food>> getAll() {
        return SliceUtils.ok(foodRepository.findAll(), "Fetched all foods successfully");
    }

    public ApiResponse<Food> getById(Long id) {
        return foodRepository.findById(id)
            .map(f -> SliceUtils.ok(f, "Food fetched successfully"))
            .orElse(SliceUtils.notFound("Food"));
    }

    public ApiResponse<Food> create(Food food) {
        Food saved = foodRepository.save(food);
        ApiResponse<Food> r = SliceUtils.ok(saved, "Food added successfully");
        r.setStatus(201);
        return r;
    }

    public ApiResponse<Food> update(Long id, Food details) {
        return foodRepository.findById(id).map(food -> {
            food.setName(details.getName());
            food.setDescription(details.getDescription());
            food.setPrice(details.getPrice());
            food.setCategory(details.getCategory());
            food.setImg(details.getImg());
            return SliceUtils.<Food>ok(foodRepository.save(food), "Food updated successfully");
        }).orElse(SliceUtils.notFound("Food"));
    }

    public ApiResponse<Void> delete(Long id) {
        return foodRepository.findById(id).map(food -> {
            foodRepository.delete(food);
            return SliceUtils.<Void>ok(null, "Food deleted successfully");
        }).orElse(SliceUtils.notFound("Food"));
    }
}
