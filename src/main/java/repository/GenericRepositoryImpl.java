package repository;

import app.FeedbackToUserException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.RollbackException;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {

    private final Class<T> entityClass;

    public GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

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
        T entity = entityManager.find(entityClass, id);

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
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public T update(EntityManager entityManager, T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(EntityManager entityManager, ID id) throws RollbackException, NoResultException {
        //EntityTransaction transaction = entityManager.getTransaction();
        //transaction.begin();
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

}
