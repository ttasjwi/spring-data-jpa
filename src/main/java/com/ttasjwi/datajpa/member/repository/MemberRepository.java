package com.ttasjwi.datajpa.member.repository;

import com.ttasjwi.datajpa.member.domain.Member;
import com.ttasjwi.datajpa.member.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameAndAgeGreaterThan(String name, int age);



    /**
     * 쿼리 찾기 순서
     * 1. 타입, 메서드명을 기반으로 NamedQuery를 조회 (우선순위 높다)
     * 2. 없으면 메서드명을 기반으로 Query 생성
     */
    //@Query(name = "Member.findByName")
    List<Member> findByName(@Param("name") String name);


    @Query("SELECT m FROM Member as m Where m.name = :name and m.age = :age")
    List<Member> findMember(@Param("name") String name, @Param("age") int age);

    @Query("SELECT m.name FROM Member as m")
    List<String> findMemberNameList();

    @Query("SELECT new com.ttasjwi.datajpa.member.dto.MemberDto(m.id, m.name, t.name) " +
            "FROM Member as m " +
            "JOIN m.team as t")
    List<MemberDto> findMemberDto();

    /**
     * 컬렉션 파라미터 바인딩 -> 이름 목록으로 회원 조회
     */
    @Query("SELECT m FROM Member as m WHERE m.name in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    Optional<Member> findOptionalByName(String name);
    Member findMemberByName(String name);
    List<Member> findListByName(String name);
}
