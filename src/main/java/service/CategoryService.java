package service;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
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
            Category category = categoryRepository.findById(em, id);
            if (category != null) {
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                categoryRepository.deleteById(em, id);
                transaction.commit();
            }else {
                throw new EntityNotFoundException("Category with id " + id + " not found");
            }
        }finally {
            em.close();
        }
    }
    
}
