import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Service {
    private UnifiedDAO unifiedDAO;
    private Connection conn;

    public Service(Connection conn) {
        this.unifiedDAO = new UnifiedDAO(conn);
        this.conn = conn; // conn을 초기화합니다.
    }

    // 회원 가입
    public void registerMember(UnifiedDTO member) {
        int result = unifiedDAO.registerMember(member);
        if (result > 0) {
            System.out.println("회원 가입이 성공적으로 완료되었습니다.");
        } else {
            System.out.println("회원 가입에 실패했습니다. 이미 사용 중인 아이디인지 확인하세요.");
        }
    }

  // 로그인 프로시저 호출하여 로그인 시간 기록
  public UnifiedDTO loginMember(String id, String password) {
    UnifiedDTO member = unifiedDAO.login(id, password);
    if (member != null) {
        recordLogin(member.getId());
        System.out.println("로그인 성공: " + member.getId() + " << " + member.getMemberName() + " >>님 환영합니다.");
        } else {
            System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }
        return member; 
  }




    // // 로그아웃
    // public void logoutMember(String memberId) {
    //     unifiedDAO.recordLogout(memberId);
    //     System.out.println("로그아웃 성공!");
    // }
    // 로그아웃 시, 프로시저 호출하여 로그아웃 시간 기록
    public void logoutMember(String memberId) {
        recordLogout(memberId); // 로그아웃 기록 남기기    
        System.out.println("로그아웃 성공!");
    }

    

    // 로그인 기록을 위한 프로시저 호출
    private void recordLogin(String logid) {
        String sql = "{call loginProcedure(?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, logid);
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   // 로그아웃 기록을 위한 프로시저 호출
   private void recordLogout(String logid) {
    String sql = "{call logoutProcedure(?)}";
    try (CallableStatement cstmt = conn.prepareCall(sql)) {
        cstmt.setString(1, logid);
        cstmt.execute();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    // 아이디 찾기
    public String findMemberId(String memberName, String password, String tel) {
        String memberId = unifiedDAO.findMemberId(memberName, password, tel);
        if (memberId != null) {
            System.out.println("아이디 찾기 성공: 회원님의 아이디는 " + memberId + "입니다.");
        } else {
            System.out.println("아이디 찾기 실패: 이름 또는 전화번호를 확인하세요.");
        }
        return memberId;
    }

    // 회원 탈퇴
    public boolean deleteMember(String memberId, String password, String certPassword) {
        String savedPassword = unifiedDAO.getPasswordById(memberId);
        if (savedPassword != null && savedPassword.equals(password)) {
            boolean success = unifiedDAO.setDeleteYn(memberId, true);
            if (success) {
                unifiedDAO.recordLogout(memberId);
                System.out.println("회원 탈퇴가 성공적으로 완료되었습니다.");
                return true;
            } else {
                System.out.println("회원 탈퇴에 실패했습니다. 다시 시도하세요.");
            }
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
        return false;
    }

    // 비밀번호 초기화
    public void resetPassword(String id, String newPassword) {
        int result = unifiedDAO.resetPassword(id, newPassword);
        if (result > 0) {
            System.out.println("비밀번호가 성공적으로 초기화되었습니다.");
        } else {
            System.out.println("비밀번호 초기화에 실패했습니다.");
        }
    }

    // 본인 인증
    public boolean verifyUser(String id, String password, String tel) {
        UnifiedDTO user = unifiedDAO.getMemberById(id);
        if (user != null) {
            return user.getPassword().equals(password) && user.getTel().equals(tel);
        }
        return false;
    }

    // 내 정보 보기
    public UnifiedDTO getMemberById(String userId) {
        return unifiedDAO.getMemberById(userId);
    }

    // 모든 게시물 조회
    public List<UnifiedDTO> getAllBoards() {
        return unifiedDAO.getAllBoards();
    }

    // 게시물 추가
    public void insertBoard(UnifiedDTO board) {
        int result = unifiedDAO.insertBoard(board);
        if (result > 0) {
            System.out.println("게시물이 성공적으로 추가되었습니다.");
        } else {
            System.out.println("게시물 추가에 실패했습니다.");
        }
    }

    // 게시물 삭제
    public boolean deleteBoard(int boardId) {
        UnifiedDTO board = unifiedDAO.getBoardById(boardId);
        if (board == null) {
            System.out.println("게시글이 존재하지 않습니다.");
            return false;
        }
        int result = unifiedDAO.deleteBoard(boardId);
        return result > 0;
    }

    // 게시물 수정
    public boolean updateBoard(UnifiedDTO updatedBoard) {
        int result = unifiedDAO.updateBoard(updatedBoard);
        return result > 0;
    }

    // 게시물 상세 조회
    public UnifiedDTO getBoardById(int no) {
        return unifiedDAO.getBoardById(no);
    }

    // 조회수 증가
    public void incrementViewCount(int no) {
        unifiedDAO.incrementViewCount(no);
    }

    // 관리자 여부 확인
    public boolean checkAdminStatus(String memberId) {
        return unifiedDAO.checkAdminStatus(memberId);
    }

    // 모든 회원 조회 (관리자 전용)
    public List<UnifiedDTO> showMemberAll() {
        return unifiedDAO.showMemberAll();
    }
}