package repository;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import model.Beer;
import model.Brewer;

import java.util.List;
import java.util.Optional;

public class BrewerRepository {

    //create(T entity)
    //• findById(Long id)
    //• findAll()
    //• update(T entity)
    //• delete(Long id)

    //o findBrewerByName(String name)
    //o findAllBrewersWithBeerCount()

    public void create(Brewer brewer) {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.persist(brewer);
            transaction.commit();
        }finally{
            em.close();
        }
    }

    public Optional<Brewer> findById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        Brewer brewer = null;
        try{
            brewer = em.find(Brewer.class, id);
            if (brewer != null) {
                for(Beer beer : brewer.getBeers()){
                    long fetchAllHack = beer.getId();
                }
            }
        } finally {
            em.close();
        }
        return Optional.ofNullable(brewer);
    }

    public List<Brewer> findAll(){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        List<Brewer> brewers = em.createQuery("select b from Brewer b ").getResultList();
        for(Brewer brewer : brewers){
            long fetchAllHack = brewer.getBeers().size();
        }
        em.close();
        return brewers;
    }

    public void update(Brewer brewer){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.merge(brewer);
            transaction.commit();
        }finally {
            em.close();
        }
    }

    public void deleteById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            Brewer brewer = em.find(Brewer.class, id);
            if  (brewer != null) {
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.remove(brewer);
                transaction.commit();
            }else {
                throw new EntityNotFoundException("Brewer with id " + id + " not found");
            }
        }finally {
            em.close();
        }
    }
}
