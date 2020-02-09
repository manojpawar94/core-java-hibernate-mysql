package org.manoj.hibernate.dao;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    public T findById(ID id);
    public List<T> findAll();
    public void save(T entity);
    public void update(T entity);
    public void saveOrUpdate(T entity);
    public void delete(T entity);
    public void deleteAll();
}