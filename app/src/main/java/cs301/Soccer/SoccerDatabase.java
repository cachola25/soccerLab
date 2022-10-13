package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String,SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        if(database.containsKey(firstName+lastName)){return false;}
        database.put(firstName+lastName,new SoccerPlayer(firstName,lastName,uniformNumber,teamName));
        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        if(!database.containsKey(firstName+lastName)){return false;}
        database.remove(firstName+lastName);
        return true;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        return database.get(firstName+lastName);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        if(!database.containsKey(firstName+lastName)){return false;}
        database.get(firstName+lastName).bumpGoals();
        return true;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        if(!database.containsKey(firstName+lastName)){return false;}
        database.get(firstName+lastName).bumpYellowCards();
        return true;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        if(!database.containsKey(firstName+lastName)){return false;}
        database.get(firstName+lastName).bumpRedCards();
        return true;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if(teamName == null) {
            return database.size();
        }
        int count = 0;
        for(SoccerPlayer player : database.values()){
            if(player.getTeamName().equals(teamName)){count++;}
        }
        return count;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {

        ArrayList<SoccerPlayer> players = new ArrayList<>();
        for(SoccerPlayer player : database.values()) {
            players.add(player);
        }
        if(idx >= players.size()){
            return null;
        }
        ArrayList<SoccerPlayer> playersOnTeam = new ArrayList<>();
        for(SoccerPlayer player : players){
            if(player.getTeamName().equals(teamName)){
                playersOnTeam.add(player);
            }
        }
        if(teamName != null) {
            if(idx < playersOnTeam.size()) {
                return playersOnTeam.get(idx);
            }else{
                return null;
            }
        }

        return players.get(idx);
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        if(!file.exists() || !file.canRead()){
            return false;
        }
        Scanner scnr = null;
        try{
            scnr = new Scanner(file);
        }catch(Exception e){
            Log.d("FILE ERROR",e.getMessage());
        }

        int count = 0;
        String firstName ="";
        String lastName = "";
        int uniform = 0;
        String team = "";
        int goals = 0;
        int yellowCards = 0;
        int redCards = 0;
        while(scnr.hasNextLine()){
            firstName = scnr.nextLine();
            lastName = scnr.nextLine();
            if(database.containsKey(firstName+lastName)){
                database.remove(firstName+lastName);
            }
            uniform = scnr.nextInt();
            team = scnr.next();
            SoccerPlayer temp = new SoccerPlayer(firstName,lastName,uniform,team);
            goals = scnr.nextInt();
            while(temp.getGoals() != goals){
                temp.bumpGoals();
            }
            yellowCards = scnr.nextInt();
            while(temp.getYellowCards() != yellowCards){
                temp.bumpYellowCards();
            }
            redCards = scnr.nextInt();
            while(temp.getRedCards() != redCards){
                temp.bumpRedCards();
            }
            scnr.nextLine();
            database.put(firstName+lastName, temp);
        }
        scnr.close();
        return true;
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        PrintWriter pw = null;
        FileOutputStream fileOS = null;
        try{
            fileOS = new FileOutputStream(file,true);
            pw = new PrintWriter(fileOS);
        }catch(Exception e){
            Log.d("FILE ERROR",e.getMessage());
            return false;
        }
        for(SoccerPlayer player : database.values()){
            pw.println(logString(player.getFirstName()));
            pw.println(logString(player.getLastName()));
            pw.println(logString(""+player.getUniform()));
            pw.println(logString(player.getTeamName()));
            pw.println(logString(""+player.getGoals()));
            pw.println(logString(""+player.getYellowCards()));
            pw.println(logString(""+player.getRedCards()));
        }
        pw.close();
        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
//        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet<String> teams = new HashSet<>();
        for(SoccerPlayer player : database.values()){
            teams.add(player.getTeamName());
        }
        return teams;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
