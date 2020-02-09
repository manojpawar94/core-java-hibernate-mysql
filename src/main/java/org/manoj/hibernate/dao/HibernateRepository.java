package org.manoj.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.manoj.hibernate.utils.HibernateUtils;

public abstract class HibernateRepository<T, ID extends Serializable> implements Repository<T, ID> {

    private Optional<Session> session = Optional.empty();
    private Transaction currentTransaction;
    private boolean isInternalSession = false;
    private Class<T> classTypex;

    public Session openSessionWithoutTransaction() {
        setSession(Optional.ofNullable(HibernateUtils.getSession()));
        return getSession().get();
    }

    public Session openSessionWithTransaction() {
        setSession(Optional.ofNullable(HibernateUtils.getSession()));
        setCurrentTransaction(getCurrentSession().beginTransaction());
        return getSession().get();
    }

    public void closeSessionWithoutTransaction() {
        getCurrentSession().close();
    }

    public void closeSessionWithTransaction() {
        getCurrentTransaction().commit();
        getCurrentSession().close();
    }

    public void closeSessionWithRollbackTransaction() {
        getCurrentTransaction().rollback();
        getCurrentSession().close();
    }

    @Override
    public T findById(final ID id) {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            final T entity = getCurrentSession().find(getClassTypex(), id);
            return entity;
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            final List<T> entities = getCurrentSession().createQuery("from " + getClassTypex().getName()).list();
            return entities;
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }

    }

    @Override
    public void save(final T entity) {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            getCurrentSession().save(entity);
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }
    }

    @Override
    public void update(final T entity) {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            getCurrentSession().update(entity);
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }
    }

    @Override
    public void saveOrUpdate(final T entity) {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            getCurrentSession().saveOrUpdate(entity);
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }
    }

    @Override
    public void delete(final T entity) {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            getCurrentSession().delete(entity);
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }
    }

    @Override
    public void deleteAll() {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }

            final List<T> ts = findAll();
            ts.stream().forEach((entity) -> delete(entity));
        } catch (Exception e) {
            if (isInternalSession()) {
                closeSessionWithRollbackTransaction();
                setInternalSession(false);
            }
            throw e;
        } finally {
            if (isInternalSession()) {
                closeSessionWithTransaction();
                setInternalSession(false);
            }
        }
    }

    public Session getCurrentSession() {
        return getSession().get();
    }

    public void setCurrentSession(final Session currentSession) {
        setSession(Optional.of(currentSession));
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(final Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    protected Optional<Session> getSession() {
        return session;
    }

    private void setSession(final Optional<Session> session) {
        this.session = session;
    }

    public boolean isInternalSession() {
        return isInternalSession;
    }

    public void setInternalSession(final boolean isInternalSession) {
        this.isInternalSession = isInternalSession;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getClassTypex() {
        if (classTypex == null) {
            Type mySuperclass = getClass().getGenericSuperclass();
            Type tType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[0];
            String className = tType.toString().split(" ")[1];
            try {
                classTypex = (Class<T>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classTypex;
    }

}