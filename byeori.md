# byeori 개발 일지
### 2022-12-31 진행사항
+ 기능 기획서 보드 작성
+ ERD 설계
+ RDS 데이터베이스 구축
+ EC2 인스턴스 구축
+ SSL 구축 (by Certbot)

### 2023-01-01 진행사항
+ ERD 수정
  + varchar fasle -> boolean false
  + 약관동의 넣을 service 테이블 생성
  + User 테이블에 loginStatus 추가
  + User 테이블의 userid -> userId 수정
  + User 테이블의 phone 컬럼 형식 int -> varchar(11) 로 변경
+ dev/prod 서버 구축
+ API 명세서 설계
+ 회원가입 API
+ 팔로우 API
  + 생성
<details markdown="1">
<summary>ERD</summary>

![instagram-rising-test](https://user-images.githubusercontent.com/94090893/210167880-f02503d1-63a8-4c03-ab5c-6ad612391884.png)

</details>

### 2023-01-02 진행사항
+ 회원가입 API 개발 완료 (by jwt)
+ 로그인 API
+ 회원탈퇴 API
  + User 테이블의 status : 'ACTIVE' -> status : 'INACTIVE' 변경
  + 추후 게시글, 좋아요, 댓글도 함께 상태 변경 예정
+ 로그아웃 API
  + User 테이블의 inActive : '1' -> inActive : '2' 변경
 
### 2023-01-03 진행사항
+ 회원가입 API 수정
  + 휴대폰 번호 및 이메일 관련 vaildation 수정
  + 생년월일 추가 
+ 로그인 API 수정
  + 로그인 시 User 테이블의  loginStatus 'ACTIVE' 로 변경, inActive : 활동상태 컬럼, 로그인 시 변경 X
+ 로그인 API 추가
  + 로그아웃 시 User 테이블의 loginStatus 'INACTIVE' 로 변경
+ 팔로우 API 생성 및 개발 완료
  + Follow 테이블의 followingIdx : 팔로우 신청하는 사람, followerIdx : 팔로우 당한 사람
+ 팔로워 API 생성 및 개발 완료
+ 팔로잉 API 생성 및 개발 완료

### 2023-01-04 진행사항
+ ERD 수정
  + 'Audio', 'Tag', 'Location' 테이블 추가
+ 팔로우 API 수정
  + 팔로우 하기 API 와 팔로우 삭제 API 통합
+ 검색 API 생성
+ 1차 피드백
  + 기획서 보충 : 서버 개발자 역할 분담 section
  + DM 테이블에 content column 추가
  + API 명세서 보충 : 인수인계사항 추가

### 2023-01-05 진행사항
+ ERD 수정
  +  'Audio': audioImg -> audioImgUrl 컬럼명 변경  
  +  'Tag': tagImgUrl 컬럼 생성 
  +  'Location' locationImgUrl 컬럼 생성
+ 유저 / 태그 / 장소 검색 API 개발 완료

### 2023-01-06 진행사항
+ 최근 검색 기록 조회 API 개발 완료
+ 최근 검색 기록 삭제 API
  + RequestParam O : 하나의 검색어 삭제
  + RequestParam X : 검색기록 모두 삭제

### 2023-01-07 진행사항
+ 비밀번호 변경 API 개발 완료
  + 팔로워 조회 API 수정
    - 팔로우 유무 상태 값 추가
  + 팔로잉 조회 API 수정
    - 팔로우 유무 상태 값 추가
+ ERD 수정
  + DM 테이블의 content (대화내용) 추가
<details markdown="1">
<summary>ERD</summary>

![Untitled](https://user-images.githubusercontent.com/94090893/211157373-c07d67e5-3409-4c84-a748-0045c29340f8.png)

</details>

### 2023-01-08 진행사항
+ 추천친구 조회 API 개발 완료
  + 추천친구 기준
    +   1) 상대는 나를 팔로우 했지만 나는 팔로우 안한 사람
    +   2) 내가 팔로우 한 사람들 중 2명 이상이 팔로우 한 사람
+ 함께 아는 친구 조회 API 개발 완료
  + 함께 아는 친구 기준
    +   내가 팔로우 한 사람을 내 팔로워도 팔로우 한 경우
+ ERD 수정
  +  Story -> add column content varchar(300)
  +  StoryTag -> add column locationIdx int
  +  StoryTag -> modify column userIdx null 허용
<details markdown="1">
<summary>ERD</summary>

![Untitled (1)](https://user-images.githubusercontent.com/94090893/211203019-842b5180-465f-42e6-96dd-ff0f9a2cd6f9.png)

</details>

+ 스토리 작성 API 개발 완료 (/stories/:userIdx)
+ 스토리 조회 API 개발 완료 (/stories/:userIdx/:storyIdx)

### 2023-01-09 진행사항
+ 스토리 삭제 API 개발 완료 (/stories/:userIdx/:storyIdx)
+ 24시간 지난 스토리 삭제 API 개발 완료 (/stories/:userIdx/:storyIdx ? expiration =)
+ 스토리 좋아요 API 개발 완료 (/stories/likes/:userIdx/:storyIdx)

### 2023-01-10 진행사항
+ 팔로워 & 팔로잉 조회 API 권한 설정 수정
+ 로그아웃 & 회원탈퇴 & 최근검색기록조회 & 최근검색기록삭제 jwt 추가
+ 스토리 좋아요 / 삭제 API 개발 완료 (/stories/likes/:userIdx)
+ 스토리 신고 API 개발 완료 (/stories/reports/:userIdx)
+ 스토리 조회 유저 리스트 API 개발 완료 (/stories/users/:userIdx/:storyIdx)
+ 아이디 변경 API 개발완료 (/users/:userIdx/userId)
+ ERD 수정
  +  StoryReport 테이블 추가
  +  StoryViewer 테이블 추가
<details markdown="1">
<summary>ERD</summary>

![Untitled (2)](https://user-images.githubusercontent.com/94090893/211568559-5c24511a-5baf-4ffb-911b-73ab85838f75.png)

</details>

### 2023-01-11 진행사항
+ 유저 스토리 전체 조회 API 개발 완료 (/stories/:userIdx)
+ 하이라이트 추가 API 개발 완료 (/highlights/:userIdx)
+ 하이라이트 조회 API MVC 생성 (/highlights/:userIdx)
+ ERD 수정
  + Behind 테이블 추가
  + Highlight 테이블 추가
 <details markdown="1">
<summary>ERD</summary>

![Untitled (3)](https://user-images.githubusercontent.com/94090893/211819172-a2baa703-e642-4c74-8b82-6337612ba9cb.png)

</details>

### 2023-01-12 진행사항
+ 로그아웃 API 수정
  + uri 오류 수정 : /:userIdx/logOut -> /{userIdx}/logOut
+ 하이라이트 전체 조회 API 수정
  + 하이라이트 1개 조회 -> 하이라이트 전체 조회 수정
+ 회원탈퇴 API jwt 추가
+ 최근 검색 기록 삭제 API jwt 추가
+ 스토리 조회 API jwt 제거
+ 스토리 삭제 API 수정

+ 
