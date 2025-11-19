package service;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.RollbackException;
import model.Brewer;
import repository.GenericRepository;
import repository.GenericRepositoryImpl;

public class BaseService {

//    private GenericRepository<Brewer, Long> brewerRepository = new GenericRepositoryImpl<>(Brewer.class);
//
//    public void deleteById(long id){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        try{
//            brewerRepository.deleteById(em, id);
//        } catch (RollbackException rbe) {
//            throw new FeedbackToUserException("Dit element wordt nog voor andere elementen gebruikt." + " Brouwer id " + id);
//        }catch(NoResultException nre){
//            throw new FeedbackToUserException("Element niet gevonden." + " Brouwer id " + id);
//        } finally {
//            em.close();
//        }
//    }
}
