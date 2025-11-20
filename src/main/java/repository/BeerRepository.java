package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Beer;

import java.util.List;

public class BeerRepository extends GenericRepositoryImpl<Beer, Long> {

    public BeerRepository() {
        super(Beer.class);
    }

    //    • create(T entity)
//    • findById(Long id)
//    • findAll()
//    • update(T entity)
//    • delete(Long id)
//
//    o findBeersByCategory(long categoryId)
//    o findBeersByBrewer(long brewerId)
//    o findBeersCheaperThan(double maxPrice)

    public List<Beer> findBeersByCategory(EntityManager entityManager, long categoryId) {
        String queryString = "SELECT b FROM model.Beer b WHERE b.category.id=:categoryId";
        TypedQuery<Beer> typedQuery = entityManager.createQuery(queryString, Beer.class);
        typedQuery.setParameter("categoryId", categoryId);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

    public List<Beer> findBeersByBrewer(EntityManager entityManager, long brewerId) {
        String queryString = "SELECT b FROM model.Beer b WHERE b.brewer.id=:brewerId";
        TypedQuery<Beer> typedQuery = entityManager.createQuery(queryString, Beer.class);
        typedQuery.setParameter("brewerId", brewerId);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

    public List<Beer> findBeersCheaperThan(EntityManager entityManager, float maxPrice) {
        String queryString = "SELECT b FROM model.Beer b WHERE b.price < :maxPrice";
        TypedQuery<Beer> typedQuery = entityManager.createQuery(queryString, Beer.class);
        typedQuery.setParameter("maxPrice", maxPrice);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

}
