package pt.psoft.g1.psoftg1.shared.repositories;

import java.util.List;

public interface CRUDRepository<T, ID> {
    T save(T entity);
    void delete(T entity);
    List<T> findAll();
    T findById(ID id);
}