# ari 개발 일지

## 🌱 2022-12-31 진행상황
* 라이징 테스트 OT 세미나 참여
* 기능 계획서 작성
* github 커밋 규칙
* ERD 설계
<details>
<summary>ERD</summary>
<div markdown="1">

![instagram-rising-test](https://user-images.githubusercontent.com/90203250/210132729-ad541d6c-fde1-4359-b7b9-7d9b1033ba87.png)

</div>
</details>
<br>

## 🌱 2023-01-01 진행상황
* 회의 : 1주차 기능 역할 분담
* 게시물 API 생성
  * 게시물 작성
  * 게시물 조회
 
* API SHEET 생성
* ERD 수정
  * VARCHAR false -> boolean false
  * 가입 약관 DB
  * User Table: Login status Column 추가
  * User Table: phone (int -> varchar(11)
  * User Table: userid -> userId
<details>
<summary>ERD</summary>
<div markdown="1">

![instagram-rising-test (1)](https://user-images.githubusercontent.com/90203250/210170596-dc259421-115a-4527-836c-433dc8fb3ece.png)

</div>
</details>
<br>

## 🌱 2023-01-02 진행상황
* 게시물 API 생성
  * 게시물 수정
  * 게시물 삭제
* 프로필 API 생성
  * 프로필 MVC 패턴 구조 생성
* API SHEET 작성 
  * 2023-01-01 에서 생성한 API 작성
* AWS EC2, RDS 구축
  * AWS EC2: ubuntu 20.04
  * RDS: mysql

<br>

## 🌱 2023-01-03 진행상황
* 프로필 API 생성
  * 프로필 편집 조회
  * 프로필 편집
  * 프로필 조회
* 게시물 API 생성
  * 피드
* API SHEET 작성
  * '2023-01-02 ~ 2023-01-03' 에서 생성한 API 작성
* SERVER
  * DOMAIN 연결
  * PROXY SERVER 연결
  * dev/prod 서버 구축
  * SSL 구축
* API 서버 반영

<br>

## 🌱 2023-01-04 진행상황
* 게시물 API 변경
  * 피드 조회 API -> 댓글 개수 추가
  * 피드 업데이트 순으로 변경 (오래된 순 -> 최신 순)
* 댓글 API 생성
  * 댓글 MVC 패턴 구조 생성
  * 댓글 작성 API 생성
* 에릭: 1차 멘토링 진행
```

1. 기획서 내에 있는 세부 서버 개발자 역할 분담을 채우기
 시간 날 때 자신이 생성한 API를 각각 작성
2. 서버 구축은 문제가 없음
3. ERD 설계의 경우 POST에 위치 정보를 추가하는 것이 좋을 것
  시간이 남으면 상점, 릴스와 같은 테이블도 생성 필요
4. 시간이 남으면 프론트에서 필요로하는 필수 API 외에도 또 만들면 좋을 것

이미지 업로드 부분은 프론트와 상의 후 결정하는 것으로!
```

* ERD 수정
  * 댓글 좋아요 ERD 생성
  * GITHUB DDL에도 추가
<details>
<summary>ERD</summary>
<div markdown="1">

![instagram-rising-test](https://user-images.githubusercontent.com/90203250/210481529-b17bc56c-6605-4dc1-9634-874bf2101af3.png)

</div>
</details>

* API 서버 반영
<br>

## 🌱 2023-01-05 진행상황
* 댓글 API 생성
  * 댓글 삭제
  * 댓글 수정
* API 서버 반영
<br>

## 🌱 2023-01-06 진행상황
* API SHEET 작성
  * '2023-01-05' 에서 생성한 API 작성
* 댓글 API 수정
  * 댓글 좋아요 예외 처리 추가
* 게시글 API 수정
  * 게시글 조회 API -> 댓글 좋아요 개수 추가
* API 서버 반영
<br>

## 🌱 2023-01-07 진행상황
* 게시글 API 생성
  * 게시글 좋아요한 사용자 리스트 API 
* 댓글 API 생성
  * 댓글 좋아요한 사용자 리스트 API
* API SHEET 작성
  * '2023-01-07' 에서 생성한 API 작성
* API 서버 반영
<br>

## 🌱 2023-01-08 진행상황
* 프로필 API 수정
  * 프로필 조회 API 팔로우 상태 추가
* 댓글 API 수정
  * 댓글 좋아요 리스트 조회 API 팔로우 상태 추가
* 게시글 API 수정
  * 게시글 좋아요 리스트 조회 API 팔로우 상태 추가
* API SHEET 작성
  * '2023-01-08' 에서 수정한 API 작성
* API 서버 반영

## 🌱 2023-01-09 진행상황
+ ERD 수정
<details markdown="1">
<summary>ERD</summary>

![Untitled (1)](https://user-images.githubusercontent.com/94090893/211203019-842b5180-465f-42e6-96dd-ff0f9a2cd6f9.png)

</details>

* 게시글 API 생성
  * 알고리즘 게시글 API 생성
  * 알고리즘 게시글 피드 API 생성 -> 알고리즘 게시글을 클릭했을 때 보이는 피드들
* API SHEET 작성
  * '2023-01-09' 에서 수정한 API 작성

<br>

## 🌱 2023-01-10 진행상황
* 게시물 API
  * 게시물 조회 -> 댓글 업데이트 시간 추가 수정
  * 게시물 좋아요 API 생성
  * 게시물 신고 API 생성
* 프로필 API
  * 개인 정보 설정 조회 API 생성
  * 개인 정보 설정 변경 API 생성
  * 이 계정 정보 API 생성
* API SHEET 작성
  * '2023-01-10' 에서 생성/수정한 API 작성
* 에릭 멘토링
* ERD 변경
  * USER COLUMN SEX(성별), NATION(국가) 추가
  * POSTREPORT TABLE 생성
<details markdown="1">
<summary>ERD</summary>

![Untitled (2)](https://user-images.githubusercontent.com/94090893/211568559-5c24511a-5baf-4ffb-911b-73ab85838f75.png)

</details>

## 🌱 2023-01-11 진행상황
* 댓글 API
  * 댓글 신고 API 생성
  * 댓글 좋아요 List API에 username 생성
* 게시물 API 
  * 사용자가 좋아요 한 게시물 API (최신순, 오래된 순)
  * 게시물 좋아요 List API에 username 생성
* API SHEET 작성
  * '2023-01-11' 에서 생성한 API 작성
* ERD 수정
  * CommentReport Table 추가
  * PostSave Table 추가
<details markdown="1">
<summary>ERD</summary>

![instagram-rising-test (1)](https://user-images.githubusercontent.com/90203250/211740228-c073cb0c-776d-44d0-8bcc-72bd04c5f984.png)


</details>


## 🌱 2023-01-12 진행상황
* 게시글 API
  * 게시글 저장 API 생성
  * 게시글 저장 리스트 API 생성
* API SHEET 작성
  * '2023-01-12' 에서 생성한 API 작성
* 최종 ERD
 <details markdown="1">
<summary>ERD</summary>

![Untitled (3)](https://user-images.githubusercontent.com/94090893/211819172-a2baa703-e642-4c74-8b82-6337612ba9cb.png)

</details>