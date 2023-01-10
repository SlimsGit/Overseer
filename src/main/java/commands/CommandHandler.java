package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("teams")){

            String league;
            OptionMapping leagueoption = event.getOption("league");
            if(leagueoption == null){league = "MAIN";}else{league = leagueoption.getAsString();}

            String[] teamData = DataHandler.getTeamData(league);
            String[] teamStats = DataHandler.getTeamStats(league);

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("OFFICIAL LEAGUE [O] TEAMS ALL TIME STATS");
            for (int i = 0 ; i <= teamData.length-1; i++){
                String[] stats = teamStats[i].split(",");
                eb.addField(teamData[i],
                        "\nLeague Champions: "+stats[0]+
                        "\nSeasons Played: "+stats[1]+
                        "\n W/D/L: "+stats[2]+"/"+stats[3]+"/"+stats[4],
                        true
                );
            }

            event.getChannel().sendMessageEmbeds(eb.build()).queue();
            event.reply("").queue();


        }else if(event.getName().equals("current-season")){

            String league;
            OptionMapping leagueoption = event.getOption("league");
            if(leagueoption == null){league = "MAIN";}else{league = leagueoption.getAsString();}

            String[] teamData = DataHandler.getTeamData(league);
            String[] teamSeasonStats = DataHandler.getTeamSeasonStats(league);
            String[] responses = new String[6];

            for(int i =0;i<=responses.length-1;i++){
                String[] stats = teamSeasonStats[i].split(",");
                responses[i] = String.format("**`%-15s|%3s|%3s|%3s|%8s|%7s`**\n", teamData[i], stats[0], stats[1], stats[2], stats[3], stats[4]);
            }
            String reply = "";
            reply += "**SEASON 1 STANDINGS**\n";
            reply += String.format("**`%-15s|%3s|%3s|%3s|%8s|%7s`**\n", "Team Name", "W ", "D ", "L ", "Points ", "Goals ");
            reply += String.format("**`%-15s|%3s|%3s|%3s|%8s|%7s`**\n", "---------------", "---", "---", "---", "--------", "-------");
            for(int i =0;i<=responses.length-1;i++){
                reply += responses[i];
            }
            event.reply(reply).queue();


        }else if(event.getName().equals("create-game")){


            String team1 = event.getOption("team1").getAsString();
            String team2 = event.getOption("team2").getAsString();
            MessageChannel channel = event.getChannel();
            Category category = event.getGuild().getCategoryById("1061053114997821500");
            if (category == null) {
                channel.sendMessage("Ongoing Matches NOT FOUND!").queue();
                return;
            }

            category.createTextChannel(team1+" VS. "+team2)
                    .queue();


        }else if (event.getName().equals("end-game")){

            String league;
            OptionMapping leagueoption = event.getOption("league");
            if(leagueoption == null){league = "MAIN";}else{league = leagueoption.getAsString();}

            OptionMapping option = event.getOption("team1");
            String team1 = option.getAsString();
            option = event.getOption("team2");
            String team2 = option.getAsString();
            option = event.getOption("team1-goals-scored");
            int team1goals = option.getAsInt();
            option = event.getOption("team2-goals-scored");
            int team2goals = option.getAsInt();
            try {
                DataHandler.endOfGameAddTeamStats(league, team1, team1goals, team2, team2goals);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.reply("Successfully Updated League Data.").queue();


        }else if (event.getName().equals("add-team")){

            String league;
            OptionMapping leagueoption = event.getOption("league");
            if(leagueoption == null){league = "MAIN";}else{league = leagueoption.getAsString();}

            event.deferReply();
            OptionMapping option = event.getOption("team-name");
            String teamName = option.getAsString();
            try {
                DataHandler.addTeam(league, teamName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.reply(teamName+" has been added to LEAGUE [O].").queue();

        }else if (event.getName().equals("remove-team")){

            String league;
            OptionMapping leagueoption = event.getOption("league");
            if(leagueoption == null){league = "MAIN";}else{league = leagueoption.getAsString();}

            event.deferReply();
            OptionMapping option = event.getOption("team-name");
            String teamName = option.getAsString();
            try {
                DataHandler.removeTeam(league, teamName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.reply(teamName+" has been added to LEAGUE [O].").queue();


        }else if (event.getName().equals("as-salaam-alaikum")){

            event.reply("Assalaamalaikum "+event.getUser().getAsMention()+"!").queue();

        }

    }
}
