set serveroutput on;
--게시물 테이블--
CREATE TABLE board (
    idx NUMBER NOT NULL  ,
    title VARCHAR2(100) NOT NULL ,
    content VARCHAR2(3000) NOT NULL,
    writer VARCHAR2(20) NOT NULL,
    view_cnt NUMBER DEFAULT 0 NOT NULL,
    delete_yn CHAR(1) DEFAULT 'N' NOT NULL CHECK (delete_yn IN ('Y', 'N')),
    insert_date DATE DEFAULT SYSDATE NOT NULL,
    update_date DATE,
    delete_date DATE,
    PRIMARY KEY (idx)
) 
--게시물 테이블 코멘트 추가--
COMMENT ON COLUMN board.title IS '제목';
COMMENT ON COLUMN board.content IS '내용';
COMMENT ON COLUMN board.writer IS '작성자';
COMMENT ON COLUMN board.view_cnt IS '조회 수';
COMMENT ON COLUMN board.delete_yn IS '삭제 여부';
COMMENT ON COLUMN board.insert_date IS '등록일';
COMMENT ON COLUMN board.update_date IS '수정일';
COMMENT ON COLUMN board.delete_date IS '삭제일';

-- 시퀸스와 트리거 사용--자동 번호 할당: 이 트리거는 board 테이블에 새로운 데이터를 삽입할 때마다, idx 컬럼에 자동으로 고유한 번호를 할당
CREATE SEQUENCE board_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;
--게시물 등록 트리거 발동: 행(row)--
CREATE OR REPLACE TRIGGER board_insert_trigger
BEFORE INSERT ON board --게시물 등록 전에 트리거가 실행 
FOR EACH ROW -- 행 데이터가 삽입될 때마다, 각 행(row)에 대해 트리거 실행
BEGIN 
    :NEW.idx := board_seq.NEXTVAL; --NEW는 행 레벨 트리거에서의 삽입 및 갱신 조작에서 새 테이블 행을 나타내는 의사 레코드. 사용법은 :NEW.컬럼명
END;
--------------------------------------------------------
--회원 테이블[아이디,비밀번호,전화번호,주소,성별]--

CREATE TABLE MemberInfo (
    ID VARCHAR2(50) PRIMARY KEY,
    PASSWORD VARCHAR2(100) NOT NULL,
    MEMBER_NAME VARCHAR2(100) NOT NULL,
    TEL VARCHAR2(15),
    ADDRESS VARCHAR2(255),
    SEX CHAR(1) CHECK (SEX IN ('M', 'F'))
);

COMMENT ON COLUMN board.delete_date IS '회원정보 테이블';
COMMENT ON COLUMN MemberInfo.ID IS '아이디';
COMMENT ON COLUMN MemberInfo.PASSWORD IS '비밀번호';
COMMENT ON COLUMN MemberInfo.MEMBER_NAME IS '회원 이름';
COMMENT ON COLUMN MemberInfo.TEL IS '전화번호';
COMMENT ON COLUMN MemberInfo.ADDRESS IS '주소';
COMMENT ON COLUMN MemberInfo.SEX IS '성별';  

--회원의 로그인, 로그아웃 일시 log. 밀리초까지 출력하는 TIMESTAMP로 사용.
CREATE TABLE Memberlog (
    Login_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Logout_date TIMESTAMP
);

-- 작성일이 최근 24시간 이내일 경우 HH:MM 형식으로 출력하기 위한 VIEW 생성
CREATE OR REPLACE VIEW BoardView AS
SELECT 
    idx,
    writer,
    title,
    view_cnt,
    CASE 
        WHEN insert_date >= SYSDATE - 1 THEN TO_CHAR(insert_date, 'HH24:MI')
        ELSE TO_CHAR(insert_date, 'YYYY-MM-DD HH24:MI:SS')
    END AS insert_date
FROM Board;

