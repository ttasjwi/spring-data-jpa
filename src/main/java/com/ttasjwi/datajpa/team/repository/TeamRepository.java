package com.ttasjwi.datajpa.team.repository;

import com.ttasjwi.datajpa.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
