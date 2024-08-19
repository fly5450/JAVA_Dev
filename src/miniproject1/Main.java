import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

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
                scanner.nextLine(); // 입력버퍼 클리어
                switch (choice) {
                    case 1 -> registerMember();
                    case 2 -> loginMember();
                    case 3 -> findMemberId();
                    case 4 -> resetPassword();
                    case 5 -> { logout(); programExit();}
                    default -> System.out.println("올바른 번호를 선택하세요.");
                }
            } else {
                showUserMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // 입력버퍼 클리어
                switch (choice) {
                    case 1 -> showMyInfo(); //1.나의정보보기
                    case 2 -> getAllBoards();  //2. 게시물 목록
                    case 3 -> {             //3. 회원 목록(관리자인경우)
                        if (isAdmin) {showMemberAll();}
                        else { System.out.println("접근 권한이 없습니다."); }}
                    case 4 -> logout(); //4. 로그아웃
                    case 5 -> secession(); //5. 회원탈퇴
                    case 6 -> programExit(); //6. 종료
                    case 666 -> { isAdmin = true;  System.out.println("관리자 모드로 전환되었습니다.");}
                    default -> System.out.println("올바른 번호를 선택하세요.");
                }
            }
        }
    }
    //[회원탈퇴]
    private static void secession() {
        String memberId = getInput("아이디 : ");
        String pass = getInput("비밀번호 : ");
        String certpass = getInput("비밀번호 확인 : ");
        controller.deleteMember(memberId, pass, certpass);
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
        String id = getInput("아이디:");
        String password = getInput("비밀번호:");
        String memberName = getInput("이름:");
        String tel = getInput("전화번호:");
        String address = getInput("주소:");
        String sex = getInput("성별 (M/F):");
        UnifiedDTO member = new UnifiedDTO();
        member.setId(id);
        member.setPassword(password);
        member.setMemberName(memberName);
        member.setTel(tel);
        member.setAddress(address);
        member.setSex(sex);
        controller.registerMember(member);
    }
    //[유저로그인 메뉴 ]
    private static void showUserMenu(){
        System.out.println("1. 나의 정보 확인");
        System.out.println("2. 게시물 목록");
        System.out.println("3. 회원 목록(관리자인 경우)");
        System.out.println("4. 로그아웃");
        System.out.println("5. 종료");
        System.out.print("선택: ");
    }
    // 로그인
    private static void loginMember() {
        System.out.println("<<<로그인>>>");
        String id = getInput("아이디:");
        String password = getInput("비밀번호:");
        try {
            String memberName = controller.login(id, password);
            if (memberName != null) {
                isAdmin = controller.isAdmin(id); // 관리자인지 확인하여 isAdmin 변수 설정
                if (isAdmin) {
                    System.out.println("관리자 로그인 성공: " + memberName + "님 환영합니다.");
                } else {
                    System.out.println("로그인 성공: " + memberName + "님 환영합니다.");
                }
                loggedInUserId = id; // 로그인된 사용자 ID 설정
            } else {
                System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
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
        scanner.close();
        try {
            conn.close(); // DB 연결 해제
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
    //공통 입력처리 함수 : 받은 문자열 출력후 스캐너입력을 대기한다.
    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}