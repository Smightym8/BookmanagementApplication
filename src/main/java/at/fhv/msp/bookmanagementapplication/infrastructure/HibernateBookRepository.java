package at.fhv.msp.bookmanagementapplication.infrastructure;


import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HibernateBookRepository implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAllBooks() {
        TypedQuery<Book> query = this.em.createQuery("SELECT b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        TypedQuery<Book> query = this.em.createQuery("FROM Book as b WHERE b.bookId = :bookId", Book.class);
        query.setParameter("bookId", id);

        return query.getResultStream().findFirst();
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        TypedQuery<Book> query = this.em.createQuery("FROM Book as b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);

        return query.getResultStream().findFirst();
    }

    @Override
    public void add(Book book) {
        this.em.persist(book);
    }
}
