> 게임 명장면을 짧은 영상으로 공유하고, 커뮤니티로 소통하는 웹 플랫폼

[![배포 URL](https://img.shields.io/badge/배포-www.gamecut.net-blue)](http://www.gamecut.net)

## 📋 프로젝트 개요

**개발 기간**: 2025.06.25 ~ 2025.07.18 (24일)

**팀명**: 개코 (KOSTA Final Project)

**프로젝트 설명**: 게임 플레이 중 생기는 명장면을 릴스/숏츠처럼 짧은 영상으로 공유하고, 댓글과 커뮤니티로 소통할 수 있는 웹 플랫폼

### 🎯 기획 의도

- 최근 인스타그램 릴스, 유튜브 숏츠 등 짧은 영상 콘텐츠 소비 증가
- 게임 플레이 중 명장면(킬캠, 클러치 등)을 공유하고 싶은 욕구
- 기존 커뮤니티의 불편함 개선 (텍스트 중심, 영상 업로드 불편)
- **해결책**: 게임 명장면을 짧은 영상으로 공유하고, 댓글과 커뮤니티로 소통하는 웹 플랫폼 구현

## 🔗 링크

- **Frontend Repository**: [GameCut_final_frontend](https://github.com/Sonjulking/GameCut_final_frontend)
- **Backend Repository**: [GameCut_final_backend](https://github.com/Sonjulking/GameCut_final_backend)
- **배포 URL**: [www.gamecut.net](http://www.gamecut.net)

## 👥 팀원 구성

| 이름 | 역할 | 담당 |
|------|------|------|
| **공우진** | 백엔드/프론트엔드 | |
| **고강찬** | 백엔드/프론트엔드 | 메인화면, 파일처리, 게시글 작성, GPT API 연동 |
| **조세창** | 백엔드/프론트엔드 | |
| **전희재** | 백엔드/프론트엔드  |  |
| **홍지완** | 백엔드/프론트엔드 ||

## 🛠️ 기술 스택

### Frontend
![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=black)
![Redux](https://img.shields.io/badge/Redux-764ABC?style=flat-square&logo=Redux&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=flat-square&logo=Vite&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/TailwindCSS-06B6D4?style=flat-square&logo=TailwindCSS&logoColor=white)

### Backend
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=flat-square&logo=Hibernate&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSONWebTokens&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)

### DevOps
![AWS](https://img.shields.io/badge/AWS_Lightsail-232F3E?style=flat-square&logo=AmazonAWS&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=GitHubActions&logoColor=white)

### Tools
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/VS_Code-007ACC?style=flat-square&logo=VisualStudioCode&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ-000000?style=flat-square&logo=IntelliJIDEA&logoColor=white)

## ✨ 주요 기능

### 1. 영상 공유 시스템
- 📹 게임 명장면 영상 업로드 (릴스/숏츠 형식)
- 🔀 랜덤 영상 재생
- 🏷️ AI 기반 자동 태그 추천 (GPT API)
- 🖼️ 자동 썸네일 생성

### 2. 커뮤니티 기능
- 💬 댓글 시스템 (댓글, 대댓글)
- ❤️ 좋아요 기능
- 👥 팔로우/팔로워 관리
- 📨 쪽지 시스템
- 🚫 사용자 차단

### 3. 게임화 요소
- 🎮 웹 게임 (티어 맞추기)
- 🏆 포인트 시스템 및 랭킹
- 🛒 포인트 상점 (아이템 구매)

### 4. 사용자 관리
- 🔐 소셜 로그인 (카카오, 네이버, 구글)
- 👤 프로필 관리
- 🎨 테마 설정 (다크 모드)
- 📊 내 활동 통계

## 🗄️ 데이터베이스 설계

주요 테이블:
- **USER**: 사용자 정보
- **BOARD**: 게시글 (일반/영상)
- **COMMENT**: 댓글 및 대댓글
- **VIDEO**: 영상 메타데이터
- **TAG**: 태그 및 영상-태그 매핑
- **FOLLOW**: 팔로우 관계
- **POINT_HISTORY**: 포인트 내역
- **POINT_STORE**: 상점 아이템
- **MESSAGE**: 쪽지
- **REPORT**: 신고

## 📱 주요 화면

### 메인 화면
- 무한 스크롤 방식의 영상 피드
- 태그별 필터링
- 좋아요, 댓글, 공유, 신고 기능

### 게시판
- 카드형/리스트형 보기 전환
- 카테고리별 필터링
- 영상/일반 게시글 구분

### 게시글 작성
- 일반 게시글: 에디터 기반
- 영상 게시글: 태그 입력, 썸네일 지정
- AI 태그 추천 기능

### 마이페이지
- 내 정보 관리
- 내 게시글/댓글 조회
- 팔로우/팔로워 관리
- 포인트 내역
