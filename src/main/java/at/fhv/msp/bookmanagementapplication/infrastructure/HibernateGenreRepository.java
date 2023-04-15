package at.fhv.msp.bookmanagementapplication.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateGenreRepository implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAllGenres() {
        TypedQuery<Genre> query = this.em.createQuery("SELECT g from Genre g", Genre.class);
        return query.getResultList();
    }
}
