package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.time.LocalDate; // import the LocalDate class

import java.util.Arrays;
import java.util.Scanner;


public class DataHandler {
    public static void main(String[] args) {
    }
    public static void addUser(User user) throws IOException {
        String path = (System.getProperty("user.dir")+"\\src\\main\\java\\commands\\Data\\UserData");
        String dataToAdd = user.getId()+",0,0,0,"+LocalDate.now();
        replaceFileContent(path,"-", "-\n"+dataToAdd);
        System.out.println("Initialized Data For "+user.getName());
    }

    public static void addUserFromId(String user) throws IOException {
        String path = (System.getProperty("user.dir")+"\\src\\main\\java\\commands\\Data\\UserData");
        String dataToAdd = user+",0,0,0,"+LocalDate.now();
        replaceFileContent(path,"-", "-\n"+dataToAdd);
        System.out.println("Initialized Corrupt Data "+user);
    }

    public static void addVouchID(Event event, User vouched, User vouchee, String reason) throws IOException {
        String path = (System.getProperty("user.dir")+"\\src\\main\\java\\commands\\Data\\Vouches");
        String ID = String.valueOf(getLineCount(Path.of(path)));
        String dataToAdd = ID+"-"+vouched.getId()+"-"+vouchee.getId()+"-"+reason+"-"+LocalDate.now();
        replaceFileContent(path,"-", "-\n"+dataToAdd);

        TextChannel vouchlogs = event.getJDA().getTextChannelById("1152427520419971264");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("VOUCH ID#"+ID, "");
        embed.addField("Vouched: "+vouched.getName()+" ("+vouched.getId()+")", "", false);
        embed.addField("Vouchee: "+vouchee.getName()+" ("+vouchee.getId()+")", "", false);
        embed.addField("Reason: "+reason, "", false);
        embed.setFooter("Date | "+LocalDate.now());

        embed.setColor(Color.DARK_GRAY);
        MessageEmbed eb = embed.build();
        vouchlogs.sendMessageEmbeds(eb).queue();
        embed.clear();
    }

    public static void addScamVouchID(Event event, User vouched, User vouchee, String reason) throws IOException {
        String path = (System.getProperty("user.dir")+"\\src\\main\\java\\commands\\Data\\ScamVouches");
        String ID = String.valueOf(getLineCount(Path.of(path)));
        String dataToAdd = ID+"-"+vouched.getId()+"-"+vouchee.getId()+"-"+reason+"-"+LocalDate.now();
        replaceFileContent(path,"-", "-\n"+dataToAdd);

        TextChannel vouchlogs = event.getJDA().getTextChannelById("1152427520419971264");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("VOUCH ID#"+ID, "");
        embed.addField("Scam Vouched: "+vouched.getName()+" ("+vouched.getId()+")", "", false);
        embed.addField("Vouchee: "+vouchee.getName()+" ("+vouchee.getId()+")", "", false);
        embed.addField("Reason: "+reason, "", false);
        embed.setFooter("Date | "+LocalDate.now());

        embed.setColor(Color.RED);
        MessageEmbed eb = embed.build();
        vouchlogs.sendMessageEmbeds(eb).queue();
        embed.clear();
    }

    public static void replaceFileContent(String filePath, String find, String replaceWith) throws IOException {
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

    public static String[] getSpecifiedData(String userid) throws IOException {
        String userline = getFromFile(userid);
        if(userline == null){addUserFromId(userid);}
        userline = getFromFile(userid);
        String[] userinfo = userline.split(",");
        return userinfo;
    }
    public static void updateUserData(String user, Integer received, Integer given, Integer scam) throws IOException {
        //0 - UserID, 1 - Vouches Received, Vouches Given, Rating, Date Joined
        Path path = Paths.get("C:\\Users\\issaa\\IdeaProjects\\Overseer\\src\\main\\java\\commands\\Data\\UserData");
        Charset charset = StandardCharsets.UTF_8;
        String userline = getFromFile(user);
        if(userline == null){addUserFromId(user);}
        userline = getFromFile(user);
        String[] userinfo = userline.split(",");
        Integer userinfo1int = Integer.parseInt(userinfo[1])+received;
        Integer userinfo2int = Integer.parseInt(userinfo[2])+given;
        Integer userinfo3int = Integer.parseInt(userinfo[3])+scam;
        userinfo[1] = userinfo1int.toString();
        userinfo[2] = userinfo2int.toString();
        userinfo[3] = userinfo3int.toString();

        String concatenatedString = String.join(",", userinfo);

        String content = new String(Files.readAllBytes(path), charset);
        content = content.replaceAll(userline, concatenatedString);
        Files.write(path, content.getBytes(charset));
    }
    public static String getFromFile(String string){
        String path = (System.getProperty("user.dir")+"\\src\\main\\java\\commands\\Data\\UserData");
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();
                if(currentLine.contains(string)){
                    return currentLine;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
    public static Boolean findFromFile(String string){
        String path = (System.getProperty("user.dir")+"\\src\\main\\java\\commands\\Data\\UserData");
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                //myReader.nextLine();
                if(myReader.nextLine().contains(string)){
                    return true;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getLineCount(Path path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();

        return lines;
    }
}