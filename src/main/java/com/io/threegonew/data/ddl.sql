-- READ --
-- 먼저 test 데이터베이스의 charset 을 utf-8로 변경합니다 --
-- 기존 테이블을 삭제하거나 컬럼명을 동일하게 맞춰주세요 --
-- TOURITEM 테이블은 데이터가 NULL인 컬럼이 많으므로 contentid를 제외하고는 모두 NULL로 설정하였습니다 --
ALTER DATABASE test DEFAULT CHARACTER SET='utf8' COLLATE='utf8_general_ci';

-- 테이블 생성 --
--플래너 테이블 추가--
-- CREATE TABLE 'PLANNER' (
--     u_id varchar(40) NOT NULL,
--     p_title varchar(200) NOT NULL,
--     p_startdate DATETIME NOT NULL,
--     p_enddate DATETIME NOT NULL
-- );
-- 지역
CREATE TABLE `T_AREA` (
                          j_areacode   INT      NOT NULL,
                          j_area_name   varchar(200)      NOT null,
                          latitude double,
                          longitude double
);
insert into T_AREA values('1', '서울', 37.56632099109217, 126.97794558984958);
insert into T_AREA values('2', '인천', 37.45590872680688, 126.70552692050053);
insert into T_AREA values('3', '대전', 36.35046856919224, 127.38482123708665);
insert into T_AREA values('4', '대구', 35.87140219365024, 128.60174803297681);
insert into T_AREA values('5', '광주', 37.42941190584035, 127.2551452976328);
insert into T_AREA values('6', '부산', 35.179775233195855, 129.0749925863485);
insert into T_AREA values('7', '울산', 35.53962603316087, 129.31151418501798);
insert into T_AREA values('8', '세종특별자치시', 36.480132736457776, 127.28875674178784);
insert into T_AREA values('31', '경기도', 37.27504774582099, 127.00944450410927);
insert into T_AREA values('32', '강원도', 37.88539639746763, 127.7297762607464);
insert into T_AREA values('33', '충청북도', 36.63567674729703, 127.49138343414079);
insert into T_AREA values('34', '충청남도', 36.658833942854216, 126.67284926797313);
insert into T_AREA values('35', '경상북도', 36.57601417809463, 128.50559446919635);
insert into T_AREA values('36', '경상남도', 35.23828668905039, 128.69239588910835);
insert into T_AREA values('37', '전라북도', 35.82036067006113, 127.10872713817363);
insert into T_AREA values('38', '전라남도', 34.81621546364296, 126.46291182755675);
insert into T_AREA values('39', '제주도', 33.4889273516415, 126.50042271000662);

-- 리뷰, 리뷰북, 리뷰 사진 테이블 추가 --
CREATE TABLE `review` (
                          `review_id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `book_id` bigint(20) DEFAULT NULL,
                          `user_id` varchar(40) NOT NULL,
                          `touritem_id` varchar(40) DEFAULT NULL,
                          `touritem_title` varchar(40) NOT NULL,
                          `review_content` text NOT NULL,
                          `view_count` bigint(20) DEFAULT 0 NOT NULL,
                          `reg_date` date NOT NULL,
                          `mod_date` date NOT NULL,
                          PRIMARY KEY (`review_id`),
                          KEY `review_FK` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `review_book` (
                               `book_id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `user_id` varchar(40) NOT NULL,
                               `planner_id` bigint(20) NOT NULL,
                               `book_title` varchar(40) NOT NULL,
                               `book_content` varchar(100) DEFAULT NULL,
                               `cover_o_file` varchar(255) DEFAULT NULL,
                               `cover_file_path` varchar(1000) DEFAULT NULL,
                               `reg_date` date NOT NULL,
                               `mod_date` date NOT NULL,
                               PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


CREATE TABLE `review_photo` (
                                `file_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `review_id` bigint(20) NOT NULL,
                                `o_file` varchar(255) NOT NULL,
                                `file_path` varchar(1000) NOT NULL,
                                `file_size` bigint(20) NOT NULL,
                                `reg_date` date NOT NULL,
                                PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- 플래너 테이블 추가 --
create table `PLAN` (
                        plan_id int AUTO_INCREMENT PRIMARY KEY,
                        u_id  varchar(40) not null,
                        p_id int not null,
                        plan_day int not null,
                        plan_order int not null,
                        contentid varchar(40) not null
)

--이미 플래너 테이블을 추가했다면 contentid 로 칼럼명을 바꿔주세요. touritem 의 contentid와 통일--
ALTER TABLE test.plan CHANGE plan_contentid contentid varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;


create table `PLANNER` (
                           p_id int AUTO_INCREMENT PRIMARY KEY,
                           u_id  varchar(40) not null,
                           p_name varchar(200) not null,
                           start_date DATE not null,
                           end_date DATE not null
);
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
                              b_visitcount int default 0 not NULL
);

-- 유저테이블 --
CREATE TABLE `USERS` (
                         U_ID   varchar(40)      NOT NULL,
                         U_PW   varchar(255)      NOT NULL,
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



--users pk 추가
ALTER TABLE test.users ADD CONSTRAINT users_pk PRIMARY KEY (U_ID);
--touritem pk 추가
ALTER TABLE test.touritem ADD CONSTRAINT touritem_pk PRIMARY KEY (contentid);

--북마크 테이블 추가
CREATE TABLE test.bookmark (
                                bookmark_id BIGINT auto_increment NOT NULL,
                                content_id varchar(40) NOT NULL,
                                u_id varchar(40) NOT NULL,
                                CONSTRAINT bookmarks_pk PRIMARY KEY (bookmark_id),
                                CONSTRAINT bookmarks_un UNIQUE KEY (content_id,u_id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb3
COLLATE=utf8mb3_general_ci;
-- 북마크 외래키 추가
ALTER TABLE test.bookmark ADD CONSTRAINT bookmarks_FK FOREIGN KEY (content_id) REFERENCES test.touritem(contentid);
ALTER TABLE test.bookmark ADD CONSTRAINT bookmarks_FK_1 FOREIGN KEY (u_id) REFERENCES test.users(U_ID);
