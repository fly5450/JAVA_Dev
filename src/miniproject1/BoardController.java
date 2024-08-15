package miniproject1;


//Controller Layer
import java.util.List;
import java.util.Scanner;


public class BoardController {
private BoardService boardService = new BoardService();

public void createBoard(BoardDTO board) {
   boardService.createBoard(board);
}

public List<BoardDTO> getBoardList() {
   return boardService.getBoardList();
}

static Scanner scanner = new Scanner(System.in);

public static void boardlist() {
	System.out.println("목록");
	System.out.println("번호|       제목                     | 작성일시");
	for (BoardDTO board : boardlist) {
		board.print();
	}

}
// Update, Delete methods 추가 가능

public void boardUpdate() {
	
	
}

public void boarddDlete() {
	
	
}


}
