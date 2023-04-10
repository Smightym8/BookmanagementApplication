package at.fhv.msp.bookmanagementapplication.infrastructure;


import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateBookRepository implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAllBooks() {
        TypedQuery<Book> query = this.em.createQuery("SELECT b from Book b", Book.class);
        return query.getResultList();
    }
}
