
import java.util.List;
//client(User) 와 상호작용
public class BoardController {
    private BoardService boardService = new BoardService();

    public void createBoard(Dto board) {
        boardService.createBoard(board);
    }

    public List<Dto> getBoardList() {
        return boardService.getBoardList();
    }

    public void updateBoard(Dto board) {
        boardService.updateBoard(board);
    }

    public void deleteBoard(int idx) {
        boardService.deleteBoard(idx);
    }
}
