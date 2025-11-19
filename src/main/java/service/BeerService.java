package service;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.*;
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

    public void deleteById (long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            beerRepository.deleteById(em, id);
        } catch (RollbackException rbe) {
            throw new FeedbackToUserException("Dit element wordt nog voor andere elementen gebruikt." + " Brouwer id " + id);
        }catch(NoResultException nre){
            throw new FeedbackToUserException("Element niet gevonden." + " Brouwer id " + id);
        } finally {
            em.close();
        }
    }

}
