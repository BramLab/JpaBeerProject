package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {

    private final Class<T> entityClass;

    public GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T save(EntityManager entityManager, T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(EntityManager entityManager, T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(EntityManager entityManager, T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    public void deleteById(EntityManager entityManager, ID id) {
        T entity = findById(entityManager, id);
        if (entity != null) {
            delete(entityManager, entity);
        }
    }

    @Override
    public T findById(EntityManager entityManager, ID id) {
        T entity = entityManager.find(entityClass, id);
        System.out.println(Arrays.toString(entity.getClass().getDeclaredFields()));
        for  (Field field : entity.getClass().getDeclaredFields()) {
            System.out.println(field.getName() + Arrays.toString(field.getAnnotations()));
            if (Arrays.toString(field.getAnnotations()).contains("LAZY")){
                //int lazyLoadingHack = field
                // todo:
                https://stackoverflow.com/questions/51837798/get-associated-getter-setter-of-field-member-variable
            }
        }


        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll(EntityManager entityManager) {
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public long count(EntityManager entityManager) {
        CriteriaQuery<Long> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Long.class);
        criteriaQuery.select(entityManager.getCriteriaBuilder().count(criteriaQuery.from(entityClass)));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
