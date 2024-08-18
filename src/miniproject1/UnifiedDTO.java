import java.sql.Date;
import java.sql.Timestamp;

public class UnifiedDTO  {
    // Board 테이블 관련 필드
    private int idx;
    private String title;
    private String content;
    private String writer;
    private int viewCnt;
    private String deleteYn;
    private Date insertDate;
    private Date updateDate;
    private Date deleteDate;
    // MemberInfo 테이블 관련 필드
    private String id;
    private String password;
    private String memberName;
    private String tel;
    private String address;
    private String sex;
    // MEMLOG 테이블 관련 필드
    private Timestamp loginDate;
    private Timestamp logoutDate;
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
    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }
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
    // public String getMemberName() {
    //     return memberName;
    // }
    public String getTel() {
        return tel;
    }
    public String getAddress() {
        return address;
    }
    public String getSex() {
        return sex;
    }
    //  MemberInfo 테이블 Setters 
    public void setId(String id) {
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    // public void setMemberName(String memberName) {
    //     this.memberName = memberName;
    // }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
//-----------------------------------------------------------------------------//
     // MEMLOG 테이블 Getter , Setter
public Timestamp getLoginDate() {
    return loginDate;
}
public Timestamp getLogoutDate() {
    return logoutDate;
}
public void setLoginDate(Timestamp loginDate) {
    this.loginDate = loginDate;
}
public void setLogoutDate(Timestamp logoutDate) {
    this.logoutDate = logoutDate;
}
//-----------------------------------------------------------------------------//
}