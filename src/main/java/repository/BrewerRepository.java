package repository;

import jakarta.persistence.*;
import model.Brewer;
import java.util.List;

public class BrewerRepository extends GenericRepositoryImpl<Brewer,Long> {

    public BrewerRepository() {
        super(Brewer.class);
    }

    //create(T entity)
    //• findById(Long id)
    //• findAll()
    //• update(T entity)
    //• delete(Long id)

    //o findBrewerByName(String name)
    //o findAllBrewersWithBeerCount()

    public List<Brewer> findBrewerByName(EntityManager entityManager, String name) {
        String queryString = "SELECT b FROM model.Brewer b WHERE b.name like CONCAT('%',:name,'%')";
        TypedQuery<Brewer> typedQuery = entityManager.createQuery(queryString, Brewer.class);
        typedQuery.setParameter("name", name);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

    public List<Object[]> findAllBrewersWithBeerCount(EntityManager entityManager) {
        String queryString =
                "SELECT br.id, br.name, br.location, COUNT(be) " +
                "FROM Brewer br " +
                "LEFT JOIN br.beers be " +
                "GROUP BY br.id, br.location, br.name";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(queryString, Object[].class);
        if (!typedQuery.getResultList().isEmpty()) {
            return typedQuery.getResultList();
        } else {
            return null;
        }
    }

}
