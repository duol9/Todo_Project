# Spring 일정관리 앱
일정 생성, 조회, 수정, 삭제가 가능한(CRUD) 일정 관리 어플리케이션입니다.
- Lv1 : https://github.com/duol9/Todo_Project/tree/Lv1
- Lv2 : https://github.com/duol9/Todo_Project/tree/Lv2
     
## 개발환경
- Java17
- Spring Boot (3.4.0)
- JDBC
- MySQL (8.0.28)
- Intellij IDEA

## ERD

![image](https://github.com/user-attachments/assets/4196f3f2-2f59-46eb-a420-3b3a60bd053b)


## API 명세서

![image](https://github.com/user-attachments/assets/850aad7d-f9d5-4fd3-aa5a-fea590df90b5)

## API 명세서 상세설명
**일정 생성**
- 기능: 새로운 일정 추가 (`작성자명`, `비밀번호`, `할일`)
- 사용 시점: 클라이언트가 일정을 새로 추가하고 싶을 때

**전체 일정 조회**
- 기능: 등록된 일정 목록 조회
- 사용 시점
  - `수정일`, `작성자명`을 조건으로 등록된 일정 목록을 전부 조회하고 싶을 때
  - 모든 일정 목록을 조회하고 싶을 때
 
**선택 일정 조회**
- 기능: ID를 선택하여 일정 조회
- 사용 시점: 선택한 일정만 조회하고 싶을 때

**일정 수정**
- 기능: 선택한 ID의 일정 수정
- 사용 시점: `할일`, `작성자명`을 수정하고 싶을 때 

**일정 삭제**
- 기능: 선택한 ID의 일정 삭제
- 사용 시점: 특정 일정을 삭제하고 싶을 때




