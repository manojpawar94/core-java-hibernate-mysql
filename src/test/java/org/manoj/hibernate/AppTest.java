package org.manoj.hibernate;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.manoj.hibernate.dao.EmployeeDao;
import org.manoj.hibernate.entity.Employee;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Ignore
    @Test
    public void testSaveMethod() {
        EmployeeDao dao = new EmployeeDao();
        long salary = 30000;
        Employee employee = new Employee("Rajesh Patil", "Software", salary, new Date());
        dao.save(employee);
    }

    @Test
    public void testFindByIdMethod() {
        EmployeeDao dao = new EmployeeDao();
        long id = 1;
        Employee employee = dao.findById(id);
        assertNotNull(employee);
        System.out.println(employee.toString());
    }

    @Test
    public void testFindAllMethod() {
        EmployeeDao dao = new EmployeeDao();
        try {
            List<Employee> employees = dao.findAll();
            assertNotNull(employees);
            employees.stream().forEach(employee -> System.out.println(employee.toString()));
        } catch (Exception e) {

        }
    }

    @Test
    public void testDeleteMethod() {
        EmployeeDao dao = new EmployeeDao();
        try {
            dao.openSessionWithTransaction();
            Employee employee = dao.findByName("Manoj Pawar");
            assertNotNull(employee);
            System.out.println(employee.toString());
            dao.delete(employee);
            List<Employee> employees = dao.findAll();
            employees.stream().forEach(employeeObj -> System.out.println(employeeObj.toString()));
            dao.closeSessionWithTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            dao.closeSessionWithRollbackTransaction();
        }
    }
}