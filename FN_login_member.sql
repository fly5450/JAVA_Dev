DECLARE
  P_ID VARCHAR2(200);
  P_PASSWORD VARCHAR2(200);
  v_Return VARCHAR2(200);
BEGIN
  P_ID := NULL;
  P_PASSWORD := NULL;

  v_Return := FN_LOGIN_MEMBER(
    P_ID => P_ID,
    P_PASSWORD => P_PASSWORD
  );
  /* Legacy output: 
DBMS_OUTPUT.PUT_LINE('v_Return = ' || v_Return);
*/ 
  :v_Return := v_Return;
--rollback; 
END;
