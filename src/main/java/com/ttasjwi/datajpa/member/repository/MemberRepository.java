package com.ttasjwi.datajpa.member.repository;

import com.ttasjwi.datajpa.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
