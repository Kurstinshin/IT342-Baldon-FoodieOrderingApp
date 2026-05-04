package edu.cit.baldon.foodieorderingapp.feature.food;

import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Food Feature — Controller
 * Routes: GET/POST/PUT/DELETE /api/v1/foods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Food>>> getAll() {
        return ResponseEntity.ok(foodService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> getById(@PathVariable Long id) {
        ApiResponse<Food> r = foodService.getById(id);
        return r.isSuccess() ? ResponseEntity.ok(r) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Food>> create(@RequestBody Food food) {
        ApiResponse<Food> r = foodService.create(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(r);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Food>> update(@PathVariable Long id, @RequestBody Food food) {
        ApiResponse<Food> r = foodService.update(id, food);
        return r.isSuccess() ? ResponseEntity.ok(r) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> r = foodService.delete(id);
        return r.isSuccess() ? ResponseEntity.ok(r) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
    }
}
