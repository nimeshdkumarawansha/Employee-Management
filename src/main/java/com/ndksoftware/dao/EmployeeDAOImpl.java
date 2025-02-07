package com.ndksoftware.dao;

import com.ndksoftware.model.Employee;
import com.ndksoftware.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public Long create(Employee employee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Long id = (Long) session.save(employee);
            transaction.commit();
            return id;
        }
    }

    @Override
    public Employee findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    @Override
    public List<Employee> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaQuery<Employee> query = session.getCriteriaBuilder().createQuery(Employee.class);
            query.from(Employee.class);
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Employee> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.where(cb.like(root.get("name"), "%" + name + "%"));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.where(cb.equal(root.get("department"), department));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Employee> findByPosition(String position) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.where(cb.equal(root.get("position"), position));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Employee> findByHireDate(LocalDate hireDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.where(cb.equal(root.get("hireDate"), hireDate));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public void update(Employee employee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
            }
            transaction.commit();
        }
    }

    @Override
    public boolean exists(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            return employee != null;
        }
    }
}