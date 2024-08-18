import java.sql.Connection;
import java.util.List;

public class Service {
    private UnifiedDAO unifiedDAO;

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
    // 아이디 찾기
    public String findMemberId(String memberName, String tel) {
        return unifiedDAO.findMemberId(memberName, tel);
    }
    // 비밀번호 초기화
    public int resetPassword(String id, String newPassword) {
        return unifiedDAO.resetPassword(id, newPassword);
    }
    //현재 비밀번호 확인
    public String getPasswordById(String memberId) {
        return unifiedDAO.getPasswordById(memberId);
    }
    // 게시물 조회
    public List<UnifiedDTO> getAllBoards() {
        return unifiedDAO.getAllBoards();
    }
    // 게시글 추가
    public int insertBoard(UnifiedDTO board) {
        return unifiedDAO.insertBoard(board);
    }

     // 게시글 삭제
    public int deleteBoard(int idx) {
        return unifiedDAO.deleteBoard(idx);
    }
    // 게시글 수정
    public int updateBoard(UnifiedDTO board) {
        return unifiedDAO.updateBoard(board);
    }

    // 로그인 log기록
     public UnifiedDTO loginLogRecord(String id, String password) {
        UnifiedDTO member = unifiedDAO.login(id, password);
        if (member != null) {
            unifiedDAO.recordLogin(id); // 로그인 이력 기록
        }
        return member;
    }
        // 로그아웃 log기록
     public void logout(String memberId) {
        unifiedDAO.recordLogout(memberId);
    }
}

    
    