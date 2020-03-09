package com.trophonix.pluginbase.commands;

import com.google.common.collect.ImmutableList;
import com.trophonix.pluginbase.config.I18n;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
public abstract class BaseCommand {

  private CommandHandler handler;
  @Getter private final String id;
  @Getter private ImmutableList<String> aliases;
  @Getter private String helpI18n;

  public abstract boolean onCommand(CommandSender sender, String[] args);

  public abstract List<String> onTabComplete(CommandSender sender, String[] args);

  public boolean isAlias(String cmd) {
    return id.equalsIgnoreCase(cmd) || aliases.contains(cmd.toLowerCase());
  }

  protected I18n i18n() {
    return handler.i18n();
  }

}
