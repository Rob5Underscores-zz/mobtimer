package uk.co.rob5underscores;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by ROB on 03/01/2017.
 */

@Plugin(id = "mobtimer", name = "MobTimer", version = "1.0")
public class MobTimer {

    @Inject
    private Logger logger;

    private boolean isNight;
    private boolean isLagging;

    private static final Text prefix = Text.builder("[MobTimer] ").color(TextColors.GOLD).build();

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        isNight = false;
        isLagging = false;

        if(!Sponge.getServer().getDefaultWorld().isPresent()) {
            //Disable plugin
            Sponge.getGame().getEventManager().unregisterPluginListeners(this);
        }

        Scheduler scheduler = Sponge.getScheduler();
        Task.Builder taskBuilder = scheduler.createTaskBuilder();

        CommandSpec commandSpec = CommandSpec.builder().arguments(GenericArguments.none()).executor(new ExplainMT()).build();

        Sponge.getCommandManager().register(this, commandSpec, "explainmt");

        taskBuilder.execute(
                () -> {

                    if(Sponge.getServer().getTicksPerSecond() < 12) {
                        if(!isLagging) {
                            isLagging = true;
                            Sponge.getServer().getDefaultWorld().get().setGameRule("doMobSpawning", "false");
                            Sponge.getServer().getDefaultWorld().get().setGameRule("doMobSpawning", "true");
                            Text text = Text.builder("Mob Spawning has been disabled due to low TPS!").color(TextColors.DARK_RED).build().concat(Text.NEW_LINE).concat(Text.builder("Please click to see why this is necessary!").color(TextColors.GRAY).onClick(TextActions.runCommand("/explainmt")).build());
                            for(Player player : Sponge.getServer().getOnlinePlayers()) {
                                player.sendMessage(prefix.concat(text));
                            }
                        }
                    }

                    if(Sponge.getServer().getDefaultWorld().get().getWorldTime() > 13000L && Sponge.getServer().getDefaultWorld().get().getWorldTime() > 23000L){
                        if(isNight == false) {
                            isNight = true;
                            isLagging = false;
                            Sponge.getServer().getDefaultWorld().get().setGameRule("doMobSpawning", "true");
                            Text text = Text.builder("Night time has begun! Mob Spawning is enabled!").color(TextColors.DARK_RED).build().concat(Text.NEW_LINE).concat(Text.builder("Please click to see why this is necessary!").color(TextColors.GRAY).onClick(TextActions.runCommand("/explainmt")).build());
                            for(Player player : Sponge.getServer().getOnlinePlayers()) {
                                player.sendMessage(prefix.concat(text));
                            }
                        }
                    } else {
                        if(isNight == true) {
                            isNight = false;
                            Sponge.getServer().getDefaultWorld().get().setGameRule("doMobSpawning", "false");
                            Text text = Text.builder("Night time has ended! Mob Spawning is disabled!").color(TextColors.DARK_RED).build().concat(Text.NEW_LINE).concat(Text.builder("Please click to see why this is necessary!").color(TextColors.GRAY).onClick(TextActions.runCommand("/explainmt")).build());
                            for(Player player : Sponge.getServer().getOnlinePlayers()) {
                                player.sendMessage(prefix.concat(text));
                            }
                        }
                    }
                }
        ).interval(5, TimeUnit.SECONDS).submit(this);
    }

}
