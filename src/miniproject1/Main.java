import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static Connection conn; // Connection 객체 생성
    private static Controller controller;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user01", "5450");
            controller = new Controller(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 줄바꿈 문자 처리

            switch (choice) {
                //회원가입
                case 1:
                    registerMember();
                    break;
                //로그인
                case 2:
                    loginMember();
                    break;
                //아이디찾기
                case 3:
                    findMemberId();
                    break;
                //비밀번호 초기화
                case 4:
                    resetPassword();
                    break;
                //종료
                case 5:
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    try {
                        conn.close(); // DB 연결 해제
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
        System.out.print("선택: ");
    }

    // 회원 가입
    private static void registerMember() {
        System.out.println("아이디:");
        String id = scanner.nextLine();
        System.out.println("비밀번호:");
        String password = scanner.nextLine();
        System.out.println("이름:");
        String memberName = scanner.nextLine();
        System.out.println("전화번호:");
        String tel = scanner.nextLine();
        System.out.println("주소:");
        String address = scanner.nextLine();
        System.out.println("성별 (M/F):");
        String sex = scanner.nextLine();

        //입력값을 DTO객체에게 전달
        UnifiedDTO member = new UnifiedDTO();
        member.setId(id);
        member.setPassword(password);
        member.setMemberName(memberName);
        member.setTel(tel);
        member.setAddress(address);
        member.setSex(sex);
        //DTO는 Controller 호출
        controller.registerMember(member);
    }

    // 로그인
    private static void loginMember() {
        System.out.println("<<<로그인>>>");
        System.out.println("아이디:");
        String id = scanner.nextLine();
        System.out.println("비밀번호:");
        String password = scanner.nextLine();
        UnifiedDTO member = controller.login(id, password);
    }

    // 아이디 찾기
    private static void findMemberId() {
        System.out.println("이름:");
        String memberName = scanner.nextLine();
        System.out.println("비밀번호:");
        String password = scanner.nextLine();
        System.out.println("전화번호:");
        String tel = scanner.nextLine();

        controller.findMemberId(memberName, tel);
    }

    // 비밀번호 초기화
    private static void resetPassword() {
        System.out.println("아이디:");
        String id = scanner.nextLine();
        System.out.println("새 비밀번호:");
        String newPassword = scanner.nextLine();

        controller.resetPassword(id, newPassword);
    }
}