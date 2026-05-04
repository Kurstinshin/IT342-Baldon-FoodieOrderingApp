package edu.cit.baldon.foodieorderingapp.shared;

import edu.cit.baldon.foodieorderingapp.feature.auth.User;
import edu.cit.baldon.foodieorderingapp.feature.auth.UserRepository;
import edu.cit.baldon.foodieorderingapp.feature.food.Food;
import edu.cit.baldon.foodieorderingapp.feature.food.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Shared — Data Seeder
 * Seeds admin user and default food items on first boot.
 */
@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(FoodRepository foodRepo, UserRepository userRepo) {
        return args -> {
            BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

            if (!userRepo.existsByEmail("admin@foodie.com")) {
                User admin = new User("Admin", "User", "admin@foodie.com", enc.encode("admin123"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
                System.out.println("--- Admin user created: admin@foodie.com / admin123 ---");
            }

            if (foodRepo.count() == 0) {
                foodRepo.save(new Food("Shrimp Fried Rice", "Delicious shrimp fried rice with diced carrots, peas, and onions.", 50.0, "Rice", "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=300&q=80"));
                foodRepo.save(new Food("Pork Satay", "Grilled pork satay skewers served with sweet peanut sauce.", 100.0, "Grilled", "https://images.unsplash.com/photo-1529692236671-f1f6cf9683ba?w=300&q=80"));
                foodRepo.save(new Food("Papaya Salad", "Fresh green papaya salad with tomatoes, lime, and crushed peanuts.", 80.0, "Salad", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=300&q=80"));
                foodRepo.save(new Food("Chicken Adobo", "Classic Filipino chicken adobo braised in soy sauce, vinegar, and garlic.", 120.0, "Chicken", "https://images.unsplash.com/photo-1598103442097-8b74394b95c4?w=300&q=80"));
                foodRepo.save(new Food("Beef Burger", "Juicy homemade beef burger topped with melted cheese and fresh lettuce.", 150.0, "Burger", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300&q=80"));
                foodRepo.save(new Food("Ramen Noodles", "Hot ramen noodles immersed in rich broth with egg and tender pork slices.", 130.0, "Noodles", "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=300&q=80"));
                foodRepo.save(new Food("Mango Shake", "Sweet and refreshing shake blended from ripe fresh mangoes.", 60.0, "Drinks", "https://images.unsplash.com/photo-1553530666-ba11a90a3546?w=300&q=80"));
                foodRepo.save(new Food("Vegetable Curry", "Spicy mixed vegetable curry featuring potatoes, carrots, and peas.", 110.0, "Curry", "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=300&q=80"));
                System.out.println("--- 8 default food items seeded into database ---");
            }
        };
    }
}
