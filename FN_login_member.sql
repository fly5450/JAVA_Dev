CREATE OR REPLACE FUNCTION USER01.FN_login_member (
    p_id IN VARCHAR2,
    p_password IN VARCHAR2
) RETURN VARCHAR2 IS
    v_member_name VARCHAR2(100);
BEGIN
    SELECT MEMBERNAME
    INTO v_member_name
    FROM MemberInfo
    WHERE ID = p_id
    AND PASSWORD = p_password;

    RETURN v_member_name;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
END FN_login_member;