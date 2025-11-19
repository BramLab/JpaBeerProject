package service;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.*;
import model.Brewer;
import repository.GenericRepository;
import repository.GenericRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class BrewerService {
    private GenericRepository<Brewer, Long> brewerRepository = new GenericRepositoryImpl<>(Brewer.class);

    public void create(Brewer brewer) {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            brewerRepository.create(em, brewer);
        } finally {
            em.close();
        }
    }

    public Optional<Brewer> findById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        Brewer brewer;
        try {
            brewer = brewerRepository.findById(em, id);
            if (brewer != null) {
                int fetchAllHack = brewer != null ? brewer.getBeers().size() : 0;
            }
        } catch(EntityNotFoundException e){
            return Optional.empty();
        } finally {
            em.close();
        }
        return Optional.ofNullable(brewer);
    }

    public List<Brewer> findAll(){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        return brewerRepository.findAll(em);
    }

    public void update(Brewer brewer){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            if (brewer.getId() == 0){
                throw new FeedbackToUserException("Brouwer bestaat nog niet in database.");
            }
            Brewer brewerFromRepo = brewerRepository.findById(em, brewer.getId());
            if(brewerFromRepo == null){
                throw new FeedbackToUserException("Brouwer bestaat nog niet in database.");
            }
//            transaction.rollback();
//            if (!em.contains(brewer)){
//                throw new FeedbackToUserException("Brouwer mogelijk veranderd ondertussen. Ververs eerst.");
//            }
            brewerRepository.update(em, brewer);
            transaction.commit();
        } finally {
            em.close();
        }
    }

    public void deleteById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            brewerRepository.deleteById(em, id);
        } catch (RollbackException rbe) {
            throw new FeedbackToUserException("Dit element wordt nog voor andere elementen gebruikt." + " Brouwer id " + id);
        }catch(NoResultException nre) {
            throw new FeedbackToUserException("Element niet gevonden." + " Brouwer id " + id);
        }catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public boolean isDetached(Brewer brewer) {
        EntityManager em = JpaConfig.getEntityManager();
        return brewer.getId() != 0  // must not be transient
                && !em.contains(brewer)  // must not be managed now
                && em.find(Brewer.class, brewer.getId()) != null;  // must not have been removed
    }

}
