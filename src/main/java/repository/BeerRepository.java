package repository;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Beer;

import java.util.List;
import java.util.Optional;

public class BeerRepository extends GenericRepositoryImpl{

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
        String queryString = "SELECT b FROM beer b WHERE b.category_id=:category_id";
        TypedQuery<Beer> typedQuery = entityManager.createQuery(queryString, Beer.class);
        typedQuery.setParameter("category_id", categoryId);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

    @Override
    public Beer findById(EntityManager entityManager, long id) {
        Beer entity = (Beer) entityManager.find(getEntityClass(), id);
        return entity;

//        String queryString = "SELECT u FROM " + getEntityClass().getSimpleName() + " u WHERE u.id=:id";
//        TypedQuery<T> typedQuery = entityManager.createQuery(queryString, getEntityClass());
//        typedQuery.setParameter("id", id);
//        if  (!typedQuery.getResultList().isEmpty()) {
//            return entityManager.find(getEntityClass(), id);
//        } else {
//            return null;
//        }
    }

}
