import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Connection conn;
    private static Controller controller;
    private static Scanner scanner = new Scanner(System.in);
    private static String loggedInUserId = null;
    private static boolean isAdmin = false;

    public static void main(String[] args) {
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
                int choice = getInputInt("선택>>: ");
                switch (choice) {
                    case 1 -> registerMember();
                    case 2 -> loginMember();
                    case 3 -> findMemberId();
                    case 4 -> resetPassword();
                    case 5 -> programExit();
                    case 666 -> checkAdminStatus();
                    default -> System.out.println("올바른 번호를 선택하세요.");
                }
            } else {
                showUserMenu();
                int choice = getInputInt("선택: ");
                switch (choice) {
                    case 1 -> showMyInfo();
                    case 2 -> insertBoard();
                    case 3 -> manageBoards();
                    case 4 -> {
                        if (isAdmin) {
                            showMemberAll();
                        } else {
                            System.out.println("접근 권한이 없습니다.");
                        }
                    }
                    case 5 -> logout();
                    case 6 -> secession();
                    case 7 -> {
                        logout();
                        programExit();
                    }
                    default -> System.out.println("올바른 번호를 선택하세요.");
                }
            }
        }
    }

    private static void showUserMenu() {
        System.out.println("1. 나의 정보 확인");
        System.out.println("2. 게시글 등록");
        System.out.println("3. 게시판 보기");
        System.out.println("4. 회원 목록(관리자 전용)");
        System.out.println("5. 로그아웃");
        System.out.println("6. 회원탈퇴");
        System.out.println("7. 종료");
    }

    private static void showMainMenu() {
        System.out.println("1. 회원 가입");
        System.out.println("2. 로그인");
        System.out.println("3. 아이디 찾기");
        System.out.println("4. 비밀번호 초기화");
        System.out.println("5. 종료");
    }

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

    private static void loginMember() {
        System.out.println("<<<로그인>>>");
        String id = getInput("아이디:");
        String password = getInput("비밀번호:");
        UnifiedDTO member = controller.login(id, password);
        if (member != null) {
            isAdmin = "Y".equals(member.getIsAdmin());
            loggedInUserId = member.getId();
        } 
    }

    private static void findMemberId() {
        String memberName = getInput("이름:");
        String password = getInput("비밀번호:");
        String tel = getInput("전화번호:");
        controller.findMemberId(memberName, password, tel);
    }

    private static void resetPassword() {
        String id = getInput("아이디:");
        String oldPassword = getInput("현재 비밀번호:");
        String tel = getInput("등록된 전화번호:");

        if (controller.verifyUser(id, oldPassword, tel)) {
            String newPassword = getInput("새 비밀번호:");
            controller.resetPassword(id, newPassword);
            System.out.println("비밀번호가 초기화되었습니다. :"+newPassword);
        } else {
            System.out.println("본인 인증에 실패했습니다. 정보를 다시 확인해주세요.");
        }
    }

    private static void secession() {
        String pass = getInput("비밀번호:");
        String certpass = getInput("비밀번호 확인:");
        controller.deleteMember(loggedInUserId, pass, certpass);
        logout();
    }

    private static void logout() {
        controller.logout(loggedInUserId);
        loggedInUserId = null;
        isAdmin = false;
    }

    private static void showMyInfo() {
        UnifiedDTO myInfo = controller.getMyInfo(loggedInUserId);
        if (myInfo != null) {
            System.out.println("아이디: " + myInfo.getId());
            System.out.println("이름: " + myInfo.getMemberName());
            System.out.println("전화번호: " + myInfo.getTel());
            System.out.println("주소: " + myInfo.getAddress());
            System.out.println("성별: " + myInfo.getSex());
        }
    }

    private static void insertBoard() {
        String title = getInput("제목을 입력하세요: ");
        String content = getInput("내용을 입력하세요: ");
        String writer = getInput("작성자를 입력하세요: ");
        String boardPassword = getInput("수정/삭제시 사용할 비밀번호를 입력하세요:");

        UnifiedDTO board = new UnifiedDTO();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        board.setBoardPassword(boardPassword);

        controller.insertBoard(board);
    }

    private static void deleteBoard() {
        int boardId = getInputInt("삭제할 게시글 번호를 입력하세요: ");
        controller.deleteBoard(boardId);
    }

    private static void updateBoard() {
        int boardId = getInputInt("수정할 게시글 번호를 입력하세요: ");
        UnifiedDTO board = controller.getBoardById(boardId);

        if (board != null) {
            String newTitle = getInput("새로운 제목을 입력하세요: ");
            String newContent = getInput("새로운 내용을 입력하세요: ");
            String newBoardPassword = getInput("새로운 게시물 비밀번호를 입력하세요 ");

            board.setTitle(newTitle);
            board.setContent(newContent);
            board.setBoardPassword(newBoardPassword);

            controller.updateBoard(board);
        }
    }

    private static void boardView() {
        int boardId = getInputInt("상세보기를 원하는 게시물 번호?");
        UnifiedDTO board = controller.getBoardById(boardId);
        if (board != null) {
            controller.incrementViewCount(boardId);
            detailView(board);
        }
    }

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

    private static void showMemberAll() {
        List<UnifiedDTO> memberList = controller.showMemberAll();
        if (memberList != null && !memberList.isEmpty()) {
            System.out.println("회원 목록:");
            for (UnifiedDTO member : memberList) {
                System.out.println("아이디: " + member.getId());
                System.out.println("이름: " + member.getMemberName());
                System.out.println("전화번호: " + member.getTel());
                System.out.println("주소: " + member.getAddress());
                System.out.println("성별: " + member.getSex());
                System.out.println("------------------------------------");
            }
        }
    }

    private static void manageBoards() {
        getAllBoards();
        while (true) {
            boardMenu();
            int boardMenuChoice = getInputInt("선택: ");
            switch (boardMenuChoice) {
                case 1 -> boardView();
                case 2 -> updateBoard();
                case 3 -> deleteBoard();
                case 4 -> {
                    System.out.println("이전 메뉴로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }

    private static void getAllBoards() {
        List<UnifiedDTO> boardList = controller.getAllBoards();
        if (boardList == null || boardList.isEmpty()) {
            System.out.println("게시물이 없습니다.");
            return;
        }

        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) boardList.size() / pageSize);
        int currentPage = 1;

        while (true) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, boardList.size());

            System.out.printf("%-4s | %-3s | %-23s | %-6s | %-10s%n", "No.", "작성자", "제목", "읽은수", "작성일");
            System.out.println("----------------------------------------------------------------------------------------");

            for (int i = startIndex; i < endIndex; i++) {
                UnifiedDTO board = boardList.get(i);
                System.out.printf("%-4d | %-7s | %-25s | %-6d | %-20s%n",
                        board.getIdx(), board.getWriter(), truncateString(board.getTitle(), 25),
                        board.getViewCnt(), board.getInsertDate().toString());
            }

            System.out.printf("현재 페이지: %d/%d (페이지이동: [페이지숫자], 이전 페이지: p, 다음 페이지: n, 이전메뉴로: q): ", currentPage, totalPages);
            String input = getInput("");

            switch (input.toLowerCase()) {
                case "n" -> {
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("마지막 페이지입니다.");
                    }
                }
                case "p" -> {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("첫 페이지입니다.");
                    }
                }
                case "q" -> {
                    return;
                }
                default -> {
                    try {
                        int page = Integer.parseInt(input);
                        if (page >= 1 && page <= totalPages) {
                            currentPage = page;
                        } else {
                            System.out.println("유효한 페이지 번호가 아닙니다.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("잘못된 입력입니다. 숫자 또는 'p', 'n', 'q' 중 하나를 입력하세요.");
                    }
                }
            }
        }
    }

    private static String truncateString(String str, int length) {
        return str.length() > length ? str.substring(0, length) + "..." : str;
    }

    private static void boardMenu() {
        System.out.println("1. 게시글 상세보기");
        System.out.println("2. 게시글 수정");
        System.out.println("3. 게시글 삭제");
        System.out.println("4. 돌아가기");
    }

    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

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

    private static void checkAdminStatus() {
        isAdmin = true;
        System.out.println("관리자 모드로 전환되었습니다.");
    }

    private static void programExit() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("프로그램을 종료합니다.");
            System.exit(0);
        }
    }
}