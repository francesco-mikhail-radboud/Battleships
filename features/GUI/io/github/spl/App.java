import io.github.spl.game.GameView;
import io.github.spl.game.*;

public class App {
    public static void main(String[] args) {
        GUIGameView view = new GUIGameView();
        view.initializeGrids();
        view.run(); 
    }
}
