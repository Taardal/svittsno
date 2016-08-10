package no.svitts.core.repository;

import no.svitts.core.exception.RepositoryException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;

abstract class CoreRepository<T> implements Repository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreRepository.class);

    private SessionFactory sessionFactory;

    CoreRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            LOGGER.error("Could not get current session", e);
            throw new RepositoryException(e);
        }
    }

    CriteriaBuilder getCriteriaBuilder() {
        return getCurrentSession().getCriteriaBuilder();
    }
}
