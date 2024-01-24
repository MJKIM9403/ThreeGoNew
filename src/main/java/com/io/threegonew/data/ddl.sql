-- READ --
-- 먼저 test 데이터베이스의 charset 을 utf-8로 변경합니다 --
-- 기존 테이블을 삭제하거나 컬럼명을 동일하게 맞춰주세요 --
-- TOURITEM 테이블은 데이터가 NULL인 컬럼이 많으므로 contentid를 제외하고는 모두 NULL로 설정하였습니다 --
ALTER DATABASE test DEFAULT CHARACTER SET='utf8' COLLATE='utf8_general_ci';

-- 테이블 생성 --
-- 추천 테이블 추가 --
CREATE TABLE `LIKES` (
                         l_id INT AUTO_INCREMENT,
                         b_id DOUBLE NOT NULL,
                         u_id varchar(40) NOT NULL,
                         CONSTRAINT like_pk PRIMARY KEY (l_id),
                         CONSTRAINT like_uq UNIQUE (b_id, u_id)
);

-- 댓글 테이블 추가 --
CREATE TABLE `REPLY`
(
    b_id DOUBLE,
    re_num INT auto_increment,
    u_id varchar(50) NOT NULL,
    re_content varchar(400) NOT NULL,
    re_regdate DATETIME DEFAULT SYSDATE() NOT NULL,
    re_modifydate DATETIME,
    re_del varchar(1) DEFAULT 'N' NOT NULL,
    re_parent DOUBLE DEFAULT 0 NOT NULL,
    re_child DOUBLE DEFAULT 0 NOT NULL,
    re_ref DOUBLE NOT NULL,
    re_order DOUBLE DEFAULT 0 NOT NULL,
    re_level DOUBLE DEFAULT 0 NOT NULL,
    CONSTRAINT reply_pk PRIMARY KEY (re_num)
);

-- 게시판 테이블 --
create table `BOARD` (
                              b_id int AUTO_INCREMENT PRIMARY KEY,
                              u_id varchar(50) not null,
                              u_name varchar(50) not null,
                              b_title varchar(200) not null,
                              b_content LONGTEXT not null,
                              b_postdate datetime default sysdate() not null,
                              b_ofile varchar(200),
                              b_sfile varchar(30),
                              b_visitcount double default 0 not NULL
);

-- 유저테이블 --
CREATE TABLE `USERS` (
                         U_ID   varchar(40)      NOT NULL,
                         U_PW   varchar(40)      NOT NULL,
                         U_NAME   varchar(40)      NOT NULL,
                         U_EMAIL   varchar(200)      NOT NULL,
                         U_OFILE   varchar(200)      NULL,
                         U_SFILE   varchar(200)      NULL,
                         U_ABOUT   varchar(200)      NULL
);

-- 투어아이템 --
CREATE TABLE `TOURITEM` (
                            contentid   varchar(40)      NOT NULL,
                            cat1   varchar(40),
                            cat2   varchar(40),
                            cat3   varchar(40),
                            areacode   varchar(40),
                            contenttypeid   varchar(40),
                            addr1   varchar(200),
                            addr2   varchar(200),
                            firstimage   varchar(600),
                            mapx   varchar(200),
                            mapy   varchar(200),
                            mlevel   varchar(40),
                            sigungucode   varchar(40),
                            tel   varchar(200),
                            title   varchar(200)
);

-- 대분류
CREATE TABLE `T_CAT1` (
                          cat1   varchar(40)      NOT NULL,
                          cat1_name   varchar(200)      NOT NULL
);

-- 중분류
CREATE TABLE `T_CAT2` (
                          cat2   varchar(40)      NOT NULL,
                          cat2_name   varchar(200)      NOT NULL,
                          cat1   varchar(40)      NOT NULL
);


-- 소분류
CREATE TABLE `T_CAT3` (
                          cat3   varchar(40)      NOT NULL,
                          cat3_name   varchar(200)      NOT NULL,
                          cat2   varchar(40)      NOT NULL,
                          cat1 varchar(40)      NOT NULL
);

-- 지역
CREATE TABLE `T_AREA` (
                          j_areacode   INT      NOT NULL,
                          j_area_name   varchar(200)      NOT NULL
);


-- 시군구
CREATE TABLE `T_SIGUNGU` (
                             s_sigungucode   INT      NOT NULL,
                             s_areacode   INT      NOT NULL,
                             sigungu_name   varchar(200)      NOT NULL,
                             CONSTRAINT sigungu_pk PRIMARY KEY(s_sigungucode, s_areacode)
);

-- 관광타입
CREATE TABLE `T_C_TYPE` (
                            contenttypeid   varchar(40)      NOT NULL,
                            ctype_name   varchar(200)      NOT NULL
);

-- maria db에서 한글입력이 가능하게 utf-8로 바꿔줍니다.
alter table `LIKES` convert to CHARSET UTF8;
alter table `REPLY` convert to CHARSET UTF8;
alter table `BOARD` convert to CHARSET UTF8;
alter table `USERS` convert to CHARSET UTF8;
alter table `TOURITEM` convert to CHARSET UTF8;
alter table `T_CAT1` convert to CHARSET UTF8;
alter table `T_CAT2` convert to CHARSET UTF8;
alter table `T_CAT3` convert to CHARSET UTF8;
alter table `T_AREA` convert to CHARSET UTF8;
alter table `T_SIGUNGU` convert to CHARSET UTF8;
alter table `T_C_TYPE` convert to CHARSET UTF8;



-- 데이터 입력
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('12', '관광지');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('14', '문화시설');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('15', '축제공연행사');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('25', '여행코스');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('28', '레포츠');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('32', '숙박');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('38', '쇼핑');
-- SQLINES LICENSE FOR EVALUATION USE ONLY
INSERT INTO `T_C_TYPE` (contenttypeid, ctype_name) VALUES ('39', '음식점');

--패스워드 varchar40은 너무 작아서 암호화된 암호가 들어가지 않습니다.
--이걸로 수정해주세요
ALTER TABLE test.users MODIFY COLUMN U_PW varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;