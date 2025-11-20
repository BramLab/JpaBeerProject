package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NamedQueries(
        @NamedQuery(name = "Category.findCategoriesByName",
                query = "SELECT c FROM model.Category c WHERE c.name like CONCAT('%',:name,'%')")
)

@Data
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "category")
    private List<Beer> beers;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
