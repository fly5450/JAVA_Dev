import java.sql.Connection;
import java.util.List;

public class Service { //UnifiedDAO 클래스를 호출하여 실제 데이터베이스 작업을 처리 
    private UnifiedDAO unifiedDAO;
    public String memberName;

    public Service(Connection conn) {
        this.unifiedDAO = new UnifiedDAO(conn);
    }

    // 회원 가입
    public int registerMember(UnifiedDTO member) {
        return unifiedDAO.registerMember(member);
    }
    // 로그인
    public UnifiedDTO login(String id, String password) {
        return unifiedDAO.login(id, password);
    }
    // 로그아웃
    public UnifiedDTO logout(String id, String password) {
        return unifiedDAO.login(id, password);
    }
    // 아이디 찾기
    public String findMemberId(String member_Name, String password, String tel) {
        return unifiedDAO.findMemberId(member_Name, password, tel);
    }
    // 비밀번호 초기화
    public int resetPassword(String id, String newPassword) {
        return unifiedDAO.resetPassword(id, newPassword);
    }
    //현재 비밀번호 확인
    public String getPasswordById(String memberId) {
        return unifiedDAO.getPasswordById(memberId);
    }
     //현재 비밀번호 확인
     public UnifiedDTO getMemberById(String memberId) {
        return unifiedDAO.getMemberById(memberId);
    }
  
    //게시글 상세보기
    public UnifiedDTO getBoardById(int boardId) {
        return unifiedDAO.getBoardById(boardId);
    }
      // 게시물 목록조회
      public List<UnifiedDTO> getAllBoards() {
        return unifiedDAO.getAllBoards(); // UnifiedDAO의 getAllBoards() 호출
    }
    
    // 조회수 증가
    public void incrementViewCount(int no) {
        unifiedDAO.incrementViewCount(no);
    }
    // 게시글 추가
    public int insertBoard(UnifiedDTO board) {
        return unifiedDAO.insertBoard(board);
    }
     // 게시글 삭제
    public boolean deleteBoard(int idx) {
        int rowsAffected = unifiedDAO.deleteBoard(idx);
        return rowsAffected > 0; // 성공적으로 삭제된 경우 true 반환
    }
      // [게시물 수정]
      public boolean updateBoard(UnifiedDTO updatedBoard) {
        return unifiedDAO.updateBoard(updatedBoard) > 0;
    }

    //회원탈퇴시 memberinfo테이블 Yn필드변경
    public boolean setDeleteYn(String memberId, boolean deleteYn) {
        return unifiedDAO.setDeleteYn(memberId, deleteYn);
    }

  
    /* 
    로그인 시 loginLogRecord 메서드를 호출하여 로그아웃 시간을 기록함. */
    // 로그인 log기록
     public UnifiedDTO loginLogRecord(String id, String password) {
        UnifiedDTO member = login(id, password);
        if (member != null) {
            unifiedDAO.recordLogin(id); // 로그인 이력 기록
        }
        return member;
    }
        //  로그아웃 시 log기록, LOG 테이블과 MemberInfo 테이블에 로그아웃 시간을 기록
     public void recordLogoutToTable(String memberId) {
        unifiedDAO.recordLogout(memberId); // 로그아웃 이력 기록, recordLogout 메서드를 통해 두 테이블에 로그아웃 시간을 업데이트
    }
    public boolean checkAdminStatus(String memberId) {
        // 관리자 여부를 확인하는 쿼리를 실행
        return unifiedDAO.checkAdminStatus(memberId);
    }
    //관리자 기능 : 모든 멤버 조회 함수
    public List<UnifiedDTO> showMemberAll() {
        return unifiedDAO.showMemberAll();
    }


}

