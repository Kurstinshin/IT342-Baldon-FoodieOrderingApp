package edu.cit.baldon.foodieorderingapp.feature.food;

import jakarta.persistence.*;

@Entity
@Table(name = "foods")
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
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.category    = category;
        this.img         = img;
    }

    public Long   getId()          { return id; }
    public String getName()        { return name; }
    public void   setName(String v)        { this.name = v; }
    public String getDescription() { return description; }
    public void   setDescription(String v) { this.description = v; }
    public Double getPrice()       { return price; }
    public void   setPrice(Double v)       { this.price = v; }
    public String getCategory()    { return category; }
    public void   setCategory(String v)    { this.category = v; }
    public String getImg()         { return img; }
    public void   setImg(String v)         { this.img = v; }
}
