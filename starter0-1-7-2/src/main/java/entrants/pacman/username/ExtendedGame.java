package entrants.pacman.username;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

import pacman.game.Constants;
import pacman.game.Game;


public class ExtendedGame {
    private boolean[] pillIsStillAvailable = null;
    private boolean[] powerPillIsStillAvailable = null;
    private int mazeIndex;
    public Game game;


    public ExtendedGame(){
    }

    public void initGame(Game game){
        this.game = game;
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
        listOfLengths.sort(Comparator.comparing(ArrayList::size));
        if (listOfLengths.size() > 5){

        } else {

        }
        ArrayList<Integer> bestList = listOfLengths.get(listOfLengths.size() - 1);

        // FIx this for end game
        return bestList.get(0);
    }

    private void EvaluateChains(){

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