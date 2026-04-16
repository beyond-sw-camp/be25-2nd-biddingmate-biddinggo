# BiddingGo

<br>

<p align="center">
<img src="https://github.com/user-attachments/assets/1e36c230-8248-44f6-b5f0-971a78c39e9b" width="300"/>
</p>

<br>

<p align="center">
<img src="https://github.com/user-attachments/assets/e839d356-1bb6-477b-a87f-feeed40d6e67"  width="1200"/>
</p>


# Bidding mate

<table>
  
  <tr>
    <th>김진혁</th>
    <th>박선우</th>
    <th>윤정윤</th>
    <th>이민경</th>
    <th>이애은</th>
    <th>한규진
  </tr>
  
  <tr>
    <td align="center"><img width="150" height="210" style="object-fit: cover;" alt="김진혁" src="https://github.com/user-attachments/assets/d44abc12-4269-4be2-a6a9-f76a531a963f" /></td>
    <td align="center"><img width="150" height="210" style="object-fit: cover;" alt="박선우" src="https://github.com/user-attachments/assets/cf47d2b7-7633-4f17-8cbe-8b6429ceef67" /></td>   
    <td align="center"><img width="150" height="210" style="object-fit: cover;" alt="윤정윤" src="https://github.com/user-attachments/assets/5cde1533-77e1-4783-8ce9-d48ec236c25d" /></td>   
    <td align="center"><img width="150" height="210" style="object-fit: cover;" alt="이민경" src="https://github.com/user-attachments/assets/f62ebf05-846f-462e-b4d3-3fa3c58ce4fb" /></td>  
    <td align="center"><img width="150" height="210" style="object-fit: cover;" alt="이애은" src="https://github.com/user-attachments/assets/79be4278-e3e9-44c5-af71-57d1831e9406" /></td>   
    <td align="center"><img width="150" height="210" style="object-fit: cover;" alt="한규진" src="https://github.com/user-attachments/assets/45b114ef-0e30-4bcc-94d2-dfd548aa9b7c" /></td>  
  </tr>

  <tr>
    <td align="center">
      <a href="https://github.com/jin605"><img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logoColor=white" /></a>
    </td> 
    <td align="center">
      <a href="https://github.com/melly8954"><img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logoColor=white" /></a>
    </td>
    <td align="center">
      <a href="https://github.com/penep0"><img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logoColor=white" /></a>
    </td>
    <td align="center">
      <a href="https://github.com/alskung1101"><img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logoColor=white" /></a>
    </td>
    <td align="center">
      <a href="https://github.com/nueeaeel"><img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logoColor=white" /></a>
    </td>
    <td align="center">
      <a href="https://github.com/softworldqjin"><img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logoColor=white" /></a>
    </td>
  </tr>
</table>
    
---


# 목차
<br/>

