package entrants.pacman.username;

import java.util.*;

import pacman.game.Constants;
import pacman.game.Game;
import entrants.pacman.username.ScoreClass;


public class ExtendedGame {
    private boolean[] pillIsStillAvailable = null;
    private boolean[] powerPillIsStillAvailable = null;
    private int mazeIndex;
    public Game game;
    private Constants.DM distanceMeasure = Constants.DM.MANHATTAN;
    private ArrayList<Integer> pillsInMaze = null;



    public ExtendedGame(){
    }

    public void initGame(Game game, ArrayList<Integer> pillsInMaze){
        this.game = game;
        this.pillsInMaze = pillsInMaze;
        resetPills();
        resetPowerPills();
    }

    public void updateGame(Game game){
        this.game = game;

        if (mazeIndex != game.getMazeIndex()){
            this.resetData(game);
        }

        //set pills
        int[] powerPills = this.game.getPowerPillIndices();
        int[] pills = this.game.getPillIndices();

        for (int i = 0; i < powerPills.length; i++) {
            Boolean thisPill = this.game.isPowerPillStillAvailable(i);

            if (thisPill != null && thisPill == false){
                this.powerPillIsStillAvailable[i] = false;
            }
        }

        for (int i = 0; i < pills.length; i++)
        {
            Boolean thisPill = this.game.isPillStillAvailable(i);
            if (thisPill != null && thisPill == false){
                this.pillIsStillAvailable[i] = false;
            }
        }
    }

    public int goToPill(){
        ArrayList<Integer> bestChain = new ArrayList();
        ArrayList<ArrayList<Integer>> listOfLengths = new ArrayList();
        int bestCount = 0;

        for(int i = 0; i < this.pillIsStillAvailable.length; i++){
            // iterate over all pills
            // each true chain is stored
            // on false interupt store chain
            if (this.pillIsStillAvailable[i] == true){
                bestChain.add(i);
                bestCount ++;
            } else{
                if (bestChain.size() != 0){
                    ArrayList<Integer> arrayToStore = new ArrayList(bestChain);
                    listOfLengths.add(arrayToStore);
                    bestChain = new ArrayList<>();
                }
            }
        }

        // iterate chains and look for longest
        ArrayList<ScoreClass> evaluation = EvaluateChains(listOfLengths);
        int bestTarget = GetBestCurrentTarget(evaluation);
//        listOfLengths.sort(Comparator.comparing(ArrayList::size));
//        if (listOfLengths.size() > 5){
//
//        } else {
//
//        }
//        ArrayList<Integer> bestList = listOfLengths.get(listOfLengths.size() - 1);

        // FIx this for end game
        return bestTarget;
    }

    private ArrayList<ScoreClass> EvaluateChains(ArrayList<ArrayList<Integer>> listOfLengths){
        int bestCount = 0;
        ArrayList<ScoreClass> scores = new ArrayList();
        int stepSize = 25;

        for(int i = 0; i < listOfLengths.size(); i++){
            ArrayList<Integer> currentList = listOfLengths.get(i);
            // expect chains with more than 10 pills together
            // select closest chain and score

            if (currentList.size() > stepSize){
                //check distance to first and last pill - use median for scale
                int remaining = currentList.size() % stepSize;
                int iterationCount = (currentList.size() - remaining) / stepSize;

                for(int j = 0; j < iterationCount; j++){
                    int startIndex = j * stepSize;
                    ArrayList<Integer> listToConsider = new ArrayList(currentList.subList(j, j+ stepSize));
                    ScoreClass scoreToAdd = ExtractScoreFromChain(listToConsider);
                    scores.add(scoreToAdd);
                }

                if(remaining > 0){
                    int startIndex = iterationCount * stepSize - 1;
                    ArrayList<Integer> listToConsider = new ArrayList(currentList.subList(startIndex, startIndex + remaining));
                    ScoreClass scoreToAdd = ExtractScoreFromChain(listToConsider);
                    scores.add(scoreToAdd);
                }

            } else{
                ScoreClass newScore = ExtractScoreFromChain(currentList);
                scores.add(newScore);
            }
        }

        return scores;
    }

    private int GetBestCurrentTarget(ArrayList<ScoreClass> scores){
        ScoreClass bestScore = null;
        double leastDistance = 99999;
        int bestIndex = 0;
        ArrayList lengthSorted = new ArrayList(scores);
//        lengthSorted.sort(Comparator.comparing(ScoreClass::getSize).thenComparing(ScoreClass::getDistance));
//        scores.sort(Comparator.comparing(ScoreClass::getDistance).thenComparing(ScoreClass::getSize));
        scores.sort(Comparator.comparing(ScoreClass::getDensity));
//        scores.sort(Comparator.comparing(ScoreClass::getSize).thenComparing(ScoreClass::getDistance)); //.thenComparing(ScoreClass::getSize));

        // balance between length and distance


        return scores.get(0).getClosestNode();
    }

    private ScoreClass ExtractScoreFromChain(ArrayList<Integer> currentList){

        int firstElementAsNode = this.pillsInMaze.get(currentList.get(0));
        int lastElementAsNode = this.pillsInMaze.get(currentList.get(currentList.size() - 1));
        double distanceToFirst = this.game.getDistance(this.game.getPacmanCurrentNodeIndex(),
                firstElementAsNode, distanceMeasure);
        double distanceToLast = this.game.getDistance(this.game.getPacmanCurrentNodeIndex(),
                lastElementAsNode, distanceMeasure);

        double distanceMeasure = (distanceToFirst + distanceToLast) / 2;
//        double summedDistances =
        ScoreClass toAdd = new ScoreClass(distanceMeasure, currentList.size(), distanceToFirst, distanceToLast,
                firstElementAsNode, lastElementAsNode);
        return toAdd;
    }

    private void resetData(Game game){
        this.resetPowerPills();
        this.resetPills();
        this.mazeIndex = game.getMazeIndex();
    }

    private void resetPowerPills()
    {
        if (powerPillIsStillAvailable == null){
            this.powerPillIsStillAvailable = new boolean[game.getPowerPillIndices().length];
            for (int i = 0; i < this.powerPillIsStillAvailable.length; i++){
                this.powerPillIsStillAvailable[i] = true;
            }
        }
    }

    private void resetPills()
    {
        this.pillIsStillAvailable = new boolean[game.getPillIndices().length];
        for (int i = 0; i < this.pillIsStillAvailable.length; i++){
            this.pillIsStillAvailable[i] = true;
        }
    }

    public boolean isPowerPillStillAvailable()
    {
        if (this.powerPillIsStillAvailable != null){
            for (int i = 0; i < this.powerPillIsStillAvailable.length; i++){
                if (this.powerPillIsStillAvailable[i] == true)
                    return true;
            }
        }
        return false;
    }

    public boolean isPowerPillStillAvailable(int powerPillIndex){
        return this.powerPillIsStillAvailable[powerPillIndex];
    }

    public boolean isPillStillAvailable(int pillIndex){
        return this.pillIsStillAvailable[pillIndex];
    }
}