package repository;

import jakarta.persistence.*;
import model.Category;
import java.util.List;

public class CategoryRepository extends GenericRepositoryImpl<Category,Long>{

    public CategoryRepository() {
        super(Category.class);
    }

    public List<Category> findCategoriesByName(EntityManager entityManager, String name) {
        TypedQuery<Category> typedQuery = entityManager.createNamedQuery("Category.findCategoriesByName", Category.class);
        typedQuery.setParameter("name", name);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

}
