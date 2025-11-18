package service;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import model.Beer;
import model.Category;
import repository.GenericRepository;
import repository.GenericRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class BeerService {

    //private BeerRepository beerRepository = new BeerRepository();
    GenericRepository<Beer, Long> beerRepository = new GenericRepositoryImpl<>(Beer.class);

    public void create(Beer entity) {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            beerRepository.create(em, entity);
        } finally {
            em.close();
        }
    }

    public Optional<Beer> findById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        Beer entity;
        try{
            entity = beerRepository.findById(em, id);
        } catch(EntityNotFoundException ignored){
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(entity);
    }

    public List<Beer> findAll(){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        return beerRepository.findAll(em);
    }

    public void update(Beer beer){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            beerRepository.update(em, beer);
        } finally {
            em.close();
        }
    }

    public void deleteById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            Beer beer = beerRepository.findById(em, id);
            if (beer != null) {
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                beerRepository.deleteById(em, id);
                transaction.commit();
            }else {
                throw new EntityNotFoundException("Beer with id " + id + " not found");
            }
        }finally {
            em.close();
        }
    }

}
