
import java.util.List;
import java.util.Scanner;

//Client (User) 와 상호작용하는 Console (View layer)
public class Main {
    private static MemberController memberController = new MemberController();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 줄바꿈 문자 처리

            switch (choice) {
                //1. 회원가입.
                case 1:
                    registerMember();
                    break;
                //2. 로그인
                case 2:
                    loginMember();
                    break;
                //3. 아이디찾기
                case 3:
                    findMemberId();
                    break;
                //4. 비밀번호 초기화
                case 4:
                    resetPassword();
                    break;
                //5. 종료
                case 5:
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    return;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }
  
    // 메뉴 출력
    private static void showMenu() {
        System.out.println("1. 회원 가입");
        System.out.println("2. 로그인");
        System.out.println("3. 아이디 찾기");
        System.out.println("4. 비밀번호 초기화");
        System.out.println("5. 종료");
        System.out.print("선택>>: ");
    }

    // 게시물 등록
    private static void createBoard() {
        System.out.println("제목:");
        String title = scanner.nextLine();
        System.out.println("내용:");
        String content = scanner.nextLine();
        System.out.println("작성자:");
        String writer = scanner.nextLine();
        
        BoardDTO board = new BoardDTO();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        board.setViewCnt(0); // 초기 조회 수 0
        board.setDeleteYn("N"); // 삭제 여부 초기 값
        board.setInsertDate(new java.util.Date());

        controller.createBoard(board);
        System.out.println("게시물이 등록되었습니다.");
    }

    // 게시물 목록 조회
    private static void viewBoardList() {
        List<BoardDTO> boardList = controller.getBoardList();
        for (BoardDTO board : boardList) {
            System.out.println(board.getIdx() + " | " + board.getTitle() + " | " + board.getWriter() + " | " + board.getInsertDate());
        }
    }

    // 게시물 수정
    private static void updateBoard() {
        System.out.println("수정할 게시물 번호:");
        int idx = scanner.nextInt(); scanner.nextLine(); // 줄바꿈 문자 처리
    
        // 비밀번호 확인
        System.out.println("비밀번호를 입력하세요:");
        String inputPassword = scanner.nextLine();
    
        // 사용자 ID (예를 들어, 현재 로그인한 사용자의 ID를 가정)
        String memberId = "currentUser"; // 실제 ID 값을 사용
    
        if (!controller.checkPassword(memberId, inputPassword)) {
            System.out.println("비밀번호가 일치하지 않습니다. 수정할 수 없습니다.");
            return;
        }
    
        System.out.println("새 제목:");
        String title = scanner.nextLine();
        System.out.println("새 내용:");
        String content = scanner.nextLine();
        System.out.println("새 작성자:");
        String writer = scanner.nextLine();
    
        BoardDTO board = new BoardDTO();
        board.setIdx(idx);
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        board.setUpdateDate(new java.util.Date());
    
        controller.updateBoard(board);
        System.out.println("게시물이 수정되었습니다.");
    }

    // 게시물 삭제
    private static void deleteBoard() {
        System.out.println("삭제할 게시물 번호:");
        int idx = scanner.nextInt();

        controller.deleteBoard(idx);
        System.out.println("게시물이 삭제되었습니다.");
    }
}