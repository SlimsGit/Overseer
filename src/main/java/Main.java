import commands.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA bot = JDABuilder.createDefault("MTE1MjM0NDk2Mzk2MzY5MTAzOQ.Geo8m_.dJTs3Pru8CY00B162uLLzZTyveXFq3J04OWCTk")
                .addEventListeners(new CommandHandler())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.listening("to #vouches"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();

        bot.upsertCommand("vouch", "Vouch A Member").setGuildOnly(true)
                .addOption(OptionType.USER, "user", "Member to vouch", true)
                .addOption(OptionType.STRING, "reason", "Reason for Vouch (10 Char Min.)", true)
                .queue();

        bot.upsertCommand("scam-vouch", "Scam Vouch A Member").setGuildOnly(true)
                .addOption(OptionType.USER, "user", "Member to Scam Vouch", true)
                .addOption(OptionType.STRING, "reason", "Reason for Vouch (10 Char Min.)", true)
                .queue();

        bot.upsertCommand("vouches", "Check Vouches").setGuildOnly(true)
                .addOption(OptionType.USER, "user", "checks yours by default", false)
                .queue();

        bot.upsertCommand("get-roles", "Update Your Roles").setGuildOnly(true)
                .queue();

    }
}