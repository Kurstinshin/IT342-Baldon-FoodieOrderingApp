package edu.cit.baldon.foodieorderingapp.controller;

import edu.cit.baldon.foodieorderingapp.dto.ApiResponse;
import edu.cit.baldon.foodieorderingapp.entity.Food;
import edu.cit.baldon.foodieorderingapp.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    private String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC).format(Instant.now());
    }

    private <T> ApiResponse<T> createResponse(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(getTimestamp());
        return response;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Food>>> getAllFoods() {
        List<Food> foods = foodRepository.findAll();
        return ResponseEntity.ok(createResponse(foods, "Fetched all foods successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> getFoodById(@PathVariable Long id) {
        return foodRepository.findById(id)
                .map(food -> ResponseEntity.ok(createResponse(food, "Food fetched successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(null, "Food not found")));
    }

    // Admin endpoints
    @PostMapping
    public ResponseEntity<ApiResponse<Food>> addFood(@RequestBody Food food) {
        Food saved = foodRepository.save(food);
        ApiResponse<Food> response = createResponse(saved, "Food added successfully");
        response.setStatus(201);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> updateFood(@PathVariable Long id, @RequestBody Food foodDetails) {
        return foodRepository.findById(id)
                .map(food -> {
                    food.setName(foodDetails.getName());
                    food.setDescription(foodDetails.getDescription());
                    food.setPrice(foodDetails.getPrice());
                    food.setCategory(foodDetails.getCategory());
                    food.setImg(foodDetails.getImg());
                    Food updated = foodRepository.save(food);
                    return ResponseEntity.ok(createResponse(updated, "Food updated successfully"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(null, "Food not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(@PathVariable Long id) {
        return foodRepository.findById(id)
                .map(food -> {
                    foodRepository.delete(food);
                    return ResponseEntity.ok(createResponse((Void) null, "Food deleted successfully"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse((Void) null, "Food not found")));
    }
}
