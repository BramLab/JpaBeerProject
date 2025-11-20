package repository;

import app.FeedbackToUserException;
import config.JpaConfig;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import model.Brewer;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static config.JpaConfig.getEntityManager;

public class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {

    private Class<T> entityClass;

    public GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
//
//    public void setEntityClass(Class<T> entityClass) {
//        this.entityClass = entityClass;
//    }

    public GenericRepositoryImpl(){}

    @Override
    public T create(EntityManager entityManager, T entity) {
        // Create model happens independently in this app, so transaction can happen here in repo.
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return entity;
    }

    @Override
    public T findById(EntityManager entityManager, ID id) {
        T entity = entityManager.find(getEntityClass(), id);
        return entity;

//        String queryString = "SELECT u FROM " + getEntityClass().getSimpleName() + " u WHERE u.id=:id";
//        TypedQuery<T> typedQuery = entityManager.createQuery(queryString, getEntityClass());
//        typedQuery.setParameter("id", id);
//        if  (!typedQuery.getResultList().isEmpty()) {
//            return entityManager.find(getEntityClass(), id);
//        } else {
//            return null;
//        }
    }

    @Override
    public T findById_lazyLoadingHack(EntityManager entityManager, ID id) {
        T entity = entityManager.find(getEntityClass(), id);

        // https://stackoverflow.com/questions/51837798/get-associated-getter-setter-of-field-member-variable
        // Claude.ai can simplify this (see info.txt), but this is how i wrote it.
        if(entity != null){
            for  (Field field : entity.getClass().getDeclaredFields()) {
                if (Arrays.toString(field.getAnnotations()).contains("LAZY")){
                    String probableGetterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

                    for (Method method : entity.getClass().getDeclaredMethods()) {
                        if ( (method.getName()).equals(probableGetterName)) {
                            try {
                                List result = (List<Object>) method.invoke(entity);
                                int lazyLoadingHack = result.size();
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                }
            }
        }
        return entity;
    }

    @Override
    public List<T> findAll(EntityManager entityManager) {
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(getEntityClass());
        criteriaQuery.select(criteriaQuery.from(getEntityClass()));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public T update(EntityManager entityManager, T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(EntityManager entityManager, ID id) throws RollbackException, NoResultException {
        entityManager.getTransaction().begin();
        T entity = findById(entityManager, id);
        if (entity != null) {
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }else {
            entityManager.getTransaction().rollback();
            throw new NoResultException();
        }
    }

//    public boolean isDetached(EntityManager em, T entity) {
//        return entity.getId() != 0  // must not be transient
//                && !em.contains(entity)  // must not be managed now
//                && em.find(getEntityClass(), entity.getId()) != null;  // must not have been removed
//
//        try{
//            Method getId = entity.getClass().getDeclaredMethod("getId");
//            int entityId = (int) getId.invoke(entity);
//        } catch(NoSuchMethodException e){
//            //
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
