CREATE OR REPLACE PROCEDURE USER01.logoutProcedure(
    p_member_id IN VARCHAR2
)
IS
    v_now TIMESTAMP := SYSTIMESTAMP;
BEGIN
    -- MemberInfo 테이블에 로그아웃 시간 기록
    UPDATE MemberInfo
    SET last_logout_date = v_now
    WHERE id = p_member_id;

    -- MemLog 테이블에 로그아웃 시간 기록
    UPDATE MemLog
    SET logout_date = v_now
    WHERE logid = p_member_id
      AND logout_date IS NULL;  -- 가장 최근의 로그인 기록에 대한 로그아웃 시간 기록

    COMMIT;
END;
