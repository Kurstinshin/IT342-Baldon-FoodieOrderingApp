package edu.cit.baldon.foodieorderingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private String category;
    private String img;

    public Food() {}

    public Food(String name, String description, Double price, String category, String img) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.img = img;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
}
