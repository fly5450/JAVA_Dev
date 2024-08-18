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
        String member = service.login(id, password);
        if (member != null) {
            System.out.println("로그인 성공: " + id + "님 환영합니다."); 
            return member;
        } else {
            System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            return null;
        }
    }
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

    // [비밀번호 초기화]
    public void resetPassword(String id, String newPassword) {
        int result = service.resetPassword(id, newPassword);
        if (result > 0) {
            System.out.println("비밀번호가 성공적으로 초기화되었습니다.");
        } else {
            System.out.println("비밀번호 초기화에 실패했습니다.");
        }
    }

    // [게시물 조회]
   public void getAllBoards() {
        List<UnifiedDTO> boardList = service.getAllBoards();
        if (boardList.isEmpty()) {
            System.out.println("게시물이 없습니다.");
        } else {
            for (UnifiedDTO board : boardList) {
                System.out.println("IDX: " + board.getIdx());
                System.out.println("Title: " + board.getTitle());
                System.out.println("Content: " + board.getContent());
                System.out.println("Writer: " + board.getWriter());
                System.out.println("View Count: " + board.getViewCnt());
                System.out.println("Delete Y/N: " + board.getDeleteYn());
                System.out.println("Insert Date: " + board.getInsertDate());
                System.out.println("Update Date: " + board.getUpdateDate());
                System.out.println("Delete Date: " + board.getDeleteDate());
                System.out.println("----------------------------");
            }
        }
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

    public void deleteBoard(int idx) {
        int result = service.deleteBoard(idx);
            if ( result > 0) {
                System.out.println("게시물이 성공적으로 삭제되었습니다."); }
                else {
                    System.out.println("게시물 삭제에 실패했습니다.");
                }
            }

        }
    
