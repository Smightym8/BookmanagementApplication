package at.fhv.msp.bookmanagementapplication.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HibernateAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> findAllAuthors() {
        TypedQuery<Author> query = this.em.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findAuthorById(Long id) {
        TypedQuery<Author> query = this.em.createQuery("FROM Author AS a WHERE a.authorId = :authorId", Author.class);
        query.setParameter("authorId", id);

        return query.getResultStream().findFirst();
    }

    @Override
    public void add(Author author) {
        this.em.persist(author);
    }
}
