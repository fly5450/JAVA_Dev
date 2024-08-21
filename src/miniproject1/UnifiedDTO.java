import java.sql.Date;
import java.sql.Timestamp;

public class UnifiedDTO {

    // 기본 생성자
    public UnifiedDTO() {
        // 기본 생성자에서는 필드를 초기화하지 않아도 됩니다.
        // 객체 생성 시 필드를 설정할 필요가 있는 경우 생성자나 setter 메서드를 사용하세요.
    }

    // Board 테이블 관련 필드
    private int idx;
    private String title;
    private String content;
    private String writer;
    private String boardPassword;
    private int viewCnt;
    private Date insertDate;

    public String getBoardPassword() {
        return this.boardPassword;
    }

    public void setBoardPassword(String boardPassword) {
        this.boardPassword = boardPassword;
    }

    public String getLogid() {
        return this.logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public Timestamp getLogin_Date() {
        return this.login_Date;
    }

    public void setLogin_Date(Timestamp login_Date) {
        this.login_Date = login_Date;
    }

    public Timestamp getLogout_Date() {
        return this.logout_Date;
    }

    public void setLogout_Date(Timestamp logout_Date) {
        this.logout_Date = logout_Date;
    }
    private Date updateDate;
    private Date deleteDate;

    // MemberInfo 테이블 관련 필드
    private String id;
    private String password;
    private String memberName;
    private String tel;
    private String address;
    private String sex;
    private Timestamp last_login_date;
    private Timestamp last_logout_date;
    private String deleteYn;
    private String isAdmin;
    
    public Timestamp getLast_login_date() {
        return this.last_login_date;
    }

    public void setLast_login_date(Timestamp last_login_date) {
        this.last_login_date = last_login_date;
    }

    public Timestamp getLast_logout_date() {
        return this.last_logout_date;
    }

    public void setLast_logout_date(Timestamp last_logout_date) {
        this.last_logout_date = last_logout_date;
    }
 ;

    // MEMLOG 테이블 관련 필드
    private String logid;
    private Timestamp login_Date;
    private Timestamp logout_Date;

    //-----------------------------------------------------------------------------//
    // Board 테이블 : Getters 
    public int getIdx() {
        return idx;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    // Board 테이블 : Setters 
    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    // public void setDeleteYn(String deleteYn) {
    //     this.deleteYn = deleteYn;
    // }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
    //-----------------------------------------------------------------------------//
    //  MemberInfo 테이블 Getters 
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    //  MemberInfo 테이블 Setters 
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    //-----------------------------------------------------------------------------//
    // MEMLOG 테이블 Getter , Setter
    public Timestamp getLoginDate() {
        return login_Date;
    }

    public Timestamp getLogoutDate() {
        return logout_Date;
    }

    public void setLoginDate(Timestamp login_Date) {
        this.login_Date = login_Date;
    }

    public void setLogoutDate(Timestamp logout_Date) {
        this.logout_Date = logout_Date;
    }
    //-----------------------------------------------------------------------------//
}