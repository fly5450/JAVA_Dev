package miniproject1;

import java.sql.Connection;
import java.util.List;

public class Controller {
    private Service service;

    public Controller(Connection conn) {
        this.service = new Service(conn);
    }

    // 회원 가입
    public void registerMember(UnifiedDTO member) {
        service.registerMember(member);
    }

    // 로그인
    public UnifiedDTO login(String id, String password) {
        return service.loginMember(id, password);
        
    }

    // 로그아웃
    public void logout(String memberId) {
        service.logoutMember(memberId);
    }

    // 아이디 찾기
    public String findMemberId(String memberName, String password, String tel) {
        return service.findMemberId(memberName, password, tel);
    }

    // 회원 탈퇴
    public boolean deleteMember(String memberId, String password, String certPassword) {
        return service.deleteMember(memberId, password, certPassword);
    }

    // 비밀번호 초기화
    public void resetPassword(String id, String newPassword) {
        service.resetPassword(id, newPassword);
    }

    // 본인 인증
    public boolean verifyUser(String id, String password, String tel) {
        return service.verifyUser(id, password, tel);
    }

    // 내 정보 보기
    public UnifiedDTO getMyInfo(String userId) {
        return service.getMemberById(userId);
    }

    // 모든 게시물 조회
    public List<UnifiedDTO> getAllBoards() {
        return service.getAllBoards();
    }

    // 게시물 추가
    public void insertBoard(UnifiedDTO board) {
        service.insertBoard(board);
    }

    // 게시물 삭제
    public boolean deleteBoard(int boardId) {
        return service.deleteBoard(boardId);
    }

    // 게시물 수정
    public boolean updateBoard(UnifiedDTO updatedBoard) {
        return service.updateBoard(updatedBoard);
    }

    // 게시물 상세 조회
    public UnifiedDTO getBoardById(int no) {
        return service.getBoardById(no);
    }

    // 관리자 여부 확인
    public boolean checkAdminStatus(String memberId) {
        return service.checkAdminStatus(memberId);
    }

    // 조회수 증가
    public void incrementViewCount(int no) {
        service.incrementViewCount(no);
    }

    // 모든 회원 조회 (관리자 전용)
    public List<UnifiedDTO> showMemberAll() {
        return service.showMemberAll();
    }
}