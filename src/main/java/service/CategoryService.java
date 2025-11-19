package service;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.*;
import model.Category;
import repository.GenericRepository;
import repository.GenericRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    GenericRepository<Category, Long> categoryRepository = new GenericRepositoryImpl<>(Category.class);

    public void create(Category entity) {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            categoryRepository.create(em, entity);
        } finally {
            em.close();
        }
    }

    public Optional<Category> findById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        Category entity;
        try{
            entity = categoryRepository.findById(em, id);
        } catch(EntityNotFoundException ignored){
            return Optional.empty();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(entity);
    }

    public List<Category> findAll(){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        return categoryRepository.findAll(em);
    }

    public void update(Category category){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            em.getTransaction().begin();
            categoryRepository.update(em, category);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            categoryRepository.deleteById(em, id);
        } catch (RollbackException rbe) {
            throw new FeedbackToUserException("Dit element wordt nog voor andere elementen gebruikt." + " Brouwer id " + id);
        }catch(NoResultException nre){
            throw new FeedbackToUserException("Element niet gevonden." + " Brouwer id " + id);
        } finally {
            em.close();
        }
    }
    
}
