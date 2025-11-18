package model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;

    private String name;
    private float alcoholPercentage;
    private float price;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    public Beer(String name, float alcoholPercentage, float price) {
        this.name = name;
        this.alcoholPercentage = alcoholPercentage;
        this.price = price;
    }

    public Beer(String name, float alcoholPercentage, float price,  Brewer brewer, Category category) {
        this.name = name;
        this.alcoholPercentage = alcoholPercentage;
        this.price = price;
        this.brewer = brewer;
        this.category = category;
    }
}
