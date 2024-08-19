import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
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
                case 1 -> registerMember();
                case 2 -> loginMember();
                case 3 -> findMemberId();
                case 4 -> resetPassword();
                case 5 -> {
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    try {
                        conn.close(); // DB 연결 해제
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                default -> System.out.println("올바른 번호를 선택하세요.");
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

    //로그인
    private static void loginMember() {
        System.out.println("<<<로그인>>>");
        System.out.print("아이디:");
        String id = scanner.nextLine();
        System.out.print("비밀번호:");
        String password = scanner.nextLine();
    
        try {
            String memberName = controller.login(id, password);
            if (memberName != null) {
                System.out.println("로그인 성공: " + memberName + "님 환영합니다.");
            } else {
                System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //로그인 - PL/SQL 함수 호출
    public String loginMember(String id, String password) {
        String memberName = null;
        String sql = "{ ? = call login_member(?, ?) }";  // PL/SQL 함수 호출

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            // 출력 파라미터 설정
            cstmt.registerOutParameter(1, Types.VARCHAR);

            // 입력 파라미터 설정
            cstmt.setString(2, id);
            cstmt.setString(3, password);

            // 함수 실행
            cstmt.execute();

            // 출력 파라미터 값 가져오기
            memberName = cstmt.getString(1);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberName;
    }
  
    private static String findMemberId() {
        System.out.println("아이디를 찾기 위해 이름/비밀번호/전화번호를 입력하세요:");
        System.out.print("이름:");
        String memberName = scanner.nextLine();
        System.out.print("비밀번호:");
        String password = scanner.nextLine();
        System.out.print("전화번호:");
        String tel = scanner.nextLine();
        
        try {
            String memberId = controller.findMemberId(memberName, password, tel); 
            if (memberId != null) {
                System.out.println("아이디 찾기 성공: " + memberId);
                return memberId; // 아이디를 반환
            } else {
                System.out.println("아이디 찾기 실패: 이름 또는 전화번호를 확인하세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null; // 실패 시 null 반환
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