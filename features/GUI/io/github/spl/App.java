import io.github.spl.game.GameView;

import javax.swing.SwingUtilities;

import io.github.spl.game.*;

public class App {
    public static void main(String[] args) {
    	GUIGameView view = new GUIGameView(args);
        view.run(); 
    }
}
