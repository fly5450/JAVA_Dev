CREATE OR REPLACE PROCEDURE USER01.FIND_MEMBER_ID (
    p_membername IN MemberInfo.MEMBERNAME%TYPE,
    p_password IN MemberInfo.PASSWORD%TYPE,
    p_tel IN MemberInfo.TEL%TYPE,
    o_memberid OUT MemberInfo.ID%TYPE
) IS
BEGIN
    SELECT ID
    INTO o_memberid
    FROM MemberInfo
    WHERE MEMBERNAME = p_membername
      AND PASSWORD = p_password
      AND TEL = p_tel
      AND deleteYn = 'N';  -- 삭제되지 않은 사용자만 검색

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        o_memberid := NULL;  -- 해당 조건에 맞는 아이디가 없으면 NULL 반환
END;