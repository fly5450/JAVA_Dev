import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
//  DB (Model)에 접근하여 쿼리 실행을 하는 객체.
public class UnifiedDAO  {
    public Connection conn;

    public UnifiedDAO(Connection conn) {
        this.conn = conn;
    }

    public UnifiedDAO() {
        try {
            // JDBC 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Oracle DB와의 연결 설정
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "user01", "5450");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*---------------------------------------------------------------------------------------------------------------
     시큐어코딩 준수 : prepareStatement 사용하여 sql 인젝션 공격 취약점 대비와 성능 향상. 컴파일 1회 - 실행 여러번
----------------------------------------------------------------------------------------------------------------*/
//MemberInfo 테이블 관련 CRUD 메서드-
// [회원 가입]
public int registerMember(UnifiedDTO member) {
    String sql = "INSERT INTO MemberInfo (ID, PASSWORD, MEMBER_NAME, TEL, ADDRESS, SEX) " 
               + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, member.getId());
        pstmt.setString(2, member.getPassword());
        pstmt.setString(3, member.getMemberName());
        pstmt.setString(4, member.getTel());
        pstmt.setString(5, member.getAddress());
        pstmt.setString(6, member.getSex());
        return pstmt.executeUpdate();
    } catch (SQLException e) {
        if (e.getErrorCode() == 1) { // ORA-00001: unique constraint violated
            System.out.println("가입불가!!");
        } else {
            e.printStackTrace();
            System.out.println(e.getErrorCode());
        }
        return 0;
    }
}
 // [로그인]
 public UnifiedDTO  login(String id, String password) {
    String sql = "SELECT * FROM MemberInfo WHERE ID = ? AND PASSWORD = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, id);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            // 로그인 성공 시 사용자 정보를 DTO에 담는다.
            UnifiedDTO member = new UnifiedDTO();
            member.setId(rs.getString("ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setMemberName(rs.getString("MEMBER_NAME"));
            member.setTel(rs.getString("TEL"));
            member.setAddress(rs.getString("ADDRESS"));
            member.setSex(rs.getString("SEX"));
            
            // 로그인 성공 시 사용자 객체반환
            return member;
        } else {
            // 로그인 실패 시 null 반환
            return null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

    //---------------------------------------------------------------------------------------------------------------------------//
// 로그인 시 로그 기록 및 회원 테이블에 로그인 시간 업데이트
public void recordLogin(String memberId) {
    String sqlLog = "INSERT INTO MemberLog (id, login_date) VALUES (?, ?)";
    String sqlUpdateMember = "UPDATE MemberInfo SET last_login_date = ? WHERE id = ?";
    Timestamp now = new Timestamp(System.currentTimeMillis());

    try (PreparedStatement pstmtLog = conn.prepareStatement(sqlLog);
         PreparedStatement pstmtMember = conn.prepareStatement(sqlUpdateMember)) {
        pstmtLog.setString(1, memberId);
        pstmtLog.setTimestamp(2, now);
        pstmtLog.executeUpdate();

        pstmtMember.setTimestamp(1, now);
        pstmtMember.setString(2, memberId);
        pstmtMember.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
 // 로그아웃 시 이력 기록 및 회원 테이블 업데이트
 public void recordLogout(String memberId) {
    String sqlLog = "UPDATE MemberLog SET logout_date = ? WHERE id = ? AND logout_date IS NULL";
    String sqlUpdateMember = "UPDATE MemberInfo SET last_logout_date = ? WHERE id = ?";
    Timestamp now = new Timestamp(System.currentTimeMillis());

    try (PreparedStatement pstmtLog = conn.prepareStatement(sqlLog);
         PreparedStatement pstmtMember = conn.prepareStatement(sqlUpdateMember)) {
        pstmtLog.setTimestamp(1, now);
        pstmtLog.setString(2, memberId);
        pstmtLog.executeUpdate();

        pstmtMember.setTimestamp(1, now);
        pstmtMember.setString(2, memberId);
        pstmtMember.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//---------------------------------------------------------------------------------------------------------------------------//
  // 아이디 찾기
//   public String findMemberId(String id,String password, String tel) {
//     String sql = "SELECT ID FROM MemberInfo WHERE MEMBER_NAME = ? AND TEL = ?";
//     try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//         pstmt.setString(1, id);
//         pstmt.setString(2, password);
//         pstmt.setString(3, tel);
//         ResultSet rs = pstmt.executeQuery();
//         if (rs.next()) {
//             return rs.getString("ID");
//         }
//     } catch (SQLException e) {
//         e.printStackTrace();
//     }
//     return null;
// }
 // 아이디 찾기 - PL/SQL 프로시저 호출
 public String findMemberId(String memberName, String password, String tel) {
    String memberId = null;
    String sql = "{call FIND_MEMBER_ID(?, ?, ?, ?)}";

    try (CallableStatement cstmt = conn.prepareCall(sql)) {
        // 입력 파라미터 설정
        cstmt.setString(1, memberName);
        cstmt.setString(2, password);
        cstmt.setString(3, tel);
        // 출력 파라미터 설정
        cstmt.registerOutParameter(4, Types.VARCHAR);
        // 프로시저 실행
        cstmt.execute();

        // 출력 파라미터 값 가져오기
        memberId = cstmt.getString(4);
    } catch (Exception e) {
       System.err.println("Service.findMemberId 오류: " + e.getMessage());
            e.printStackTrace();
            return null;
    }
    return memberId;
}
 // 비밀번호 초기화
 public int resetPassword(String id, String newPassword) {
    String sql = "UPDATE MemberInfo SET PASSWORD = ? WHERE ID = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, newPassword);
        pstmt.setString(2, id);
        return pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}
 // 현재 비밀번호 확인 :  memberId를 기반으로 데이터베이스에서 현재 비밀번호를 가져온다.
  public String getPasswordById(String memberId) {
    String sql = "SELECT password FROM MemberInfo WHERE ID = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, memberId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("password");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }
//---------------------------------------------------------------------------------------------------------------------------//
// Board 테이블 관련 CRUD 메서드 | 등록, 조회, 수정, 삭제 |
  //[게시글 등록] 게시물 등록시 입력 항목 : 제목, 내용, 수정/삭제시 사용할 비밀번호으로 한다.
  public int insertBoard(UnifiedDTO board) {
    String sql = "INSERT INTO board (title, content, writer, view_cnt, insert_date) "
               + "VALUES (?, ?, ?, 0, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, board.getTitle());
        pstmt.setString(2, board.getContent());
        pstmt.setString(3, board.getWriter());
        
        // insertDate가 null인 경우 현재 시간을 기본값으로 설정
        if (board.getInsertDate() == null) {
            board.setInsertDate(new java.sql.Date(System.currentTimeMillis()));
        }
        pstmt.setDate(4, new java.sql.Date(board.getInsertDate().getTime()));
        
        return pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}

   // [게시물 수정]
   public int updateBoard(UnifiedDTO board) {
    String sql = "UPDATE board SET title = ?, content = ?, writer = ?, view_cnt = ?, "
               + "delete_yn = ?, update_date = ? WHERE idx = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, board.getTitle());
        pstmt.setString(2, board.getContent());
        pstmt.setString(3, board.getWriter());
        pstmt.setInt(4, board.getViewCnt());
        pstmt.setString(5, board.getDeleteYn());
        pstmt.setDate(6, new java.sql.Date(board.getUpdateDate().getTime()));
        pstmt.setInt(7, board.getIdx());
        return pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
        }
    }
    // [게시물 삭제]
    public int deleteBoard(int idx) {
        String sql = "DELETE FROM board WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idx);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

     // 게시물 상세보기
     public UnifiedDTO boardView(int idx) {
        String sql = "SELECT * FROM board WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idx);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UnifiedDTO board = new UnifiedDTO();
                board.setIdx(rs.getInt("idx"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setViewCnt(rs.getInt("view_cnt"));
                // board.setDeleteYn(rs.getString("delete_yn"));
                board.setInsertDate(rs.getDate("insert_date"));
                board.setUpdateDate(rs.getDate("update_date"));
                // board.setDeleteDate(rs.getDate("delete_date"));
                return board;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //게시글이 존재하지않을 경우 null 리턴
    }
    //게시글 조회시 ViewConut +1 증가
    public void incrementViewCount(int no) {
        String sql = "UPDATE board SET view_cnt = view_cnt + 1 WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, no);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //행넘버 조회
    public UnifiedDTO getBoardById(int no) {
        String sql = "SELECT * FROM board WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, no);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UnifiedDTO board = new UnifiedDTO();
                    board.setIdx(rs.getInt("idx"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setWriter(rs.getString("writer"));
                    board.setViewCnt(rs.getInt("view_cnt"));
                    board.setInsertDate(rs.getDate("insert_date"));
                    board.setUpdateDate(rs.getDate("update_date"));
                    board.setDeleteYn(rs.getString("delete_yn"));
                    return board;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 게시물 번호가 존재하지 않을 경우
    }
    
    public List<UnifiedDTO> getAllBoards() {
        List<UnifiedDTO> boardList = new ArrayList<>();
        String sql = "SELECT * FROM board";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                UnifiedDTO board = new UnifiedDTO();
                board.setIdx(rs.getInt("idx"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setViewCnt(rs.getInt("view_cnt"));
                board.setInsertDate(rs.getDate("insert_date"));
                board.setUpdateDate(rs.getDate("update_date"));
                board.setDeleteYn(rs.getString("delete_yn"));
                boardList.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardList;
    }
     // 내정보보기
     public UnifiedDTO getMemberById(String memberId) {
        String sql = "SELECT id, member_name, tel, address, sex FROM MemberInfo WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UnifiedDTO member = new UnifiedDTO();
                member.setId(rs.getString("id"));
                member.setMemberName(rs.getString("member_name"));
                member.setTel(rs.getString("tel"));
                member.setAddress(rs.getString("address"));
                member.setSex(rs.getString("sex"));
                return member;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //삭제여부필드 변경
    public boolean setDeleteYn(String id, boolean deleteYn) {
        String sql = "UPDATE MemberInfo SET deleteYn = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Y");
            pstmt.setString(2, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//---------------------------------------------------------------------------------------------------------------------------//
    // [관리자 기능: 모든 회원 목록 조회]
    public List<UnifiedDTO> showMemberAll() {
        List<UnifiedDTO> memberList = new ArrayList<>();
        String sql = "SELECT id, member_name, tel, address, sex FROM MemberInfo";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UnifiedDTO member = new UnifiedDTO();
                member.setId(rs.getString("id"));
                member.setMemberName(rs.getString("member_name"));
                member.setTel(rs.getString("tel"));
                member.setAddress(rs.getString("address"));
                member.setSex(rs.getString("sex"));
                memberList.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return memberList;
    }
   
    //관리자ID인지 쿼리를 날려 확인하는 함수
    public boolean isAdmin(String id) {
        String sql = "SELECT isadminYn FROM MemberInfo WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String isAdmin = rs.getString("isadminYn");
                return "Y".equalsIgnoreCase(isAdmin); // 'Y'이면 관리자
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
}