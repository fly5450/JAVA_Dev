CREATE OR REPLACE PROCEDURE USER01.loginProcedure(
    p_member_id IN VARCHAR2
)
IS
    v_now TIMESTAMP := SYSTIMESTAMP;
BEGIN
    -- MemberInfo 테이블에 로그인 시간 기록
    UPDATE MemberInfo
    SET last_login_date = v_now
    WHERE id = p_member_id;

    -- MemLog 테이블에 로그인 시간 기록
    INSERT INTO MemLog (logid, login_date)
    VALUES (p_member_id, v_now);

    COMMIT;
END;
