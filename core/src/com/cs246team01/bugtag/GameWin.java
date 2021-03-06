package com.cs246team01.bugtag;

import com.badlogic.gdx.math.Rectangle;

/**
 * Responsible for tracking and checking if the game should end, based on whether the bugs are touching.
 */
class GameWin {
    private int win_status;
    private String whoIsWinner;

    /**
     * Default constructor which sets initial value of the win_status and nothing else.
     */
    GameWin() {
        win_status = 0;
    }


    /**
     * Checks to see if the bugs are touching and whether the game should end as a result.
     *
     * @param bugGame Needed parameter to obtain the hit boxes of each bug and their current positions.
     * @return Returns '3' to represent GAME_OVER if bugs are touching, or '1' to represent
     * GAME_STARTED (meaning the current game should still continue) if they're not touching.
     */
    int checkWin(GridObjectHandler bugGame, int timeRemaining) {
        Rectangle bug1_hitbox = bugGame.getBugOne().getHitBox();
        Rectangle bug2_hitbox = bugGame.getBugTwo().getHitBox();


        if (bug1_hitbox.overlaps(bug2_hitbox)) {
            if (bugGame.getBugOne().isChaser())
                whoIsWinner = "Yellow bug won!";
            else if (bugGame.getBugTwo().isChaser())
                whoIsWinner = "Red bug won!";

            return win_status = 3;
        }
        else if (timeRemaining <= 0){
            if (bugGame.getBugOne().isChaser())
                whoIsWinner = "Red bug won!";
            else if (bugGame.getBugTwo().isChaser())
                whoIsWinner = "Yellow bug won!";

            return win_status = 3;
        }
        else
            return win_status = 1;
    }

    String whoIsWinner(){return whoIsWinner;}

    /**
     * Resets the status that reports whether the game has been won.
     */
    private void resetWinStatus() {
        win_status = 0;
    }

    /**
     * Determines whether the game should be reset based on the win_status.
     *
     * @return Returns true if game reset is needed, and false if not.
     */
    boolean isResetNeeded() {
        if (win_status == 3) {
            resetWinStatus();
            return true;
        } else {
            resetWinStatus();
            return false;
        }
    }
}


