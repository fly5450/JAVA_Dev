import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

//User (View)
public class Main {
    private static Connection conn; // Connection 객체 선언
    private static Controller controller; //Controller 객체 선언
    private static Scanner scanner = new Scanner(System.in);
    private static String loggedInUserId = null; //프로그램 시작시 비로그인 상태
    private static boolean isAdmin = false;  // 관리자 여부 체크
    
    public static void main(String[] args) throws Exception {
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user01", "5450");
            controller = new Controller(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        
        while (true) {
            if (loggedInUserId == null) {
                showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();                 // 입력버퍼 클리어
                switch (choice) {
                    case 1 -> registerMember();         // 1. 회원가입 - 회원정보 입력
                    case 2 -> loginMember();            // 2. 로그인
                    case 3 -> findMemberId();           // 3. 아이디 찾기
                    case 4 -> resetPassword();          //4.  비밀번호 초기화
                    case 5 -> programExit();            // 5. 종료
                    case 666 -> checkAdminStatus();                  // 히든메뉴 관리자모드
                    default -> System.out.println("올바른 번호를 선택하세요.");
                }
            } else {
                showUserMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // 입력버퍼 클리어
                switch (choice) {
                    case 1 -> showMyInfo();             //1. 나의 정보 보기
                    case 2 -> insertBoard();            //2. 게시글 등록
                    case 3 -> Boards();          //3. 게시글 목록 조회
                    case 4 -> { //4. 회원 목록(관리자인 경우)
                        if (isAdmin) {  showMemberAll(); }
                        else {  System.out.println("접근 권한이 없습니다."); }
                    }
                    case 5 -> logout(); //5. 로그아웃
                    case 6 -> secession(); //6. 회원탈퇴
                    case 7 -> {
                        logout();
                        programExit(); //7. 종료(선 로그아웃)
                    }
                    default -> System.out.println("올바른 번호를 선택하세요.");
                }
            }
        }
    }
      // [유저 메뉴 ]
      private static void showUserMenu(){
        System.out.println("1. 나의 정보 확인");
        System.out.println("2. 게시글 등록");
        System.out.println("3. 게시판 보기");
        System.out.println("4. 회원 목록(관리자전용 메뉴)");
        System.out.println("5. 로그아웃");
        System.out.println("6. 회원탈퇴");
        System.out.println("7. 종료");
        System.out.print("선택: ");
    }
    // [메인메뉴 출력]
    private static void showMainMenu() {
            System.out.println("1. 회원 가입");
            System.out.println("2. 로그인");
            System.out.println("3. 아이디 찾기");
            System.out.println("4. 비밀번호 초기화");
            System.out.println("5. 종료");
            System.out.print("선택>>: ");
    }
    // [게시판 메뉴 출력]
    private static void boardMenu(){
        System.out.println("1. 게시글 상세보기");
        System.out.println("2. 게시글 수정");
        System.out.println("3. 게시글 삭제");
        System.out.println("4. 돌아가기");
        System.out.print("선택: ");
    }
    // [내정보보기]
    private static void showMyInfo() {
        UnifiedDTO myInfo = controller.getMyInfo(loggedInUserId);
        if (myInfo != null) {
            System.out.println("아이디: " + myInfo.getId());
            System.out.println("이름: " + myInfo.getMemberName());
            System.out.println("전화번호: " + myInfo.getTel());
            System.out.println("주소: " + myInfo.getAddress());
            System.out.println("성별: " + myInfo.getSex());
        } else {
            System.out.println("정보를 불러오는데 실패했습니다.");
        }
    }
    // [회원 가입]
    private static void registerMember() {  
        while (true) {
            // 회원 정보 입력
            String id = getInput("아이디:");
            String password = getInput("비밀번호:");
            String memberName = getInput("이름:");
            String tel = getInput("전화번호:");
            String address = getInput("주소:");
            String sex = getInput("성별 (M/F):");
            
            // DTO에 회원 정보 설정
            UnifiedDTO member = new UnifiedDTO();
            member.setId(id);
            member.setPassword(password);
            member.setMemberName(memberName);
            member.setTel(tel);
            member.setAddress(address);
            member.setSex(sex);
    
            // 회원 가입 메뉴 출력 및 선택
            System.out.println("1. 가입");
            System.out.println("2. 다시 입력");
            System.out.println("3. 이전 화면으로");
            System.out.print("선택: ");
            int registerChoice = scanner.nextInt();
            scanner.nextLine(); // 입력버퍼 클리어
    
            switch (registerChoice) {
                case 1 -> {
                    controller.registerMember(member); // 가입 확정
                    return; // 함수 종료 (메인 메뉴로 돌아감)
                }
                case 2 -> System.out.println("다시 입력하세요."); // 다시 입력
                case 3 -> {
                    System.out.println("이전 화면으로 돌아갑니다.");
                    return; // 함수 종료 (메인 메뉴로 돌아감)
                }
                default -> System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }
    
    // [회원탈퇴]
    private static void secession() {
        System.out.println("회원탈퇴를 진행합니다. ");
        String memberId = getInput("아이디 : ");
        String pass = getInput("비밀번호 : ");
        String certpass = getInput("비밀번호 확인 : ");
        controller.deleteMember(memberId, pass, certpass);
    }
    // [로그인]
    private static void loginMember() {
        System.out.println("<<<로그인>>>");
        String id = getInput("아이디:");
        String password = getInput("비밀번호:");
        try {
            UnifiedDTO member = controller.login(id, password);
            if (member != null) {
                isAdmin = "Y".equals(member.getIsAdmin()); // 관리자인지 확인
                loggedInUserId = member.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 로그아웃
    private static void logout() {
        controller.logout(loggedInUserId);
        loggedInUserId = null; // 로그아웃 후 로그인 상태 초기화
        isAdmin = false; // 관리자 상태 초기화
        System.out.println("로그아웃되었습니다.");
    }
    //아이디찾기
    private static String findMemberId() {
        String memberName = getInput("이름:");
        String password = getInput("비밀번호:");
        String tel = getInput("전화번호:");
        try {
            String memberId = controller.findMemberId(memberName, password, tel);
            if (memberId != null) {
                System.out.println("아이디 찾기 성공: " + memberId);
                return memberId;
            } else {
                System.out.println("아이디 찾기 실패: 이름 또는 전화번호를 확인하세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 비밀번호 초기화
    private static void resetPassword() {
        String id = getInput("아이디:");
        String newPassword = getInput("새 비밀번호:");
        controller.resetPassword(id, newPassword);
    }
    // 프로그램 종료 : 스캐너 close 후 DB커넥션 해제 후 종료
    private static void programExit() {
        System.out.println("프로그램을 종료합니다");
        try { scanner.close(); conn.close(); // DB 연결 해제
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
  // 게시물 상세보기
private static void boardView() {
    List<UnifiedDTO> boardList = controller.getAllBoards(); // Controller에서 데이터 가져오기
    if (boardList == null || boardList.isEmpty()) {
        System.out.println("게시물이 없습니다.");
        return;
    }
    String strNo = getInput("상세보기를 원하는 게시물 번호?");
    try {
        final int no = Integer.parseInt(strNo);
        UnifiedDTO board = controller.getBoardById(no); // 게시물 조회
        
        if (board != null) { //게시글이 존재할 경우
            controller.incrementViewCount(no); // 조회수 1 증가
            detailView(board); // 상세보기 실행
        } else {
            System.out.println("게시물 번호가 존재하지 않습니다.");
        }
    } catch (NumberFormatException e) {
        System.out.println("올바른 번호를 입력하세요.");
    } catch (Exception e) {
        e.printStackTrace();
    }
} 

     // 게시물 상세정보 출력
     private static void detailView(UnifiedDTO board) {
        System.out.println("게시물 번호: " + board.getIdx());
        System.out.println("제목: " + board.getTitle());
        System.out.println("내용: " + board.getContent());
        System.out.println("작성자: " + board.getWriter());
        System.out.println("조회수: " + board.getViewCnt());
        System.out.println("등록일: " + board.getInsertDate());
        if (board.getUpdateDate() != null) {
            System.out.println("수정일: " + board.getUpdateDate());
        }
        if ("Y".equalsIgnoreCase(board.getDeleteYn())) {
            System.out.println("삭제된 게시물입니다.");
        }
    }
    //모든 게시글 목록 보기
    private static void getAllBoards() {
        List<UnifiedDTO> boardList = controller.getAllBoards(); // Controller에서 데이터 가져오기
        if (boardList == null || boardList.isEmpty()) {
            System.out.println("게시물이 없습니다.");
            return;
        }
        System.out.println("게시물 목록:");
        System.out.println("------------------------------------");
        for (UnifiedDTO board : boardList) {
            System.out.println("글 번호: " + board.getIdx());
            System.out.println("제목: " + board.getTitle());
            System.out.println("작성자: " + board.getWriter());
            System.out.println("조회수: " + board.getViewCnt());
            System.out.println("작성일: " + board.getInsertDate());
            System.out.println("------------------------------------");
        }
    }
    //게시글 등록
    private static void insertBoard() {
        String title = getInput("제목을 입력하세요: ");
        String content = getInput("내용을 입력하세요: ");
        String writer = getInput("작성자를 입력하세요: ");
        String password = getInput("수정/삭제시 사용할 비밀번호를 입력하세요: ");
        
        UnifiedDTO board = new UnifiedDTO();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        board.setPassword(password);
        // board.setInsertDate(new Timestamp(insertDate)); // 현재 날짜와 시간으로 설정
        controller.insertBoard(board);
    }
    
    // 게시글 삭제
    private static void deleteBoard() {
        int boardId = getInputInt("삭제할 게시글 번호를 입력하세요: ");
        UnifiedDTO board = controller.getBoardById(boardId);
    
        if (board == null) {
            System.out.println("게시글이 존재하지 않습니다.");
            return; // 이전 화면으로 돌아가기
        }
    
        boolean result = controller.deleteBoard(boardId);
        if (result) {
            System.out.println("게시글이 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("게시글 삭제에 실패했습니다.");
        }
    }

    // 게시글 수정
    private static void updateBoard() {
        int boardId = getInputInt("수정할 게시글 번호를 입력하세요: ");
        UnifiedDTO board = controller.getBoardById(boardId);
    
        if (board == null) {
            System.out.println("게시글이 존재하지 않습니다.");
            return; // 이전 화면으로 돌아가기
        }
    
        String newTitle = getInput("새로운 제목을 입력하세요: ");
        String newContent = getInput("새로운 내용을 입력하세요: ");
        
        board.setTitle(newTitle);
        board.setContent(newContent);
    
        boolean result = controller.updateBoard(board);
        if (result) {
            System.out.println("게시글이 성공적으로 수정되었습니다.");
        } else {
            System.out.println("게시글 수정에 실패했습니다.");
        }
    }
    /* 관리자 기능 : 모든 회원 조회 */
    private static void showMemberAll() {
        List<UnifiedDTO> memberList = controller.showMemberAll(); // Controller 클래스의 showMemberList 호출
    
        if (memberList == null || memberList.isEmpty()) {
            System.out.println("회원 목록이 없습니다.");
            return;
        }

    System.out.println("회원 목록:");
    System.out.println("------------------------------------");
    for (UnifiedDTO member : memberList) {
        System.out.println("아이디: " + member.getId());
        System.out.println("이름: " + member.getMemberName());
        System.out.println("전화번호: " + member.getTel());
        System.out.println("주소: " + member.getAddress());
        System.out.println("성별: " + member.getSex());
        System.out.println("------------------------------------");
    }
}
private static void Boards() {
    getAllBoards(); // 게시글 목록 조회

    while (true) {
        boardMenu(); // 게시글 관리 메뉴 출력
        int boardMenuChoice = scanner.nextInt();
        scanner.nextLine(); // 입력버퍼 클리어

        switch (boardMenuChoice) {
            case 1 -> boardView(); // 1. 게시글 상세보기
            case 2 -> updateBoard(); // 2. 게시글 수정
            case 3 -> deleteBoard(); // 3. 게시글 삭제
            case 4 -> {
                System.out.println("이전 메뉴로 돌아갑니다.");
                return; // '돌아가기' 선택 시 함수 종료
            }
            default -> System.out.println("올바른 번호를 선택하세요.");
        }
    }
}
    //공통 입력처리 함수 : 받은 문자열 출력후 스캐너입력을 대기한다.
    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    // 공통 입력처리 함수 : 받은 문자열 출력 후 받은 문자열을 정수형으로 변환하여 반환.
    private static int getInputInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("유효한 숫자를 입력하세요.");
            }
        }
    }
    private static void checkAdminStatus(){
        isAdmin = true;
        System.out.println("관리자 모드로 전환되었습니다.");
        return ;
    }
}