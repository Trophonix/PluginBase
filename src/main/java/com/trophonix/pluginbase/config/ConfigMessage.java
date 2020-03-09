package com.trophonix.pluginbase.config;

import com.trophonix.pluginbase.utils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ConfigMessage {


  private String[] message;

  public ConfigMessage(@NotNull ConfigurationSection config, @NotNull String key) {
    if (config.isString(key)) {
      message = new String[]{Colors.color(config.getString(key))};
    } else {
      message = Colors.color(config.getStringList(key)).toArray(new String[0]);
    }
  }

  public String[] toArray(Object[] args) {
    return Arrays.stream(message).map(str -> {
      for (int i = 0; i < args.length - 1; i += 2) {
        str = str.replace(args[i].toString(), args[i + 1].toString());
      }
      return str;
    }).toArray(String[]::new);
  }

  public String join(CharSequence delimiter, Object[] args) {
    return String.join(delimiter, toArray(args));
  }

  public String getAsString(Object[] args) {
    return join(" ", args);
  }

  public void send(@NotNull CommandSender sender, @NotNull Object[] args) {
    for (String line : message) {
      for (int i = 0; i < args.length - 1; i += 2) {
        line = line.replace(args[i].toString(), args[i + 1].toString());
      }
      sender.sendMessage(line);
    }
  }

}
