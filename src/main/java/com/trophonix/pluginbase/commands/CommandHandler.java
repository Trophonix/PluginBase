package com.trophonix.pluginbase.commands;

import com.trophonix.pluginbase.PluginBase;
import com.trophonix.pluginbase.config.I18n;
import com.trophonix.pluginbase.utils.FileUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandHandler implements TabExecutor {

  private final PluginBase base;

  private List<BaseCommand> commands = new ArrayList<>();

  private I18n i18n;

  public CommandHandler(PluginBase base) {
    this.base = base;
  }

  public void add(BaseCommand command) {
    commands.add(command);
    PluginCommand cmd = base.getCommand(command.getId());
    if (cmd != null) {
      cmd.setExecutor(this);
      cmd.setTabCompleter(this);
    }
  }

  @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    commands.stream().filter(command -> command.getId().equals(cmd.getName()))
        .findFirst().ifPresent(command -> {
          if (!command.onCommand(sender, args)) {
            i18n().send(sender, command.getHelpI18n(), new Object[]{});
          }
    });
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    BaseCommand command = commands.stream().filter(bc -> bc.getId().equals(cmd.getName()))
        .findFirst().orElse(null);
    if (command != null) {
      return command.onTabComplete(sender, args);
    }
    return null;
  }

  public I18n i18n() {
    if (i18n == null) {
      File folder = FileUtil.file(base.getDataFolder(), "lang/commands");
      if (!folder.exists()) folder.mkdirs();
      for (String i : base.getProvidedLanguages()) {
        String name = i + ".yml";
        base.saveResource("lang/commands/" + name, true);
      }
      i18n = new I18n(base.getDefaultLanguage(), folder);
    }
    return i18n;
  }

}
