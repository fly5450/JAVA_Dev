CREATE OR REPLACE PROCEDURE USER01.DISPLAY_BOARD_WITH_TIME_FORMAT AS
    CURSOR board_cursor IS
        SELECT idx, title, content, writer, viewcnt, insert_date
        FROM board;

    v_idx        board.idx%TYPE;
    v_title      board.title%TYPE;
    v_content    board.content%TYPE;
    v_writer     board.writer%TYPE;
    v_viewcnt    board.viewcnt%TYPE;
    v_insert_date board.insert_date%TYPE;
    v_formatted_date VARCHAR2(20);

BEGIN
    -- Header 출력
    DBMS_OUTPUT.PUT_LINE('게시물 번호 | 작성자   | 제목           | 읽은수 | 작성일');
    DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------------');

    -- Cursor를 사용하여 게시물 정보 조회
    FOR board_record IN board_cursor LOOP
        v_idx := board_record.idx;
        v_title := board_record.title;
        v_content := board_record.content;
        v_writer := board_record.writer;
        v_viewcnt := board_record.viewcnt;
        v_insert_date := board_record.insert_date;

        -- 작성일 기준으로 24시간 이내이면 시:분 형식, 아니면 날짜 형식
        IF (SYSDATE - v_insert_date) * 24 < 24 THEN
            v_formatted_date := TO_CHAR(v_insert_date, 'HH24:MI');
        ELSE
            v_formatted_date := TO_CHAR(v_insert_date, 'YYYY-MM-DD');
        END IF;

        -- 게시물 정보 출력
        DBMS_OUTPUT.PUT_LINE(
            v_idx || ' | ' || v_writer || ' | ' || v_title || ' | ' ||
            v_viewcnt || ' | ' || v_formatted_date
        );
    END LOOP;
END;
