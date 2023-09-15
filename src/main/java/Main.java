import commands.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA bot = JDABuilder.createDefault("MTE1MjM0NDk2Mzk2MzY5MTAzOQ.Geo8m_.dJTs3Pru8CY00B162uLLzZTyveXFq3J04OWCTk")
                .addEventListeners(new CommandHandler())
                .build().awaitReady();
    }
}