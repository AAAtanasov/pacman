package entrants.pacman.username;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.info.GameInfo;
import pacman.game.internal.Ghost;
import pacman.game.internal.Maze;

import java.util.*;
import entrants.pacman.username.ExtendedGame;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController {
    private int[][] stateArray = new int[200][200];
    private MOVE myMove = MOVE.NEUTRAL;
    private int startIndex = 0;
    private ArrayList<Integer> x_train = new ArrayList();
    private ArrayList<Integer> y_train = new ArrayList();
    private ArrayList<Integer> pillsInMaze = new ArrayList();
    private ArrayList<Integer> powerPillsInMaze = new ArrayList();
    private Constants.DM distanceMeasure = Constants.DM.MANHATTAN;
    private int currentTargetNode = 0;
    private ExtendedGame extendedGame = null;
    private static final Random RANDOM = new Random();
    private static final int SIZE = MOVE.values().length;
    private static final List<MOVE> VALUES =
            Collections.unmodifiableList(Arrays.asList(MOVE.values()));
    private int gameLevel = -1;


//    public Maze currentMaze = game.getCurrentMaze();

    public MOVE getMove(Game game, long timeDue) {
        int currentLevel = game.getCurrentLevel();
        if (currentLevel != gameLevel){
            FirstIteration(game);
            this.extendedGame = new ExtendedGame();
            this.extendedGame.initGame(game, this.pillsInMaze);
            this.gameLevel = currentLevel;
        }


        this.extendedGame.updateGame(game);
        GameInfo state = game.getPopulatedGameInfo();
        int pacmanIndex = state.getPacman().currentNodeIndex;
        MOVE lastMove = state.getPacman().lastMoveMade;

        MOVE test = game.getApproximateNextMoveTowardsTarget(pacmanIndex, 0, lastMove, distanceMeasure );

        myMove = MOVE.NEUTRAL;

        //check if ghost is present
        if(state.getGhosts().size() > 0){
            ArrayList<MOVE> movesAway = new ArrayList();
            EnumMap<Constants.GHOST, Ghost> ghosts = state.getGhosts();
            ArrayList<Ghost> ghostArr = new ArrayList(ghosts.values());

            for(Ghost ghost : ghostArr){
                double distanceToGhost = game.getDistance(pacmanIndex, ghost.currentNodeIndex, distanceMeasure);
                if (ghost.edibleTime == 0 && distanceToGhost < 35){
                    MOVE away = game.getNextMoveAwayFromTarget(pacmanIndex,ghost.currentNodeIndex, distanceMeasure);
                    movesAway.add(away);
                }
            }

            if (movesAway.size() > 1){
                ArrayList<MOVE> runTowards = new ArrayList();
                runTowards.addAll(Arrays.asList(game.getPossibleMoves(pacmanIndex)));
                runTowards.removeAll(movesAway);

                if(runTowards.size() == 0){
                    myMove = VALUES.get(RANDOM.nextInt(SIZE));
                } else {
                    // check better move
                    if (runTowards.size() > 1){
                        System.out.println("Dumb move");
                        System.out.println(runTowards.get(0));

                    }
                    ExtractFeaturesFromState(state, game);
                    MOVE suggestedMove = game.getNextMoveTowardsTarget(pacmanIndex, currentTargetNode,  distanceMeasure);
                    // check for a better move
                    if (runTowards.contains(suggestedMove)){
                        myMove = suggestedMove;
                        System.out.println("Suggested move used: " + myMove.toString());

                    } else {
                        myMove = runTowards.get(0);

                    }

                }
                // select the one which brings the best reward

            } else {
                if (movesAway.size() == 0) {
//                    System.out.println("Error");
//                    myMove = VALUES.get(RANDOM.nextInt(SIZE));
                    if (currentTargetNode == 0 || pacmanIndex == currentTargetNode){
                        ExtractFeaturesFromState(state, game);
                    }

                    myMove = game.getNextMoveTowardsTarget(pacmanIndex, currentTargetNode,  distanceMeasure);

                } else {
                    myMove = movesAway.get(0);
                }
            }


        } else {
//            if (currentTargetNode == 0 || pacmanIndex == currentTargetNode){
//                ExtractFeaturesFromState(state, game);
//            } else if (game.isJunction(pacmanIndex)){
//                ExtractFeaturesFromState(state, game);
//            }
            ExtractFeaturesFromState(state, game);

            myMove = game.getNextMoveTowardsTarget(pacmanIndex, currentTargetNode, lastMove, distanceMeasure);
        }
        //check if target is achieved

        //check if pill will be eaten


        return myMove;
    }

    private int FindBestTarget(Game game, int pillIndex){
//        Boolean isPillAvailable = this.extendedGame.isPillStillAvailable(pillIndex);
//        if (isPillAvailable) {
//            return pillIndex;
//        } else {
//        }
        return this.extendedGame.goToPill();

    }

    public void ExtractFeaturesFromState(GameInfo info, Game game) {
        int pacmanIndex = info.getPacman().currentNodeIndex;
        int[] pill_nodes = game.getActivePillsIndices();
        if (pill_nodes.length == 0){
            int pillIndex = this.extendedGame.goToPill();
//            int asdasda = this.pillsInMaze.get(pillIndex);
            this.currentTargetNode = pillIndex;
            int test = 1;

        } else {
            int maxOccurances = 0;

            int[] distanses = new int[pill_nodes.length];
            for (int i = 0; i < pill_nodes.length; i ++){
                distanses[i] = game.getManhattanDistance(pacmanIndex, pill_nodes[i]);
            }

            int[] copy = distanses.clone();
            Arrays.sort(distanses);
            int min = Arrays.stream(distanses).min().getAsInt();
            int max = Arrays.stream(distanses).max().getAsInt();
            int indexOfMaxa = Arrays.binarySearch(distanses, max);
            if (indexOfMaxa < distanses.length - 1){
                maxOccurances = distanses.length - indexOfMaxa; // error prone
            }

            for (Integer item : copy) {
                if (item == max){
                    maxOccurances += 1;
                }
            }
            this.currentTargetNode = pill_nodes[indexOfMaxa];
        }






        // compare distances
        // look for sequences
        int test = 1;
        // compute each distance to pills
        // assign to cost function


        //check for ghosts -> if present calculate distance
        // assign to cost function
        //check for powerpills -> if present calclulate distance
        // assign to cost function

        // get junctions
        // get current state
        // see where there are many pills


    }

    public void FirstIteration(Game game) {
        int[] allPills = game.getPillIndices();
        int[] allPowerPills = game.getPowerPillIndices();

        for (Integer pill : allPills){
            pillsInMaze.add(pill);
        }
        for (Integer powerPill : allPowerPills){
            powerPillsInMaze.add(powerPill);
        }
    }

}