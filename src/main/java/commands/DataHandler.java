package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class DataHandler {
    public static void main(String[] args) {
    }
    public static void addTeam(String league, String newTeamName) throws IOException {
        replaceFileContent("C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\Teams", "X", newTeamName);
    }

    public static void removeTeam(String league, String oldTeamName) throws IOException {
        replaceFileContent("C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\Teams", oldTeamName, "X");
    }

    public static void endOfGameAddTeamStats(String league, String team1, int team1Goals, String team2, int team2Goals) throws IOException {
        String[] teamData = getTeamData(league);
        String[] teamStats = getTeamStats(league);
        String[] teamSeasonStats = getTeamSeasonStats(league);
        String winner = "";
        //Determine Winner
        if (team1Goals > team2Goals){
            winner = team1;
        }else if(team2Goals > team1Goals){
            winner = team2;
        }else{
            winner = "draw";
        }


        int team1index = Arrays.asList(teamData).indexOf(team1);
        int team2index = Arrays.asList(teamData).indexOf(team2);
        System.out.println(team1index); System.out.println(team2index);

        String[] reconstruct;
        //Configuring teamStats
        //TeamStats - League Championships, Seasons Played, Wins, Draws, Losses
            reconstruct = teamStats[team1index].split(",");
            if(winner == team1){
                reconstruct[2] = ""+(Integer.parseInt(reconstruct[2])+1);
            }else if(winner == team2){
                reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+1);
            }else if (winner == "draw"){
                reconstruct[3] = ""+(Integer.parseInt(reconstruct[3])+1);
            }
            String reconstructedTeam1TeamStats = reconstruct[0]+","+reconstruct[1]+","+reconstruct[2]+","+reconstruct[3]+","+reconstruct[4];

            reconstruct = teamStats[team2index].split(",");
            if(winner == team1){
                reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+1);
            }else if(winner == team2){
                reconstruct[2] = ""+(Integer.parseInt(reconstruct[2])+1);
            }else if (winner == "draw"){
                reconstruct[3] = ""+(Integer.parseInt(reconstruct[3])+1);
            }
            String reconstructedTeam2TeamStats = reconstruct[0]+","+reconstruct[1]+","+reconstruct[2]+","+reconstruct[3]+","+reconstruct[4];

            //WRITING TO FILE
            String filePath = "C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\TeamStats";
            //Instantiating the Scanner class to read the file
            Scanner sc = new Scanner(new File(filePath));
            //instantiating the StringBuffer class
            StringBuffer buffer = new StringBuffer();
            //Reading lines of the file and appending them to StringBuffer
            int currentLine = 0;
            while (sc.hasNextLine()) {
                if(currentLine == team1index){
                    buffer.append(reconstructedTeam1TeamStats+System.lineSeparator());
                    sc.nextLine();
                }else if(currentLine == team2index){
                    buffer.append(reconstructedTeam2TeamStats+System.lineSeparator());
                    sc.nextLine();
                }else{
                    buffer.append(sc.nextLine()+System.lineSeparator());
                }
                currentLine++;
            }
            String fileContents = buffer.toString();
            //closing the Scanner object
            sc.close();
            //instantiating the FileWriter class
            FileWriter writer = new FileWriter(filePath, false);
            writer.write(fileContents);
            writer.flush();


        //Configuring teamSeasonStats
        //TeamSeasonStats - Wins, Draws, Losses, Points, Goals Scored
        reconstruct = teamSeasonStats[team1index].split(",");
        if(winner == team1){
            reconstruct[0] = ""+(Integer.parseInt(reconstruct[0])+1);
            reconstruct[3] = ""+(Integer.parseInt(reconstruct[3])+3);
            reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+team1Goals);
        }else if(winner == team2){
            reconstruct[2] = ""+(Integer.parseInt(reconstruct[2])+1);
            reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+team1Goals);
        }else if (winner == "draw"){
            reconstruct[1] = ""+(Integer.parseInt(reconstruct[1])+1);
            reconstruct[3] = ""+(Integer.parseInt(reconstruct[3])+1);
            reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+team1Goals);
        }
        String reconstructedTeam1TeamSeasonStats = reconstruct[0]+","+reconstruct[1]+","+reconstruct[2]+","+reconstruct[3]+","+reconstruct[4];

        reconstruct = teamSeasonStats[team2index].split(",");
        if(winner == team2){
            reconstruct[0] = ""+(Integer.parseInt(reconstruct[0])+1);
            reconstruct[3] = ""+(Integer.parseInt(reconstruct[3])+3);
            reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+team2Goals);
        }else if(winner == team1){
            reconstruct[2] = ""+(Integer.parseInt(reconstruct[2])+1);
            reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+team2Goals);
        }else if (winner == "draw"){
            reconstruct[1] = ""+(Integer.parseInt(reconstruct[1])+1);
            reconstruct[3] = ""+(Integer.parseInt(reconstruct[3])+1);
            reconstruct[4] = ""+(Integer.parseInt(reconstruct[4])+team2Goals);
        }
        String reconstructedTeam2TeamSeasonStats = reconstruct[0]+","+reconstruct[1]+","+reconstruct[2]+","+reconstruct[3]+","+reconstruct[4];

        //WRITING TO FILE
        String filePath2 = "C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\TeamSeasonStats";
        //Instantiating the Scanner class to read the file
        Scanner sc2 = new Scanner(new File(filePath2));
        //instantiating the StringBuffer class
        StringBuffer buffer2 = new StringBuffer();
        //Reading lines of the file and appending them to StringBuffer
        int currentLine2 = 0;
        while (sc2.hasNextLine()) {
            if(currentLine2 == team1index){
                buffer2.append(reconstructedTeam1TeamSeasonStats+System.lineSeparator());
                sc2.nextLine();
            }else if(currentLine2 == team2index){
                buffer2.append(reconstructedTeam2TeamSeasonStats+System.lineSeparator());
                sc2.nextLine();
            }else{
                buffer2.append(sc2.nextLine()+System.lineSeparator());
            }
            currentLine2++;
        }
        String fileContents2 = buffer2.toString();
        //closing the Scanner object
        sc2.close();
        //instantiating the FileWriter class
        FileWriter writer2 = new FileWriter(filePath2, false);
        writer2.write(fileContents2);
        writer2.flush();
    }


    public static String[] getTeamData(String league) {
        return getFileContent("C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\Teams", 6);
    }


    public static String[] getTeamStats(String league) {
        return getFileContent("C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\TeamStats", 6);
    }



    public static String[] getTeamSeasonStats(String league) {
        return getFileContent("C:\\Users\\issaa\\IdeaProjects\\LeagueOBot\\src\\main\\java\\Data\\"+league+"\\TeamSeasonStats", 6);
    }

    public static void replaceFileContent(String filePath, String find, String replaceWith) throws IOException{
        //Instantiating the Scanner class to read the file
        Scanner sc = new Scanner(new File(filePath));
        //instantiating the StringBuffer class
        StringBuffer buffer = new StringBuffer();
        //Reading lines of the file and appending them to StringBuffer
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }
        String fileContents = buffer.toString();
        //closing the Scanner object
        sc.close();
        String oldLine = find;
        String newLine = replaceWith;
        //Replacing the old line with new line
        fileContents = fileContents.replaceFirst(oldLine, newLine);
        //instantiating the FileWriter class
        FileWriter writer = new FileWriter(filePath);
        writer.append(fileContents);
        writer.flush();
    }
    public static String[] getFileContent(String filePath, int arrLength){
        String[] data = new String[arrLength];
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            int lineNumber = 0;
            while (myReader.hasNextLine()) {
                data[lineNumber] = myReader.nextLine();
                lineNumber++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
}