import java.sql.Connection;
import java.util.List;

public class Controller {
    private Service service;

    public Controller(Connection conn) {
        this.service = new Service(conn);
    }
    
    // [회원 가입]
    public void registerMember(UnifiedDTO member) {
        int result = service.registerMember(member);
        if (result > 0) {
            System.out.println("회원 가입이 성공적으로 완료되었습니다.");
        } else {
            System.out.println("회원 가입에 실패했습니다. 이미 사용 중인 아이디인지 확인하세요.");
        }
    }

    // [로그인]
    public String login(String id, String password) throws Exception {
        String result = service.login(id, password);
        if (result != null) {
            System.out.println("로그인 성공: " +  id+ "님 환영합니다."); 
            return result;
        } else {
            System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            return null;
        }
    }
    // [로그아웃]
    public void logout(String memberId) {
        service.recordLogoutToTable(memberId);
        System.out.println("로그아웃 성공! ");
    }

    public String findMemberId(String memberName, String password, String tel) {
        String memberId = service.findMemberId(memberName, password, tel);
        if (memberId != null) {
            System.out.println("아이디 찾기 성공: 회원님의 아이디는 " + memberId + "입니다.");
        } 
        return memberId; 
    }
    // 관리자기능 : 모든 회원 조회.
    public List<UnifiedDTO> showMemberAll() {
        return service.showMemberAll();
    }
    
    // [회원 탈퇴]
  public void deleteMember(String memberId, String password, String certPassword) {
    // 비밀번호 일치 여부 확인
    if (!password.equals(certPassword)) {
        System.out.println("비밀번호가 일치하지 않습니다. 다시 시도하세요.");
        return;
    }

    // 현재 비밀번호 확인
    String storedPassword = service.getPasswordById(memberId);
    if (storedPassword != null && storedPassword.equals(password)) {
        boolean success = service.setDeleteYn(memberId, true);
        if (success) {
            System.out.println("성공적으로 회원탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.");
        } else {
            System.out.println("회원 탈퇴에 실패했습니다. 다시 시도하세요.");
        }
    } else {
        System.out.println("비밀번호가 일치하지 않습니다.");
    }
}
    // [비밀번호초기화]
    public void resetPassword(String id, String newPassword) {
        int result = service.resetPassword(id, newPassword);
        if (result > 0) {
            System.out.println("비밀번호가 성공적으로 초기화되었습니다.");
        } else {
            System.out.println("비밀번호 초기화에 실패했습니다.");
        }
    }

    public UnifiedDTO getMyInfo(String userId) {
        return service.getMemberById(userId); 
    }
    // [모든 게시물 조회]
    public List<UnifiedDTO> getAllBoards() {
    List<UnifiedDTO> boardList = service.getAllBoards();
    return boardList; 
    }

    // [게시물 추가]
    public void insertBoard(UnifiedDTO board) {
        int result = service.insertBoard(board);
        if (result > 0) {
            System.out.println("게시물이 성공적으로 추가되었습니다.");
        } else {
            System.out.println("게시물 추가에 실패했습니다.");
        }
    }
    // [게시물 삭제]
    public void deleteBoard(int idx) {
        int result = service.deleteBoard(idx);
            if ( result > 0) {
                System.out.println("게시물이 성공적으로 삭제되었습니다."); 
                }
                else {
                    System.out.println("게시물 삭제에 실패했습니다.");
                }
            }
    public boolean isAdmin(String memberId) {
        // 데이터베이스에서 memberId에 해당하는 사용자가 관리자인지 확인하는 로직
        return service.checkAdminStatus(memberId);
        }

    
}
    
