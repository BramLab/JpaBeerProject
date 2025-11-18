package repository;

import jakarta.persistence.EntityManager;

import java.util.List;

public interface GenericRepository<T, ID> {
    //create(T entity)
    //• findById(Long id)
    //• findAll()
    //• update(T entity)
    //• delete(Long id)
    T create(EntityManager entityManager, T entity);
    T findById(EntityManager entityManager, ID id);
    List<T> findAll(EntityManager entityManager);
    T update(EntityManager entityManager, T entity);
    void delete(EntityManager entityManager, T entity);
    void deleteById(EntityManager entityManager, ID id);
}
