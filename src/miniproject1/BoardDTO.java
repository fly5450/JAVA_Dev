package miniproject1;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

//DTO는 프로세스 간에 데이터를 전달하는 용도의 객체. 비즈니스 로직을 포함하지 않는 데이터를 전달하기 위한 단순한 객체 역할만 한다. (단일책임)
public class BoardDTO implements Serializable {
    private int idx;
    private String title;
    private String content;
    private String writer;
    private int viewCnt;
    private String deleteYn;
    private Date insertDate;
    private Date updateDate;
    private Date deleteDate;
    private SimpleDateFormat createdate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat logindate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//테이블의 모든 DB를 넣어줘야함.

 //Getter
public int getIdx() {return idx;}
public String getTitle() {return title;}
public String getContent() {return content;}
public String getWriter() {return writer;}
public int getViewCnt() { return viewCnt;}
public String getDeleteYn() {return deleteYn;}
public Date getInsertDate() {return insertDate;}
public Date getUpdateDate() {return updateDate;}
public Date getDeleteDate() {return deleteDate;}

 //Setter
public void setIdx(int idx) {this.idx = idx;}
public void setTitle(String title) {this.title = title;}
public void setContent(String content) {this.content = content;}
public void setWriter(String writer) {this.writer = writer;}
public void setViewCnt(int viewCnt) {this.viewCnt = viewCnt;}
public void setDeleteYn(String deleteYn) {this.deleteYn = deleteYn;}
public void setInsertDate(Date insertDate) {this.insertDate = insertDate;}
public void setUpdateDate(java.util.Date date) {this.updateDate = date;}
public void setDeleteDate(Date deleteDate) { this.deleteDate = deleteDate;}
public static void createdate(SimpleDateFormat createdate) {this.createdate = createdate;}
public void setLogindate(SimpleDateFormat loginDate) {this.logindate = logindate;}

    @Override
    public String toString() {
        return "{" +
            " idx='" + getIdx() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", writer='" + getWriter() + "'" +
            ", viewCnt='" + getViewCnt() + "'" +
            ", deleteYn='" + getDeleteYn() + "'" +
            ", insertDate='" + getInsertDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", deleteDate='" + getDeleteDate() + "'" +
            ", createdate='" + getCreatedate() + "'" +
            ", logindate='" + getLogindate() + "'" +
            "}";
    }

public void print() {
	System.out.printf("%4d|%-27s|%s\n", title, content, writer,  createdate.format(createdate));
}


}
