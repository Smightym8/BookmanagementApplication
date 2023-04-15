package at.fhv.msp.bookmanagementapplication.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HibernateGenreRepository implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAllGenres() {
        TypedQuery<Genre> query = this.em.createQuery("SELECT g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        TypedQuery<Genre> query = this.em.createQuery("FROM Genre as g WHERE g.genreId = :genreId", Genre.class);
        query.setParameter("genreId", id);

        return query.getResultStream().findFirst();
    }
}
