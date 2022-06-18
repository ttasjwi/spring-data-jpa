
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

