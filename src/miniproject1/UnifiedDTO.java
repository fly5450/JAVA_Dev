package miniproject1;
import java.sql.Date;
import java.sql.Timestamp;

public class UnifiedDTO {

    // 기본 생성자
    public UnifiedDTO() {
    }
    // Board 테이블 관련 필드
    private int idx;    //1
    private String title;  //2
    private String content; //3
    private String writer; //4
    private String boardPassword; //5
    private int viewCnt; //6 
    private Date insertDate; //7
    private Date updateDate; //8
    private Date deleteDate; //9
    // MemberInfo 테이블 관련 필드
    private String id;      //1
    private String password; //2
    private String memberName; //3
    private String tel;         //4
    private String address;     //5
    private String sex;         //6
    private Date last_login_date;      //7
    private Date last_logout_date;     //8
    private String deleteYn;           //9

    private String isAdminYn;            //10
    // MEMLOG 테이블 관련 필드
    private String logid;                //1
    private Timestamp login_Date;        //2
    private Timestamp logout_Date;        //3


    public int getIdx() {
        return this.idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return this.writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getBoardPassword() {
        return this.boardPassword;
    }

    public void setBoardPassword(String boardPassword) {
        this.boardPassword = boardPassword;
    }

    public int getViewCnt() {
        return this.viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public Date getInsertDate() {
        return this.insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getDeleteDate() {
        return this.deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getLast_login_date() {
        return this.last_login_date;
    }

    public void setLast_login_date(Date last_login_date) {
        this.last_login_date = last_login_date;
    }

    public Date getLast_logout_date() {
        return this.last_logout_date;
    }

    public void setLast_logout_date(Date last_logout_date) {
        this.last_logout_date = last_logout_date;
    }

    public String getDeleteYn() {
        return this.deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getIsAdminYn() {
        return this.isAdminYn;
    }

    public void setIsAdminYn(String isAdminYn) {
        this.isAdminYn = isAdminYn;
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


    public String getLogid() {
        return this.logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }
}