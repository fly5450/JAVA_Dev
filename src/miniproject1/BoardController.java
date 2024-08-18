package miniproject1;
import java.util.List;
//client(User) 와 상호작용
public class BoardController {
    private BoardService boardService = new BoardService();

    public void createBoard(BoardDTO board) {
        boardService.createBoard(board);
    }

    public List<BoardDTO> getBoardList() {
        return boardService.getBoardList();
    }

    public void updateBoard(BoardDTO board) {
        boardService.updateBoard(board);
    }

    public void deleteBoard(int idx) {
        boardService.deleteBoard(idx);
    }
}
