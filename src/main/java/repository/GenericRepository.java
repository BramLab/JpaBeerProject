package repository;

import jakarta.persistence.EntityManager;

import java.util.List;

public interface GenericRepository<T, ID> {
    T save(EntityManager entityManager, T entity);
    T update(EntityManager entityManager, T entity);
    void delete(EntityManager entityManager, T entity);
    void deleteById(EntityManager entityManager, ID id);
    T findById(EntityManager entityManager, ID id);
    List<T> findAll(EntityManager entityManager);
    long count(EntityManager entityManager);
}
