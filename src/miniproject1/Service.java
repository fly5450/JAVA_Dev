import java.sql.Connection;
import java.util.List;

public class Service {
    private UnifiedDAO unifiedDAO;
    public String memberName;

    public Service(Connection conn) {
        this.unifiedDAO = new UnifiedDAO(conn);
    }

    // 회원 가입
    public int registerMember(UnifiedDTO member) {
        return unifiedDAO.registerMember(member);
    }

      // 로그인 시, 사용자를 확인하고 로그인 기록을 DB에 저장
      public UnifiedDTO login(String id, String password) {
        UnifiedDTO member = unifiedDAO.login(id, password);
        if (member != null) {
            unifiedDAO.recordLogin(member.getId()); // 로그인 기록 남기기
        }
        return member;
    }

    // 로그아웃 시, 로그아웃 기록을 DB에 저장
    public void logout(String memberId) {
        unifiedDAO.recordLogout(memberId); // 로그아웃 기록 남기기
    }

    // 아이디 찾기
    public String findMemberId(String memberName, String password, String tel) {
        return unifiedDAO.findMemberId(memberName, password, tel);
    }

    // 비밀번호 초기화
    public int resetPassword(String id, String newPassword) {
        return unifiedDAO.resetPassword(id, newPassword);
    }

    // 현재 비밀번호 확인
    public String getPasswordById(String memberId) {
        return unifiedDAO.getPasswordById(memberId);
    }

    // 회원 정보 조회
    public UnifiedDTO getMemberById(String memberId) {
        return unifiedDAO.getMemberById(memberId);
    }

    // 게시글 상세보기
    public UnifiedDTO getBoardById(int boardId) {
        return unifiedDAO.getBoardById(boardId);
    }

    // 게시물 목록 조회
    public List<UnifiedDTO> getAllBoards() {
        return unifiedDAO.getAllBoards();
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
        return unifiedDAO.deleteBoard(idx) > 0;
    }

    // 게시글 수정
    public boolean updateBoard(UnifiedDTO updatedBoard) {
        return unifiedDAO.updateBoard(updatedBoard) > 0;
    }

    // 회원탈퇴 처리
    public boolean setDeleteYn(String memberId, boolean deleteYn) {
        return unifiedDAO.setDeleteYn(memberId, deleteYn);
    }

    // 로그인 및 로그아웃 기록 처리
    public void recordLoginAndLogout(String memberId, boolean isLogin) {
        if (isLogin) {
            unifiedDAO.recordLogin(memberId);
        } else {
            unifiedDAO.recordLogout(memberId);
        }
    }

    // 관리자 여부 확인
    public boolean checkAdminStatus(String memberId) {
        return unifiedDAO.checkAdminStatus(memberId);
    }

    // 관리자 기능: 모든 멤버 조회
    public List<UnifiedDTO> showMemberAll() {
        return unifiedDAO.showMemberAll();
    }
}