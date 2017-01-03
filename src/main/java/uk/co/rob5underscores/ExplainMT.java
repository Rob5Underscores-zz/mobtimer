package uk.co.rob5underscores;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by ROB on 03/01/2017.
 */
public class ExplainMT implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        String explanation = "This measure is currently necessary to keep the server running smoothly as the server software in use has serious issues deciding where mobs should spawn! This will be removed whenever possible!";

        Text text = Text.builder(explanation).build().concat(Text.NEW_LINE).concat(Text.builder("Thank you for your understanding!").color(TextColors.GREEN).build());
        commandSource.sendMessage(text);

        return CommandResult.success();
    }
}
