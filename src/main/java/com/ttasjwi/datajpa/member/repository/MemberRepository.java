package com.ttasjwi.datajpa.member.repository;

import com.ttasjwi.datajpa.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameAndAgeGreaterThan(String name, int age);

}
