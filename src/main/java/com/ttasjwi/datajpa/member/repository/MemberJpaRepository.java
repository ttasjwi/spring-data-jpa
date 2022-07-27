package com.ttasjwi.datajpa.member.repository;

import com.ttasjwi.datajpa.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member as m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public long count() {
        return em.createQuery("SELECT count(m) FROM Member as m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByNameAndAgeGreaterThan(String name, int age) {
        return em.createQuery(
                        "select m " +
                                "From Member as m " +
                                "Where m.name = :name and m.age > :age", Member.class)
                .setParameter("name", name)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createNamedQuery("Member.findByName", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    /**
     * 1. 검색 : 나이로 검색
     * 2. 정렬 : 이름으로 내림차순
     * 3. 페이징 : offset부터 시작해서 limit만큼 잘라서
     */
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery(
                        "SELECT m " +
                                "FROM Member as m " +
                                "WHERE m.age = :age " +
                                "ORDER BY m.name desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 해당 나이의 총 회원 수
     */
    public long totalCountByAge(int age) {
        return em.createQuery(
                "SELECT count(m) " +
                        "FROM Member as m " +
                        "WHERE m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
