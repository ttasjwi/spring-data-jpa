package com.ttasjwi.datajpa.team.repository;

import com.ttasjwi.datajpa.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("SELECT t FROM Team as t", Team.class)
                .getResultList();
    }

    public long count() {
        return em.createQuery("SELECT count(t) FROM Team as t", Long.class)
                .getSingleResult();
    }
}
