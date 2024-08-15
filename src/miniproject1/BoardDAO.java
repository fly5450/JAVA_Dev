package miniproject1;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// DAO를 통해 DB (Model)에 접근.
public class BoardDAO {
    private Connection conn;


    public BoardDAO() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "user01", "5450");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //모든 게시글 목록 
    public List<BoardDTO> getAllBoards() {
        String sql = "-----------게시글을 선택하세요.----------";
        List<BoardDTO> boardList = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { //커서 
                BoardDTO board = new BoardDTO();
                board.setId(rs.getInt("No"));
                board.setTitle(rs.getString("제목"));
                board.setContent(rs.getString("내용"));
                board.setWriter(rs.getString("작성자"));
                board.setCreateddate(rs.getDate("작성일"));
                boardList.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardList;
    }
    /*---------------------------------------------------------------------------------------------------------------
    [게시글 등록 기능] 게시물 등록시 입력 항목 : 제목, 내용, 수정/삭제시 사용할 비밀번호으로 한다.
    시큐어코딩 준수 : prepareStatement 사용하여 sql 인젝션 공격 취약점 대비.
	----------------------------------------------------------------------------------------------------------------*/
   public int insertBoard(BoardDTO board) {
       String sql = "INSERT INTO BOARD (TITLE, CONTENT, WRITER, CREATED_DATE, LOGIN_DATE) VALUES (?, ?, ?, ?, ?, ?)";
       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(2, board.getTitle());
           pstmt.setString(3, board.getContent());
           pstmt.setString(4, board.getWriter());
           pstmt.setDate(5, board.getCreatedDate()); //여기 뭐지
           pstmt.setDate(6, new Date(board.getLoginDate() ).getTime()));
           return pstmt.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
           return 0;
       }
   }

    // Update, Delete methods 추가 가능
}