import java.sql.Connection;
import java.util.List;

// model과 view를 연결시켜주는 다리 역할, Model, View에게 직접 지시 가능
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
    public UnifiedDTO login(String id, String password) throws Exception {
        UnifiedDTO result = service.login(id, password);
        if (result != null) {
            System.out.println("로그인 성공: " + result.getId() + "<<" + result.getMemberName() + ">>" + "님 환영합니다.");
            return result;
        } else {
            System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            return null;
        }
    }
    // [로그아웃]
    public void logout(String memberId) {
        service.logout(memberId);
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
    try {
        if (!password.equals(certPassword)) {
            System.out.println("비밀번호가 일치하지 않습니다. 다시 시도하세요.");
            return;
        }

        String savedPassword = service.getPasswordById(memberId);
        if (savedPassword != null && savedPassword.equals(password)) {
            boolean success = service.setDeleteYn(memberId, true);
            if (success) {
                System.out.println("성공적으로 회원탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.");
            } else {
                System.out.println("회원 탈퇴에 실패했습니다. 다시 시도하세요.");
            }
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
    } catch (Exception e) {
        System.out.println("회원 탈퇴 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.");
        e.printStackTrace();
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
    // [내정보보기]
    public UnifiedDTO getMyInfo(String userId) {
        return service.getMemberById(userId); 
    }
    // [모든 게시물 조회]
    public List<UnifiedDTO> getAllBoards() {
        return service.getAllBoards(); // Service 클래스의 getAllBoards() 호출
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
    public boolean deleteBoard(int boardId) {
        UnifiedDTO board = service.getBoardById(boardId);

        if (board == null) {
            System.out.println("게시글이 존재하지 않습니다.");
            return false; // 게시글이 없을 경우 false 반환
        }

        return service.deleteBoard(boardId);
    }
    // [게시물 수정]
    public boolean updateBoard(UnifiedDTO updatedBoard) {
        UnifiedDTO board = service.getBoardById(updatedBoard.getIdx());

        if (board == null) {
            System.out.println("게시글이 존재하지 않습니다.");
            return false; // 게시글이 없을 경우 false 반환
        }

        boolean result = service.updateBoard(updatedBoard);
        if (result) {
            System.out.println("게시글이 성공적으로 수정되었습니다.");
        } else {
            System.out.println("게시글 수정에 실패했습니다.");
        }
        return result;
    }
    // [게시물 조회]
    public UnifiedDTO getBoardById(int no) {
        return service.getBoardById(no);
        }
    
    // 데이터베이스에서 memberId에 해당하는 사용자가 관리자인지 확인하는 로직
    public boolean isAdmin(String memberId) {
        return service.checkAdminStatus(memberId);
        }

    public void incrementViewCount(int no) {
        service.incrementViewCount(no);
    }
    

    
}
    
