package entrants.pacman.username;

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
            if(i == 148){
                int test = 1;
            }
            try {
                Boolean thisPill = this.game.isPillStillAvailable(i);
                if (thisPill != null && thisPill == false){
                    this.pillIsStillAvailable[i] = false;
                }

            } catch (NullPointerException ex) {
                int a = 1;

            }


        }
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