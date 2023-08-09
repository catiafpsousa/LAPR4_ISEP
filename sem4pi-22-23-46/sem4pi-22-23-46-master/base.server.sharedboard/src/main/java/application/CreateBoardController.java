package application;

import eapli.base.boardmanagement.domain.BoardBuilder;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;
import javax.transaction.Transactional;
import java.util.List;

@UseCaseController
public class CreateBoardController {
    private final BoardRepository boardRepo= PersistenceContext.repositories().boards();
    private SharedBoard board;

    @Transactional
    public SharedBoard createSharedBoard (final String title, final int numberOfrows, final int numberOfColumns, final List<String> rowTitles, final List <String> columnTitles, final SystemUser user){
        final SystemUser owner = user;
        final var boardBuilder = new BoardBuilder();
        boardBuilder.withOwner(owner).withTitle(title).withRowNumber(numberOfrows).withColumnNumber(numberOfColumns);
        board= boardBuilder.build();
        updateColumnTitles(numberOfColumns, columnTitles);
        updateRowTitles(numberOfrows, rowTitles);
        return boardRepo.save(board);
    }


    private void updateColumnTitles(int columns, List<String> titles){
        if(titles.size()!=0) {
            for (int i = 0; i < columns; i++) {
                String title = titles.get(i);
                board.updateColumnTitle(title, i + 1);
            }
        }
    }

    private void updateRowTitles(int rows, List<String> titles){
        if(titles.size()!=0) {
            for (int i = 0; i < rows; i++) {
                String title = titles.get(i);
                board.updateRowTitle(title, i + 1);
            }
        }
    }
}

