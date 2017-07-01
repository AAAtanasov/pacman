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



//    public Maze currentMaze = game.getCurrentMaze();

    public MOVE getMove(Game game, long timeDue) {
        if (startIndex == 0) {
            FirstIteration(game);
            this.extendedGame = new ExtendedGame();
            this.extendedGame.initGame(game);
            startIndex += 1;
        }

        this.extendedGame.updateGame(game);
        GameInfo state = game.getPopulatedGameInfo();
        int pacmanIndex = state.getPacman().currentNodeIndex;
        MOVE lastMove = state.getPacman().lastMoveMade;

        MOVE test = game.getApproximateNextMoveTowardsTarget(pacmanIndex, 0, lastMove, distanceMeasure );

        //modify pill lists
        //check if in junction
        //check for pill
//        int pillIndex = game.getPillIndex(pacmanIndex);
//        Boolean test = game.wasPillEaten();
//        if(test){
//            int lastMov
//            if(isPillAvailable){
//                //get pill index
//                if(pillsInMaze.contains(pillIndex)){
//                    pillsInMaze.remove(pillIndex);
//
//                }
//            }
//        }
        myMove = MOVE.NEUTRAL;
        //check if ghost is present
        if(state.getGhosts().size() > 0){
            ArrayList<MOVE> movesAway = new ArrayList();
            EnumMap<Constants.GHOST, Ghost> ghosts = state.getGhosts();
            for(Ghost ghost : ghosts.values()){
                MOVE away = game.getNextMoveAwayFromTarget(pacmanIndex,ghost.currentNodeIndex, lastMove, distanceMeasure);
                movesAway.add(away);
            }
            // choose best move away
            if (movesAway.size() > 1){
                MOVE originalMove = movesAway.get(0);
                MOVE[] awayDirections = new MOVE[movesAway.size()];
                MOVE[] possibleMoves = game.getPossibleMoves(pacmanIndex);
                // remove moveaway from possible moves
                ArrayList<MOVE> run = new ArrayList();
                run.addAll(Arrays.asList(awayDirections));


                // set union -> remove movesaway from possible moves
                // select the one which brings the best reward

                for(int i = 0; i < movesAway.size(); i ++){
                    MOVE nextAway = movesAway.get(i);
                    if (nextAway != originalMove){

                    }
                }
            } else {
                myMove = movesAway.get(0);
            }


        } else {
            if (currentTargetNode == 0 || pacmanIndex == currentTargetNode){
                ExtractFeaturesFromState(state, game);
            }

            myMove = game.getNextMoveTowardsTarget(pacmanIndex, currentTargetNode, lastMove, distanceMeasure);
        }
        //check if target is achieved

        //check if pill will be eaten


        return myMove;
    }

//    private int[] FindPillChain(int[] distanses, int pacmanIndex) {
//        // find best from min to max chain
//
////        int[] bestChain =
//
//
//    }

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
            int asdasda = this.pillsInMaze.get(pillIndex);
            this.currentTargetNode = asdasda;
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


        //check for chain and compute score
//        if (maxOccurances == 1) {
//            int indexOfMax = Arrays.asList(copy).indexOf(max);
//            int targetIndex = pill_nodes[indexOfMax];
//            int[] shortestPath = game.getShortestPath(pacmanIndex, targetIndex);
//        } else {
////            int maxIndex = game.getPillIndex(pill_nodes[indexOfMaxa]);
//            MOVE asd = game.getNextMoveTowardsTarget(pacmanIndex, pill_nodes[indexOfMaxa], distanceMeasure);
//            int a = 1;
//            currentTargetNode = pill_nodes[indexOfMaxa];
//            //check state
//            //check for chain
//        }




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

//        Maze temp = game.getCurrentMaze();
//        Arrays.stream(game.getCurrentMaze().graph).forEach(node -> {
//
//            int y = game.getNodeYCood(node.nodeIndex) / 2;
//            int x = game.getNodeXCood(node.nodeIndex);
//            x_train.add(x);
//            y_train.add(y);
//            int score = 50;
//            // check for pills
//
//        });
//
//        Collections.sort(x_train);
//        Collections.sort(y_train);
//        Set uniqueValues = new HashSet(x_train);
//        Set uniqueValuesy = new HashSet(y_train);
    }
//
//    private void resetGame(Game game) {
//        Arrays.stream(game.getCurrentMaze().graph).forEach(node -> {
////            state[node.y / 2][node.x / 2] = 0.07;
//            s1.putScalar(0, 0, node.y / 2, node.x / 2, 0.1); // like s^prime
////            state.putScalar(new int[]{node.y / 2, node.x / 2}, 0x444444);
//        });
//        Arrays.stream(game.getPillIndices()).forEach(index -> {
//            int x = (int) Math.ceil((float) (game.getNodeXCood(index) - 1) / 2.0), y = (int) Math.ceil((float) (game.getNodeYCood(index) - 1) / 2.0);
//            s1.putScalar(0, 0, y, x, 0.5);
////            state[y][x] = 0.5;
////            state.putScalar(new int[]{y, x}, 0xffff00);
//        });
//
//        Arrays.stream(game.getPowerPillIndices()).forEach(index -> {
//            int x = (int) Math.ceil((float) (game.getNodeXCood(index) - 1) / 2.0), y = (int) Math.ceil((float) (game.getNodeYCood(index) - 1) / 2.0);
////            state.putScalar(new int[]{y, x}, 0xf54f98);
//            s1.putScalar(0, 0, y, x, 0.5);
////            state[y][x] = 0.5;
//        });
//
//
//        resetRound();
//    }

    public double getValue(Game game){
//        game.getCurrentMaze()
        return game.getScore() + game.getPacmanNumberOfLivesRemaining()*100;
    }
}