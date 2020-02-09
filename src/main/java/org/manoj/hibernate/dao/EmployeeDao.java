package org.manoj.hibernate.dao;

import org.manoj.hibernate.entity.Employee;

public class EmployeeDao extends HibernateRepository<Employee, Long> {

    public Employee findByName(String name) {
        try {
            if (!getSession().isPresent()) {
                openSessionWithTransaction();
                setInternalSession(true);
            }
            Employee employee = (Employee) getCurrentSession().createQuery("from Employee where name=:name")
                    .setParameter("name", name).getSingleResult();
            return employee;
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

}