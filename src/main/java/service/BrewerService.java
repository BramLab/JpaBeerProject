package service;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
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
        try{
            brewer = brewerRepository.findById(em, id);
            //int fetchAllHack = brewer != null ? brewer.getBeers().size() : 0;
        } catch(EntityNotFoundException ignored){
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            em.getTransaction().begin();
            brewerRepository.update(em, brewer);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            Brewer brewer = brewerRepository.findById(em, id);
            if  (brewer != null) {
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                brewerRepository.deleteById(em, id);
                transaction.commit();
            }else {
                throw new FeedbackToUserException("Brouwer met id " + id + " niet gevonden.");
            }
        }catch(jakarta.persistence.RollbackException rbe){
            throw new FeedbackToUserException("Dit element wordt nog voor andere elementen gebruikt.");
        }finally {
            em.close();
        }
    }
}
