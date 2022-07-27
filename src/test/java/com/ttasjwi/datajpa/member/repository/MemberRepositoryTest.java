package com.ttasjwi.datajpa.member.repository;

import com.ttasjwi.datajpa.member.domain.Member;
import com.ttasjwi.datajpa.member.dto.MemberDto;
import com.ttasjwi.datajpa.team.domain.Team;
import com.ttasjwi.datajpa.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isSameAs(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByNameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getName()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByName("AAA");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    public void testQuery() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findMember("AAA", 10);


        //then
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findMemberNameList() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        //then
        List<String> memberNames = memberRepository.findMemberNameList();

        for (String memberName : memberNames) {
            log.info("memberName = {}", memberName);
        }
        assertThat(memberNames.size()).isEqualTo(2);
        assertThat(memberNames).containsExactly("AAA", "BBB");
    }

    @Test
    public void findMemberDto() throws Exception {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        for (MemberDto memberDto : memberDtos) {
            log.info("memberDto = {}", memberDto);
        }
    }

    @Test
    public void findByNames() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        //then
        List<Member> result = memberRepository.findByNames(List.of("AAA", "BBB"));
        for (Member member : result) {
            log.info("member = {}", member);
        }
    }

    /**
     * 다양한 반환타입을 지원한다.
     */
    @Test
    public void returnTypes() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result1 = memberRepository.findListByName("AAA");
        log.info("result1 = {}", result1); // "AAA"
        assertThat(result1).extracting("name").containsExactly("AAA");

        List<Member> result2 = memberRepository.findListByName("DDD");
        log.info("result2 = {}", result2); // 빈 컬렉션
        assertThat(result2).isEmpty();

        Member result3 = memberRepository.findOptionalByName("BBB").get();
        log.info("result3 = {}", result3); // "BBB"
        assertThat(result3.getName()).isEqualTo("BBB");

        Member result4 = memberRepository.findMemberByName("DDD");
        log.info("result3 = {}", result4); // null 반환!
        assertThat(result4).isNull();
    }

    @Test
    public void paging_page() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();

        for (Member member : content) {
            log.info("member = {}", member);
        }
        log.info("totalCount = {}", totalCount);

        assertThat(content).extracting("name").containsExactly("member5", "member4", "member3");
        assertThat(content.size()).isEqualTo(3);

        assertThat(page.getTotalElements()).isEqualTo(5); // 전체
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
        assertThat(page.hasPrevious()).isFalse();
        assertThat(page.isLast()).isFalse();
    }

    @Test
    public void paging_slice() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        // when
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);

        // then
        List<Member> content = slice.getContent();
//        long totalCount = slice.getTotalElements();

        for (Member member : content) {
            log.info("member = {}", member);
        }
//        log.info("totalCount = {}", totalCount);

        assertThat(content).extracting("name").containsExactly("member5", "member4", "member3");
        assertThat(content.size()).isEqualTo(3);

//        assertThat(slice.getTotalElements()).isEqualTo(5);
        assertThat(slice.getNumber()).isEqualTo(0);
//        assertThat(slice.getTotalPages()).isEqualTo(2);
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();
        assertThat(slice.hasPrevious()).isFalse();
        assertThat(slice.isLast()).isFalse();
    }

    @Test
    public void paging_list() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        // when
        List<Member> content = memberRepository.findListByAge(age, pageRequest);

        for (Member member : content) {
            log.info("member = {}", member);
        }

        assertThat(content).extracting("name").containsExactly("member5", "member4", "member3");
        assertThat(content.size()).isEqualTo(3);

//        assertThat(page.getTotalElements()).isEqualTo(5); // 전체
//        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
//        assertThat(page.isFirst()).isTrue();
//        assertThat(page.hasNext()).isTrue();
//        assertThat(page.hasPrevious()).isFalse();
//        assertThat(page.isLast()).isFalse();
    }
}
