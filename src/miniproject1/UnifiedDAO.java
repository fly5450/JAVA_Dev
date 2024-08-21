import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UnifiedDAO {
    private Connection conn;

    public UnifiedDAO(Connection conn) {
        this.conn = conn;
    }

    // 회원 가입
    public int registerMember(UnifiedDTO member) {
        String sql = "INSERT INTO MemberInfo (ID, PASSWORD, MEMBERNAME, TEL, ADDRESS, SEX) " 
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
                System.out.println("!!가입불가!! 관리자에게 문의하세요");
            } else {
                e.printStackTrace();
                System.out.println(e.getErrorCode());
            }
            return 0;
        }
    }

    // 로그인 검증
    public UnifiedDTO login(String id, String password) {
        String sql = "SELECT * FROM MemberInfo WHERE ID = ? AND PASSWORD = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                UnifiedDTO member = new UnifiedDTO();
                member.setId(rs.getString("ID"));
                member.setPassword(rs.getString("PASSWORD"));
                member.setMemberName(rs.getString("MEMBERNAME"));
                member.setTel(rs.getString("TEL"));
                member.setAddress(rs.getString("ADDRESS"));
                member.setSex(rs.getString("SEX"));
                
                return member;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 로그인 기록 및 회원 테이블에 로그인 시간 업데이트
    public void recordLogin(String memberId) {
        String sqlLog = "UPDATE MemLog SET logout_date = ? WHERE logid = ? AND logout_date IS NULL";
        String sqlUpdateMember = "UPDATE MemberInfo SET last_login_date = ? WHERE logid = ?";
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

    // 로그아웃 기록 및 회원 테이블에 로그아웃 시간 업데이트
    public void recordLogout(String memberId) {
        String sqlLog = "UPDATE MemLog SET logout_date = ? WHERE logid = ? AND logout_date IS NULL";
        String sqlUpdateMember = "UPDATE MemberInfo SET last_logout_date = ? WHERE logid = ?";
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

    // 아이디 찾기 - PL/SQL 프로시저 호출
    public String findMemberId(String memberName, String password, String tel) {
        String memberId = null;
        String sql = "{call FIND_MEMBER_ID(?, ?, ?, ?)}";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, memberName);
            cstmt.setString(2, password);
            cstmt.setString(3, tel);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.execute();
            memberId = cstmt.getString(4);
        } catch (Exception e) {
            System.err.println("Service.findMemberId 오류: " + e.getMessage());
            e.printStackTrace();
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

    // 현재 비밀번호 확인
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

    // 회원 정보 조회
    public UnifiedDTO getMemberById(String memberId) {
        String sql = "SELECT id, membername, tel, address, sex FROM MemberInfo WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UnifiedDTO member = new UnifiedDTO();
                member.setId(rs.getString("id"));
                member.setMemberName(rs.getString("membername"));
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

    // 회원삭제시 삭제여부 필드 변경
    public boolean setDeleteYn(String id, boolean deleteYn) {
        String sql = "UPDATE MemberInfo SET deleteYn = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deleteYn ? "Y" : "N");
            pstmt.setString(2, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 게시물 등록
    public int insertBoard(UnifiedDTO board) {
        String sql = "INSERT INTO board (title, content, writer, insert_date, BOARDPASSWORD) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setString(3, board.getWriter());
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setString(5, board.getBoardPassword());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 게시물 수정
    public int updateBoard(UnifiedDTO board) {
        String sql = "UPDATE board SET title = ?, content = ?, writer = ?, "
                   + "update_date = ? WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setString(3, board.getWriter());
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setInt(5, board.getIdx());
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

    // 게시물 상세보기
    public UnifiedDTO getBoardById(int no) {
        String sql = "SELECT * FROM board WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, no);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UnifiedDTO board = new UnifiedDTO();
                    board.setIdx(rs.getInt("IDX"));
                    board.setTitle(rs.getString("TITLE"));
                    board.setContent(rs.getString("CONTENT"));
                    board.setWriter(rs.getString("WRITER"));
                    board.setViewCnt(rs.getInt("VIEWCNT"));
                    board.setInsertDate(rs.getDate("INSERT_DATE"));
                    board.setUpdateDate(rs.getDate("UPDATE_DATE"));
                    return board;
                } else {
                    System.out.println("게시물 번호가 존재하지 않습니다: " + no);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 게시물 목록 조회
    public List<UnifiedDTO> getAllBoards() {
        List<UnifiedDTO> boardList = new ArrayList<>();
        String sql = "SELECT idx, title, content, writer, viewcnt, insert_date, update_date FROM board";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UnifiedDTO board = new UnifiedDTO();
                board.setIdx(rs.getInt("idx"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setViewCnt(rs.getInt("viewcnt"));
                board.setInsertDate(rs.getDate("insert_date"));
                board.setUpdateDate(rs.getDate("update_date"));
                boardList.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardList;
    }

    // 게시물 조회수 증가
    public void incrementViewCount(int no) {
        String sql = "UPDATE board SET viewcnt = viewcnt + 1 WHERE idx = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, no);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 관리자 여부 확인
    public boolean checkAdminStatus(String id) {
        String sql = "SELECT isadminYn FROM MemberInfo WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "Y".equalsIgnoreCase(rs.getString("isadminYn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 모든 회원 조회 (관리자 전용)
    public List<UnifiedDTO> showMemberAll() {
        List<UnifiedDTO> memberList = new ArrayList<>();
        String sql = "SELECT id, membername, tel, address, sex FROM MemberInfo";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UnifiedDTO member = new UnifiedDTO();
                member.setId(rs.getString("id"));
                member.setMemberName(rs.getString("membername"));
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
}
