import commands.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA bot = JDABuilder.createDefault("MTA2MDMzNjg5MTkyMjIzNTQ1Mg.Gz3QxE.yColCpaGW0LcKjko6ACnzB8WSsjCBBFhwGwFjE")
                .setActivity(Activity.watching("Egoist Matches"))
                .addEventListeners(new CommandHandler())
                .build().awaitReady();
    }
}