package miniproject1;

import java.util.List;

//Service Layer
public class BoardService {
 private BoardDAO boardDAO = new BoardDAO();

 public int createBoard(BoardDTO board) {
     return boardDAO.insertBoard(board);
 }

 public List<BoardDTO> getBoardList() {
     return boardDAO.getAllBoards();
 }

 // Update, Delete methods 추가 가능
}
