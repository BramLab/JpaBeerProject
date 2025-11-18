package repository;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import model.Beer;

import java.util.List;
import java.util.Optional;

public class BeerRepository {

//    create(T entity)
//• findById(Long id)
//• findAll()
//• update(T entity)
//• delete(Long id)
//
//    findBeersByCategory(long categoryId)
//    o findBeersByBrewer(long brewerId)
//    o findBeersCheaperThan(double maxPrice)

//    public void create(Beer beer) {
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        try{
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            em.persist(beer);
//            transaction.commit();
//        }finally{
//            em.close();
//        }
//    }
//
//    public Optional<Beer> findById(long id){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        Beer beer = null;
//        try{
//            beer = em.find(Beer.class, id);
//            if (beer != null) {
//                long fetchAllHack = beer.getBrewer().getId() + beer.getCategory().getId();
//            }
//        } finally {
//            em.close();
//        }
//        return Optional.ofNullable(beer);
//    }
//
//    public List<Beer> findAll(){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        List<Beer> beers = em.createQuery("select b from Beer b ").getResultList();
//        for (Beer beer : beers){
//            long fetchAllHack = beer.getBrewer().getId() + beer.getCategory().getId();
//        }
//        em.close();
//        return beers;
//    }
//
//    public void update(Beer beer){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        try {
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            em.merge(beer);
//            transaction.commit();
//        }finally {
//            em.close();
//        }
//    }
//
//    public void deleteById(long id){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        try{
//            Beer beer = em.find(Beer.class, id);
//            if  (beer != null) {
//                EntityTransaction transaction = em.getTransaction();
//                transaction.begin();
//                em.remove(beer);
//                transaction.commit();
//            }else {
//                throw new EntityNotFoundException("Beer with id " + id + " not found");
//            }
//        }finally {
//            em.close();
//        }
//    }


}