1. [프로젝트 개요](#1--프로젝트-개요)
2. [요구사항 정의서](#2--요구사항-정의서)
3. [기술 스택](#3--기술-스택)
4. [ERD](#4--ERD)
5. [테이블 정의서](#5--테이블-정의서)
6. [시스템 아키텍처](#6--시스템-아키텍처)
7. [API 명세서](#7--API-명세서)
8. [테스트 계획 및 결과 보고서](#8--테스트-계획-및-결과-보고서)
9. [회고](#9--회고)

<br/>

---


# 1.  프로젝트 개요
<br/> 

## 📍 프로젝트 소개
**Biddinggo는 실시간 경매 기반의 중고 및 리셀 거래 플랫폼입니다.**  
사용자는 상품을 등록하고 다양한 경매 방식(일반 경매, 연장 경매, 타임딜 등)을 통해 판매할 수 있으며, 구매자는 실시간 입찰을 통해 원하는 상품을 경쟁적으로 구매할 수 있습니다.  

또한, 실시간 알림(SSE) 기반으로 입찰 상황을 즉시 반영하여 사용자에게 몰입감 있는 거래 경험을 제공합니다.  
특히, **빅크리 경매(Vickrey Auction, 2등 가격 경매)** 방식을 도입하여 보다 공정하고 전략적인 가격 형성을 지원합니다.

---

## 📍 배경 (이 서비스가 필요한 이유)

### 1. 기존 중고 거래 플랫폼의 가격 결정 한계
- 번개장터, 당근마켓 등은 정가 기반 거래로 적정 가격 형성이 어려움  
- 협상 과정이 비효율적이며 사용자 경험 저하  

### 2. 리셀 시장 성장과 공정한 거래 필요성 증가
- 한정판 및 인기 상품은 가격 변동성이 큼  
- 경매 방식이 가장 합리적인 가격 형성 구조지만 지원 플랫폼이 제한적  

### 3. 기존 경매 플랫폼의 UX 및 기능 한계
- UI/UX가 직관적이지 않고 사용성이 떨어짐  
- 실시간 입찰 경험 부족 및 시스템 반응 속도 문제  

---

## 📍 기존 서비스와의 차별점

### 📌 기존 서비스

#### KREAM
- 패션/스니커즈 특화 플랫폼  
- 모든 거래에 검수 필수 → 거래 속도 느림  
- 경매 방식이 아닌 호가 기반 거래  

#### 코베이옥션
- 전통적인 경매 플랫폼  
- UI/UX가 올드하고 접근성이 낮음  
- 실시간성 및 사용자 경험 부족  

---

### ✅ Biddinggo의 차별점

#### 1. 빅크리(Vickrey) 기반 경매 시스템
- 최고 입찰자가 낙찰되지만, 실제 결제 금액은 두 번째로 높은 입찰가로 결정  
- 과도한 가격 경쟁 방지  
- 사용자가 자신의 최대 지불 의사를 정직하게 반영하도록 유도  

#### 2. AI 기반 상품 예측가 제공
- 과거 거래 데이터 및 유사 상품 데이터를 기반으로 적정 가격 제시  
- 판매자/구매자 모두 합리적인 의사결정 가능  

#### 3. AI 기반 유사 상품 검색
- 이미지 및 텍스트 기반 유사도 분석  
- 검색 효율 및 사용자 경험 향상  

#### 4. 다양한 경매 옵션 제공
- 연장 경매 (마감 직전 입찰 시 자동 연장)  
- 타임딜 경매 (짧은 시간 집중 경쟁)  
- 검수 경매 (신뢰 기반 거래)  

#### 5. 실시간 입찰 시스템 (SSE 기반)
- 입찰 상태 및 알림을 실시간 반영  
- 사용자 간 경쟁 상황을 즉각적으로 체감 가능  

#### 6. 100% 에스크로 기반 안전 거래
- 거래 금액을 중간 보관 후 완료 시 정산  
- 구매자/판매자 자산 보호  

#### 7. 현대적인 UI/UX
- 직관적인 경매 흐름 제공  
- 기존 경매 서비스 대비 사용성 개선  

---

## 📍 기대 효과

- **합리적인 가격 형성**
  - 빅크리 경매 구조를 통한 공정한 시장 가격 결정  

- **거래 신뢰도 향상**
  - 에스크로 + 검수 옵션 기반 안전 거래 환경  

- **사용자 참여도 증가**
  - 실시간 입찰 및 알림으로 몰입도 상승  

- **판매 효율 극대화**
  - 다양한 경매 옵션을 통한 유연한 판매 전략  

- **플랫폼 경쟁력 확보**
  - AI + 실시간 시스템 + 경매 구조 결합
<br/>

---

# 2.  요구사항 정의서
![](img/Srs.png)<br/>
🔗[ 요구사항 정의서 ](https://docs.google.com/spreadsheets/d/16YGKpTcpo310JjvUu9Q1Nm9atCI-XYx5TB2GD3ocx3E)
<br/>

---

# 3.  기술 스택
<br/>

## 🔧 Backend
<p>
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/oauth2-EB5424?style=for-the-badge&logo=oauth&logoColor=white">
  <img src="https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">
  <img src="https://img.shields.io/badge/mybatis-000000?style=for-the-badge&logo=apache&logoColor=white">
  <img src="https://img.shields.io/badge/flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white">
</p>

## 🗄️ Database
<p>
  <img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white">
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white">
  <img src="https://img.shields.io/badge/pgvector-000000?style=for-the-badge&logo=postgresql&logoColor=white">
</p>

## 🚀 Infra
<p>
  <img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
  <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/docker--compose-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/cloudflare%20r2-F38020?style=for-the-badge&logo=cloudflare&logoColor=white">
  <img src="https://img.shields.io/badge/vercel-000000?style=for-the-badge&logo=vercel&logoColor=white">
  <img src="https://img.shields.io/badge/kt%20cloud-FF0000?style=for-the-badge&logo=icloud&logoColor=white">
  <img src="https://img.shields.io/badge/ghcr-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white">
</p>

## 🔌 API
<p>
  <img src="https://img.shields.io/badge/toss%20payments-0064FF?style=for-the-badge&logo=tosspayments&logoColor=white">
  <img src="https://img.shields.io/badge/openai-412991?style=for-the-badge&logo=openai&logoColor=white">
</p>

## ⚙️ CI/CD
<p>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/ghcr-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/github%20actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">
</p>

## 🤝 Collaboration
<p>
  <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
  <img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/erdcloud-0B4F6C?style=for-the-badge&logo=icloud&logoColor=white">
  <img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white">
</p>
<br/>

---

# 4.  ERD
![](img/erd.png)<br/>
🔗[ ERD ](https://www.erdcloud.com/d/KdYRE8i9mnqYda2L5)
<br/>

---

# 5.  테이블 정의서
![](img/table.PNG)<br/>
🔗[MariaDB 테이블 정의서](https://docs.google.com/spreadsheets/d/1OoOrOKj8uusUITQNaMLExuCe3ReM_St3hvvKVMiX4vM/edit?gid=573455609#gid=573455609)

🔗[Supabase 테이블 정의서](https://docs.google.com/spreadsheets/d/1OoOrOKj8uusUITQNaMLExuCe3ReM_St3hvvKVMiX4vM/edit?gid=141144957#gid=141144957)
<br/>

---

# 6.  시스템 아키텍처
<br/>

![](https://github.com/user-attachments/assets/15f9e2e3-2915-4a29-8dea-22d367af813c)<br/>
<br/>

---
# 7.  API 명세서
![](img/table.PNG)<br/>
🔗[API 명세서](https://www.notion.so/31e1072487c38010b4d4f280f3e03481?v=31e1072487c381709755000ca276af3c&source=copy_link)
<br/>

---

# 8.  테스트 계획 및 결과 보고서
<br/>

🔗[테스트 계획 및 결과 보고서](https://docs.google.com/spreadsheets/d/16YGKpTcpo310JjvUu9Q1Nm9atCI-XYx5TB2GD3ocx3E/edit?gid=1106294099#gid=1106294099)
<br/>

---

# 9.  회고
<br/>

#### 김진혁
> 작성
 
#### 박선우
> 작성

#### 윤정윤
> 작성

#### 이민경
> 작성

#### 이애은
> 작성

#### 한규진
> 작성

