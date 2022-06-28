package com.ttasjwi.datajpa.member.repository;

import com.ttasjwi.datajpa.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameAndAgeGreaterThan(String name, int age);



    /**
     * 쿼리 찾기 순서
     * 1. 타입, 메서드명을 기반으로 NamedQuery를 조회 (우선순위 높다)
     * 2. 없으면 메서드명을 기반으로 Query 생성
     */
    //@Query(name = "Member.findByName")
    List<Member> findByName(@Param("name") String name);

}
