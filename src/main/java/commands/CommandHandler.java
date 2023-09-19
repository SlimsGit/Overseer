package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler extends ListenerAdapter {

    String[] RoleIds = {
            "1152416627296567296", // Seller | RoleIds[0]
            "1152404554420342878", // Trusted 1 | RoleIds[1]
            "1152416793600741546", // Trusted 2
            "1152416819114692618", // Trusted 3
            "1152416880431218688", // Trusted 4
            "1152416987717304332" // Trusted 5
    };
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        //SLASH COMMANDS
        if(event.getName().equals("vouch")){
            event.deferReply().queue();

            OptionMapping useropt = event.getOption("user");
            OptionMapping reasonopt = event.getOption("reason");

            if(useropt == null | reasonopt == null){
                event.reply("Missing User");
            }

            User vouched = useropt.getAsUser();
            User user = event.getInteraction().getUser();
            String reason = reasonopt.getAsString();
            if (user.getId().equals(vouched.getId())){
                event.getHook().sendMessage("You Cannot Vouch Yourself").queue();
            }else if(reason.length()<10){
                event.getHook().sendMessage("Vouch Reasons Must Be 10+ Characters, Please Include The Trade Details.").queue();
            }else{
                try {
                    DataHandler.updateUserData(""+vouched.getId(), 1, 0,0);
                    DataHandler.updateUserData(""+user.getId(), 0, 1,0);
                    DataHandler.addVouchID(event,vouched,user, reason);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                event.getHook().sendMessage("**Successfully Vouched <@"+vouched.getId()+">: "+reason+"**").queue();
            }

        }else if(event.getName().equals("scam-vouch")){
            event.deferReply().queue();

            OptionMapping useropt = event.getOption("user");
            OptionMapping reasonopt = event.getOption("reason");

            if(useropt == null | reasonopt == null){
                event.reply("Missing User").queue();
            }

            User vouched = useropt.getAsUser();
            User user = event.getInteraction().getUser();
            String reason = reasonopt.getAsString();
            if (user.getId().equals(vouched.getId())){
                event.getHook().sendMessage("You Cannot Scam Vouch Yourself").queue();
            }else{

                try {
                    DataHandler.updateUserData(""+vouched.getId(), 0, 0,1);
                    DataHandler.updateUserData(""+user.getId(), 0, 0,0);
                    DataHandler.addScamVouchID(event,vouched,user,reason);
                    Member member = useropt.getAsMember();
                    event.getGuild().addRoleToMember(member, event.getGuild().getRoleById("1152423241416183878")).queue();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                event.getHook().sendMessage("**Successfully Scam Vouched <@"+vouched.getId()+">: "+reason+"**").queue();
            }
        }else if(event.getName().equals("vouches")){
            event.deferReply().queue();
            User user = event.getInteraction().getUser();
            if (DataHandler.findFromFile(user.getId()) == false){
                try {
                    DataHandler.addUser(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                event.getHook().sendMessage("**Successfully Initialized Your Data**").queue();
            }else{
                if(event.getOption("user") != null){
                    user = event.getOption("user").getAsUser();
                }
                String[] UserInfo = new String[0];
                try {
                    UserInfo = DataHandler.getSpecifiedData(user.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //event.getHook().sendMessage("--CONSTRUCTION--").queue();
                EmbedBuilder embed = new EmbedBuilder();
                //embed.setTitle(user.getName()+"'s Profile", "");
                embed.addField(user.getName()+"'s Profile ("+user.getId()+")", "\n**Vouches Received: **`"+UserInfo[1]+"`\n**Vouches Given: **`"+UserInfo[2]+"`\n**Scam Vouches: **`"+UserInfo[3]+"`", false);
                //embed.addField("Vouches Given: "+UserInfo[2], "", false);
                //embed.addField("Scam Vouches: "+UserInfo[3], "", false);
                embed.setThumbnail(user.getEffectiveAvatarUrl());
                embed.setFooter("Market Join Date | "+UserInfo[4]);

                Button recent = Button.primary("recent", "Check Recent Vouches");

                //embed.setColor(Color.DARK_GRAY);
                MessageEmbed eb = embed.build();
                event.getHook().sendMessageEmbeds(eb).setActionRow(recent).queue();
                embed.clear();
            }
        } else if (event.getName().equals("get-roles")) {
            String[] data;
            User user = event.getInteraction().getUser();
            Member member = event.getInteraction().getMember();
            try {
                data = DataHandler.getSpecifiedData(user.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Integer vouchesRec = Integer.parseInt(data[1]);
            System.out.println(vouchesRec);
            if(vouchesRec>=400){
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[5])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[4])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[3])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[2])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[1])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[0])).queue();
                event.reply("Given Trusted 5 & Below...").queue();
            } else if(vouchesRec>=250){
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[4])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[3])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[2])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[1])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[0])).queue();
                event.reply("Given Trusted 4 & Below...").queue();
            } else if(vouchesRec>=150){
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[3])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[2])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[1])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[0])).queue();
                event.reply("Given Trusted 3 & Below...").queue();
            } else if(vouchesRec>=75){
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[2])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[1])).queue();
                event.reply("Given Trusted 2 & Below...").queue();
            } else if(vouchesRec>=25){
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[1])).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[0])).queue();
                event.reply("Given Trusted 1 & Below...").queue();
            } else if(vouchesRec>=5){
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(RoleIds[0])).queue();
                event.reply("Given Seller").queue();
            } else {
                event.reply("Not eligible for any roles").queue();
            }
        }  // add more else if

    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        User user = event.getUser();
        if(DataHandler.findFromFile(user.getId())){
            System.out.println(user.getId()+" has already registered");
            //already joined
        }else{
            try {
                DataHandler.addUser(user);
                System.out.println("Added New User Data "+user.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("recent")) {

            MessageEmbed oldmsg = event.getMessage().getEmbeds().get(0);
            EmbedBuilder embed = new EmbedBuilder();

            String name = oldmsg.getFields().get(0).getName();
            Pattern pattern = Pattern.compile("\\((\\d+)\\)");
            Matcher matcher = pattern.matcher(name);
            String profileID = "0";
            if (matcher.find()) {
                profileID = matcher.group(1);
                //System.out.println("Extracted Profile ID: " + profileID);
            } else {
                System.out.println("Profile ID not found in the input string.");
            }

            try {
                DataHandler.getSpecifiedData(profileID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[][] vouchData;
            try {
                vouchData = DataHandler.getVouchedData(profileID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (vouchData.length == 0){
                embed.addField(""+name, "``User Has No Vouches!``", false);
            }else{
                String msgtoadd = "";
                for (int i = 0; i < vouchData.length; i++) {
                    if(vouchData[i][2] != profileID){
                        msgtoadd += "**ID#"+vouchData[i][0]+":** ``"+vouchData[i][3]+"``\n*Vouched by <@"+vouchData[i][2]+"> on "+vouchData[i][4]+"*\n\n";
                    }
                    //User vouchee = event.getJDA().getUserById(vouchData[i][2]);
                }
                embed.addField(""+name, ""+msgtoadd, false);
            }
            embed.setFooter(oldmsg.getFooter().getText());
            embed.setThumbnail(oldmsg.getThumbnail().getUrl());

            MessageEmbed msg = embed.build();
            event.editMessageEmbeds(msg).setActionRow(Button.secondary("backtoprofile", "<- Back")).queue();
            embed.clear();
        } else if (event.getButton().getId().equals("backtoprofile")) {

            MessageEmbed oldmsg = event.getMessage().getEmbeds().get(0);

            String name = oldmsg.getFields().get(0).getName();
            Pattern pattern = Pattern.compile("\\((\\d+)\\)");
            Matcher matcher = pattern.matcher(name);
            String profileID = "0";
            if (matcher.find()) {
                profileID = matcher.group(1);
                //System.out.println("Extracted Profile ID: " + profileID);
            } else {
                System.out.println("Profile ID not found in the input string.");
            }
            String[] UserInfo;
            try {
                UserInfo = DataHandler.getSpecifiedData(""+profileID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //User user = event.getJDA().getUserById();
            EmbedBuilder embed = new EmbedBuilder();
            //embed.setTitle(user.getName()+"'s Profile", "");
            embed.addField(""+name, "\n**Vouches Received: **`"+UserInfo[1]+"`\n**Vouches Given: **`"+UserInfo[2]+"`\n**Scam Vouches: **`"+UserInfo[3]+"`", false);
            //embed.addField("Vouches Given: "+UserInfo[2], "", false);
            //embed.addField("Scam Vouches: "+UserInfo[3], "", false);
            embed.setThumbnail(oldmsg.getThumbnail().getUrl());
            embed.setFooter("Market Join Date | "+UserInfo[4]);

            Button recent = Button.primary("recent", "Check Recent Vouches");

            //embed.setColor(Color.DARK_GRAY);
            MessageEmbed eb = embed.build();
            event.editMessageEmbeds(eb).setActionRow(recent).queue();
            embed.clear();
        }
    }
}
