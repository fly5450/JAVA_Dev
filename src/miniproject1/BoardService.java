package miniproject1;
import java.util.List;

/*Service Layer 비즈니스 로직을 담당하는 클래스
Service 클래스는 애플리케이션의 비즈니스 로직을 처리하는 중요한 계층입니다. 
이 계층은 데이터 액세스 로직(DAO)과 애플리케이션의 다른 부분(예: 컨트롤러 또는 뷰) 사이에서 중간 역할을 합니다. 
비즈니스 로직이란 애플리케이션이 특정 요구 사항을 충족하기 위해 수행해야 하는 주요 작업을 의미합니다.

비즈니스 로직 구현:
DAO 클래스를 사용하여 데이터베이스와 상호작용하면서, 애플리케이션이 수행해야 하는 비즈니스 로직을 처리합니다.
단순히 데이터베이스에서 데이터를 가져오거나 저장하는 것 이상의 작업을 수행할 수 있습니다. 예를 들어, 데이터를 가져온 후 특정 필터링이나 변환 작업을 하거나, 
여러 개의 DAO 작업을 조합하여 하나의 복잡한 작업을 수행할 수 있습니다.

트랜잭션 관리:
비즈니스 로직의 처리 중 여러 DAO 작업이 필요할 때, 이들 작업이 하나의 트랜잭션으로 묶이도록 관리할 수 있습니다.
트랜잭션 관리는 주로 하나의 서비스 메서드에서 여러 개의 데이터베이스 작업이 수행될 때 필요합니다. 이 경우 모든 작업이 성공해야만 데이터베이스에 반영되며, 
하나라도 실패하면 전체 작업이 취소됩니다(롤백).

컨트롤러와 DAO 간의 연결:
컨트롤러는 사용자의 요청을 받아들이고, 이 요청을 처리하기 위해 서비스 클래스의 메서드를 호출합니다.
서비스 클래스는 DAO 클래스를 호출하여 데이터베이스와의 상호작용을 처리한 후, 결과를 컨트롤러에 반환합니다.
이 계층 분리는 코드를 더욱 모듈화하고, 유지 보수와 테스트를 용이하게 합니다.

재사용성:
비즈니스 로직이 서비스 클래스에 모여 있으면, 이 로직을 다른 컨트롤러나 애플리케이션의 다른 부분에서도 쉽게 재사용할 수 있습니다.
여러 컨트롤러에서 동일한 비즈니스 로직을 수행해야 하는 경우, 그 로직을 서비스 계층에서 관리함으로써 코드 중복을 피할 수 있습니다.

<Service를 이해하기 위한 큰 틀>
1. Client가 Request를 보낸다.(Ajax, Axios, fetch등..)
2. Request URL에 알맞은 Controller가 수신 받는다. (@Controller , @RestController)
3. Controller 는 넘어온 요청을 처리하기 위해 Service 를 호출한다.
4. Service는 알맞은 정보를 가공하여 Controller에게 데이터를 넘긴다.
5. Controller 는 Service 의 결과물을 Client 에게 전달해준다.

*/
public class BoardService {
    private BoardDAO boardDAO = new BoardDAO();

    public int createBoard(BoardDTO board) {
        return boardDAO.insertBoard(board);
    }

    public List<BoardDTO> getBoardList() {
        return boardDAO.getAllBoards();
    }

    public int updateBoard(BoardDTO board) {
        return boardDAO.updateBoard(board);
    }

    public int deleteBoard(int idx) {
        return boardDAO.deleteBoard(idx);
    }
}


