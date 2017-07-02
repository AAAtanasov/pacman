
import entrants.ghosts.username.Blinky;
import entrants.ghosts.username.Inky;
import entrants.ghosts.username.Pinky;
import entrants.ghosts.username.Sue;
import entrants.pacman.username.MyPacMan;
import pacman.Executor;
import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;
import pacman.game.Constants.*;

import java.util.EnumMap;
import entrants.ghosts.username.CommonGhost;
import examples.StarterGhost.POGhost;

/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {

    public static void main(String[] args) {

        Executor executor = new Executor(true, true);
//        MASController ghosts = new POCommGhost(GHOST.BLINKY);


        EnumMap<GHOST, IndividualGhostController> controllers = new EnumMap<>(GHOST.class);
        CommonGhost blinky = new CommonGhost(GHOST.BLINKY);
        CommonGhost inky = new CommonGhost(GHOST.INKY);
        CommonGhost pinky = new CommonGhost(GHOST.PINKY);
        CommonGhost sue = new CommonGhost(GHOST.SUE);

        controllers.put(GHOST.INKY, inky);
        controllers.put(GHOST.BLINKY, blinky);
        controllers.put(GHOST.PINKY, pinky);
        controllers.put(GHOST.SUE, sue);

        executor.runGameTimed(new MyPacMan(), new MASController(controllers), true);
//        executor.ru
    }
}
