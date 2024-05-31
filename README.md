# 둘레둘레

## 프로젝트 개요
'둘레둘레'는 국내 여행 계획을 세우는 플래너 기능과 이를 토대로 작성한 리뷰를 공유하는 리뷰 포스트 기능을 중심으로 하는 SNS 프로젝트입니다.
 코로나 이후 급격히 늘어난 여행 수요에 대응할 수 있도록 **다채로운 국내 관광지 정보를 소개**하고 **손쉬운 여행 계획 수립과 공유 기능을 제공**하며, 사용자들에게 친숙한 **SNS 스타일의 리뷰 작성 기능을 제공**하는 것으로 **국내 여행 소비를 촉진하는 것을 목적**으로 하고 있습니다.

## 프로젝트 기간
- 2024년 3월 4일 ~ 2024 년 4월 19일

## 개발환경
- 개발 언어: <img src="https://img.shields.io/badge/Language-Java17-1E8CBE?style=flat&logo=&logoColor=white">&nbsp;
- 개발 환경: <img src="https://img.shields.io/badge/Framework-SpringBoot3.2-6DB33F?style=flat&logo=springboot&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Spring data JPA-Spring data JPA-6DB33F?style=flat&logo=spring&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Spring Security-Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Gradle-Gradle-02303A?style=flat&logo=gradle&logoColor=white">&nbsp; 
- DB : <img src="https://img.shields.io/badge/Database-MariaDB-003545?style=flat&logo=mariadb&logoColor=white">
- 협업툴 : <img src="https://img.shields.io/badge/GitHub-GitHub-000000?style=flat&logo=github&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Figma-Figma-F24E1E?style=flat&logo=figma&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/Discord-Discord-5865F2?style=flat&logo=discord&logoColor=white">
- 라이브러리 / API : <img src="https://img.shields.io/badge/Library-Bootstrap-563D7C?style=flat&logo=bootstrap">&nbsp; <img src="https://img.shields.io/badge/Library-JQuery UI-0769AD?style=flat&logo=jquery&logoColor=white">&nbsp; <img src="https://img.shields.io/badge/Library-flatpickr-4A90D9?style=flat&logo=flatpickr&logoColor=white">&nbsp; <img src="https://img.shields.io/badge/API-국문_관광_정보-669DF6?style=flat">&nbsp; <img src="https://img.shields.io/badge/API-T_Map-red?style=flat">&nbsp;
  
## 프로젝트 ERD
![DulleDulle-ERD](https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/d7821149-964f-4756-8e61-ec1e84f31676)

## 프로젝트 기능 구조

![DulleDulle-프로젝트 구조](https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/8500ed0e-9995-4af8-9135-c8c2a0fd9fab)

<img width="4896" alt="DulleDulle-FlowChart" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/0994417c-a361-4516-b06e-27b5a58f491a">


## 프로젝트 기능 소개

### 메인 화면(피드)

<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/1af860fc-4d92-469f-a3da-b47e71cd4e19"/>
<p>- 추천 리뷰 피드와 팔로우 리뷰 피드를 제공<br>
- 다른 사람의 리뷰에 댓글과 답글을 달 수 있음</p>

**추천 리뷰 기준**

 - 전체 유저 대상 / 기준 시간(메인 화면 로드 시 or 피드 탭 전환 시 마다 설정) 72시간 사이에 관심등록이 많은 순 / 전체 기간 중 관심등록이 많은 순 / 최신 작성 순으로 우선하여 정렬

**팔로우 리뷰 기준**
 - 로그인 중인 유저가 팔로우 중인 유저와 본인 대상 / 기준 시간 이전에 작성된 리뷰를 최신 순으로 정렬



### 회원가입 및 로그인
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/03061ef3-4952-459b-b3e6-c9d461af2b7e"/>
<p>- Spring Security와 암호화를 사용하여 보안을 높이고, 로그인은 세션방식으로 구현<br>
- 회원가입 시 아이디 중복 검사와 비밀번호 확인, 이메일로 인증번호를 받아 인증하기를 거쳐야 가입이 가능</p>

### 프로필 변경, 개인정보 수정
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/3590fa51-1055-4c4c-b6f2-5aa90a40c0b9"/>

<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/cde8cbf9-ac67-474e-b38a-2a60d8c88580"/>
<p>- 마이페이지에서 내 프로필을 변경하거나 개인정보를 수정</p>

### 여행 정보
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/d83483ac-997b-44df-990a-6f9a9aa2a07e"/>
<p>- 한국관광공사 API를 이용하여 여행 정보를 제공</p>
&nbsp;
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/d78ac3ed-f5dc-43ac-974d-c81c2bec9440"/>
<p>- 지역, 카테고리, 키워드 등의 검색 옵션으로 관광지를 검색하여 목록을 조회<br>
- 관광지 상세페이지에서 동일 카테고리의 관광지들을 거리순으로 보여줌
</p>
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/63343632-2c9b-4317-8325-51fc071db918"/>
<p>- 북마크한 관광지는 마이페이지에서 확인 가능</p>

### 여행 일정
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/2644da86-1a82-4448-a58c-c5690a04ca51"/>
<p>- 날짜를 선택하고 관광지를 추가하여 여행일정을 작성<br>
- TMap 경유지 최적화 API를 사용하여 여행경로를 제공</p>

<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/057f1643-b35f-45ac-8d31-5b0141d60f65"/>
<p>- 북마크한 관광지만 따로 추가 가능<br>
- JQuery UI Sortable을 사용하여 드래그 앤 드롭으로 순서 변경 제공</p>
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/3251d918-f94c-444b-8c3b-bbea4e02560f"/>
<p>- 여행일정을 다른 유저에게 공유 가능</p>


### 마이페이지
<p>- 각 탭에 따라 현재 페이지의 유저가 작성한 리뷰 및 리뷰북, 등록한 관심리뷰와 북마크 목록을 조회<br>
- 다른 유저의 페이지에 방문 시 팔로우 가능, 팔로우 관계 조회 가능</p>

#### 리뷰북
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/336e5e42-4094-47ce-bc7f-f09547e6c95c"/>

<p>- 동일한 여행일정에 포함된 관광지 리뷰를 모아서 확인할 수 있는 기능. 여행 일정 선택, 커버 이미지 설정, 제목 및 상세정보 입력 가능</p>

#### 리뷰
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/598e87b1-c91c-48c9-a2be-adbaaaba275b"/>

<p>- 여행 계획(플래너)를 바탕으로 새 리뷰북을 생성하여 리뷰를 추가하거나, 이미 생성된 기존 리뷰북에 리뷰를 추가하거나, 리뷰북을 선택하지 않고 리뷰를 작성하는 방법 제공
</p>

#### 팔로우
<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/97068c87-9e94-4fb0-ac7f-b7fc47c63cd4"/>
<p>- 팔로우한 유저가 작성한 리뷰를 메인화면의 팔로우 피드에서 확인 가능</p>

### 검색
**유저검색**

<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/6d745777-60e0-4a85-90fa-74f45d8f3349"/>

**리뷰검색**

<img width="640px" src="https://github.com/MJKIM9403/ThreeGoNew/assets/145132400/6e160e40-c3e8-4466-8bbb-eef8b056e25b"/>
<p>- 리뷰와 유저를 검색 가능</p>


## 레퍼런스
UI
- https://codepen.io/GeorgePark/pen/VXrwOP
- https://www.myro.co.kr/

