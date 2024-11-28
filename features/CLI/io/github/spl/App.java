import io.github.spl.game.GameView;
import io.github.spl.game.*;

public class App {
    public static void main(String[] args) {
        GameView view = new CLIGameView();
        view.run();
    }
}
