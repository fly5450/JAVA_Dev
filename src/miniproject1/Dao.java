
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//  DB (Model)에 접근하여 쿼리 실행을 하는 객체.
public class Dao {
    private static Connection conn;

    public Dao(Connection conn) {
        this.conn = conn;
    }

    // public Dao() {
    //     try {
    //         // JDBC 드라이버 로드
    //         Class.forName("oracle.jdbc.driver.OracleDriver");
    //         // Oracle DB와의 연결 설정
    //         conn = DriverManager.getConnection(
    //                 "jdbc:oracle:thin:@localhost:1521:xe", "user01", "5450");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

/*---------------------------------------------------------------------------------------------------------------
     시큐어코딩 준수 : prepareStatement 사용하여 sql 인젝션 공격 취약점 대비와 성능 향상. 컴파일 1회 - 실행 여러번
    
----------------------------------------------------------------------------------------------------------------*/
//MemberInfo 테이블 관련 CRUD 메서드-
// 회원 가입
public int registerMember(Dto member) {
    String sql = "INSERT INTO MemberInfo (ID, PASSWORD, MEMBER_NAME, TEL, ADDRESS, SEX) " //실행할 쿼리 선언
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
        e.printStackTrace();
        return 0;
    }
}
 // 로그인
 public Dto login(String id, String password) {
    String sql = "SELECT * FROM MemberInfo WHERE ID = ? AND PASSWORD = ?"; //실행할 쿼리 선언
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, id);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Dto member = new Dto();
            member.setId(rs.getString("ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setMemberName(rs.getString("MEMBER_NAME"));
            member.setTel(rs.getString("TEL"));
            member.setAddress(rs.getString("ADDRESS"));
            member.setSex(rs.getString("SEX"));
            return member;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
// Board 테이블 관련 CRUD 메서드 | 등록, 조회, 수정, 삭제 |
  //[게시글 등록] 게시물 등록시 입력 항목 : 제목, 내용, 수정/삭제시 사용할 비밀번호으로 한다.
  public static int insertBoard(Dto board) {
    String sql = "INSERT INTO board (title, content, writer, view_cnt, delete_yn, insert_date) "
               + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, board.getTitle());
        pstmt.setString(2, board.getContent());
        pstmt.setString(3, board.getWriter());
        pstmt.setInt(4, board.getViewCnt());
        pstmt.setString(5, board.getDeleteYn());
        pstmt.setDate(6, new java.sql.Date(board.getInsertDate().getTime()));
        return pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}
    // [모든 게시물을 조회]
    public List<Dto> getAllBoards() {
        String sql = "SELECT * FROM board";
        List<Dto> boardList = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Dto board = new Dto();
                board.setIdx(rs.getInt("idx"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setViewCnt(rs.getInt("view_cnt"));
                board.setDeleteYn(rs.getString("delete_yn"));
                board.setInsertDate(rs.getDate("insert_date"));
                board.setUpdateDate(rs.getDate("update_date"));
                board.setDeleteDate(rs.getDate("delete_date"));
                boardList.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardList;
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

   // [게시물을 수정]
   public int updateBoard(Dto board) {
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
    // 게시물 삭제
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
}