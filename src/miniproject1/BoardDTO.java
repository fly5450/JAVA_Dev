package miniproject1;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class BoardDTO implements Serializable {
    private int id;
    private String title;
    private String content;
    private String writer;
    private SimpleDateFormat createdate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat logindate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

 //Getter
public int getId() {return  id;}
public String getTitle() {return title;}
public String getContent() {return content;}
public String getWriter() {return writer;}
public SimpleDateFormat getCreatedDate() {return  createdate;}
public SimpleDateFormat getLogindate() {return logindate;}
	
//Setter
public void setId(int id) {	id = id;}
public void setTitle(String title) {	title = title;}
public void setContent(String content) {content = content;}	
public void setWriter(String writer) {writer = writer;}
public static void setSdf(SimpleDateFormat createdate) {createdate = createdate;}
public void setLogindate(Date loginDate) {logindate = this.logindate;}
@Override
public String toString() {
	return "BoardDTO [id=" + id + ", title=" + title + ", content=" + content + ", writer=" + writer + ", loginDate="
			+ logindate + ", getId()=" + getId() + ", getTitle()=" + getTitle() + ", getContent()=" + getContent()
			+ ", getWriter()=" + getWriter() + "]";
}
//public void print() {
//	System.out.printf("%4d|%-27s|%s\n", title, content, writer,  createdate.format(createdate));
//}


}
