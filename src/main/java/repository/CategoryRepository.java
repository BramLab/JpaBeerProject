package repository;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import model.Beer;
import model.Category;

import java.util.List;
import java.util.Optional;

public class CategoryRepository {

    // create(T entity)
    //• findById(Long id)
    //• findAll()
    //• update(T entity)
    //• delete(Long id)

    //o findCategoryByName(String name)

//    public void create(Category category) {
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        try{
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            em.persist(category);
//            transaction.commit();
//        }finally{
//            em.close();
//        }
//    }

//    public Optional<Category> findById(long id){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        Category category = null;
//        try{
//            category = em.find(Category.class, id);
//            if (category != null) {
//                for(Beer beer : category.getBeers()){
//                    long fetchAllHack = beer.getId();
//                }
//            }
//        } finally {
//            em.close();
//        }
//        return Optional.ofNullable(category);
//    }

//    public List<Category> findAll(){
//        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
//        List<Category> categories = em.createQuery("select c from Category c ").getResultList();
//        for(Category category : categories){
//            long fetchAllHack = category.getBeers().size();
//        }
//        em.close();
//        return categories;
//    }

    public void update(Category category){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.merge(category);
            transaction.commit();
        }finally {
            em.close();
        }
    }

    public void deleteById(long id){
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        try{
            Category category = em.find(Category.class, id);
            if  (category != null) {
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.remove(category);
                transaction.commit();
            }else {
                throw new EntityNotFoundException("Category with id " + id + " not found");
            }
        }finally {
            em.close();
        }
    }
    
}
