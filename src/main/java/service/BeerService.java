package service;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.*;
import model.Beer;
import repository.BeerRepository;
import repository.GenericRepository;
import repository.GenericRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class BeerService {

    BeerRepository beerRepository = new BeerRepository();

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

    public List<Beer> findByCategoryId(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        return beerRepository.findBeersByCategory(em, id);
    }

    public List<Beer> findByBrewerId(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        return beerRepository.findBeersByBrewer(em, id);
    }

    public List<Beer> findBeersCheaperThan(float maxPrice){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        return beerRepository.findBeersCheaperThan(em, maxPrice);
    }

    public void update(Beer beer){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{

            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            // unlikely. (untested)
            if (beer.getId() == 0){
                throw new FeedbackToUserException("Bier bestaat nog niet in database. (0)");
            }
            Beer beerFromRepo = beerRepository.findById(em, beer.getId());
            if(beerFromRepo == null){
                throw new FeedbackToUserException("Bier bestaat nog niet in database. (null)");
            }
            beerRepository.update(em, beer);
            transaction.commit();
        } finally {
            em.close();
        }
    }

    public void deleteById (long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            beerRepository.deleteById(em, id);
        }catch(NoResultException nre){
            throw new FeedbackToUserException("Element niet gevonden." + " Bier id " + id);
        } finally {
            em.close();
        }
    }

}
