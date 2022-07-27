
## Spring-Data-Jpa

- 인프런 김영한 님의 '실전! 스프링 데이터 JPA'를 학습한 내용 기록

---

## JpaRepository 인터페이스의 기능
- Spring-Data-Jpa가 구현 클래스(프록시)를 대신 생성
- `@Repository` 어노테이션을 생략 가능
  - 컴포넌트 스캔을 Jpa가 자동으로 처리
  - JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리

---

## 메서드 이름으로 쿼리 생성
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameAndAgeGreaterThan(String name, int age);

}
```
컴파일 시 오류 발생 체크 가능
- 조회 : find...By, read...By, get...By
  - findHelloBy와 같이 식별하기 위한 내용, 설명이 들어가기도 함.
- 갯수 : count...By
- exists : exists...By (boolean)
- 삭제 : delete...By, remove...By
- distinct : 중복 제거
- limit : findLimit3, findTop, findTop3, ...

---

## NamedQuery
```java
@NamedQuery(
        name = "Member.findByName",
        query = "SELECT m FROM Member as m WHERE m.name = :name"
)

public class Member {
```
- NamedQuery의 강점 : 애플리케이션 로딩 시점에 파싱을 하기 때문에 컴파일 에러 감지가 가능

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    //생략
  
    @Query(name = "Member.findByName")
    List<Member> findByName(@Param("name") String name);
}
```
- 리포지토리에서 NamedQuery를 호출하여 사용 가능
```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // @Query(name = "Member.findByName")
    List<Member> findByName(@Param("name") String name);
}
```
- 사실 `@Query`를 생략해도 작동이 됨
  - 타입, 메서드명을 기반으로 NamedQuery를 조회 (우선순위 높다)
  - 없으면 메서드명을 기반으로 Query 생성

---

### `@Query` : 리포지토리 메소드에 쿼리 정의하기
```java
    @Query("SELECT m FROM Member as m Where m.name = :name and m.age = :age")
    List<Member> findMember(@Param("name") String name, @Param("age") int age);
```
- 쿼리를 직접 레포지토리 인터페이스 메서드의 선언부에 작성
- 애플리케이션 로딩 시점에 문법 오류, 이상한 변수 선언이 있을 경우 감지하여 예외 발생

---

### `@Query` : 값/값타입, DTO 조회
```java
    @Query("SELECT m.name FROM Member as m")
    List<String> findMemberNameList();

    @Query("SELECT new com.ttasjwi.datajpa.member.dto.MemberDto(m.id, m.name, t.name) " +
            "FROM Member as m " +
            "JOIN m.team as t")
    List<MemberDto> findMemberDto();
```
- 엔티티 뿐 아니라, 단순 값 조회 가능
  - 값타입 조회도 가능하다.
- Dto로 조회 가능
  - new 명령어 사용해야함.
  - 실제 DTO의 FQCN을 알아야함(도중에 경로가 바뀌면 다시 수정해야함)
  - 딱 맞는 순서의 DTO 생성자가 있어야함.
  - IDE의 도움을 받기 매우 힘들다...
  - 불편함이 많긴한데 이 부분은 Querydsl을 사용하면 편리해진다.

---

## 파라미터 바인딩
- 위치 기반 : 순서가 바뀔 때 위험해짐.
- 이름 기반 : 코드 가독성, 유지보수면에서 이름 기반 쿼리가 낫다.
  - 컬렉션 파라미터 바인딩도 가능하다 (in절...)

---
