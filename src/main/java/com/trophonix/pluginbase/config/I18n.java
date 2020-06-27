package com.trophonix.pluginbase.config;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class I18n {

  private static final FilenameFilter LANG_FILTER = (dir, name) ->
      (name.endsWith(".yml") || name.endsWith(".yaml"));

  private Map<String, String> staticArgs = new HashMap<>();

  private String defaultLanguage;

  private Map<String, Config> languages = new HashMap<>();

  public I18n(String defaultLanguage, File folder) {
    this.defaultLanguage = defaultLanguage.toLowerCase();
    File[] children = folder.listFiles(LANG_FILTER);
    if (children != null) {
      for (File child : children) {
        FileConfiguration yaml = new YamlConfiguration();
        yaml.options().copyDefaults(true);
        try {
          yaml.load(child);
        } catch (IOException | InvalidConfigurationException ignored) { }
        languages.put(child.getName().substring(0, child.getName().indexOf(".")).toLowerCase(), new Config(yaml));
      }
    }
  }

  /**
   * Add a static argument to replace in all messages
   * @param from String to replace
   * @param to String to put there instead
   */
  public void addStaticArg(String from, String to) {
    staticArgs.put(from, to);
  }

  /**
   * Gets a player's language or returns default if not a player
   * @param sender Sender to get language for
   * @return A language
   */
  public String getLanguage(CommandSender sender) {
    if (sender instanceof Player) {
      return ((Player) sender).getLocale().toLowerCase();
    }
    return defaultLanguage;
  }

  /**
   * Get a ConfigMessage by language and key
   * @param language Language formatted like Minecraft, e.g. "en_us"
   * @param key Key the message is saved under in the config
   * @return Specified ConfigMessage
   */
  @Nullable public ConfigMessage getMessage(@NotNull String language, @NotNull String key) {
    Config config = languages.get(language);
    if (config == null) {
      String base = language.split("_")[0];
      for (String lang : languages.keySet()) {
        if (lang.startsWith(base)) {
          language = lang;
          config = languages.get(lang);
          break;
        }
      }
    }
    if (!defaultLanguage.equals(language) && (config == null || config.getMessage(key) == null)) {
      config = languages.get(defaultLanguage);
    }
    return config != null ? config.getMessage(key) : null;
  }

  /**
   * Get a ConfigMessage by sender and key.
   * If the sender is a Player, will get their language.
   * If the sender is not a Player, will use default.
   * @param sender Sender to get message for
   * @param key Key the message is saved under in the config
   * @return Specified ConfigMessage
   */
  @Nullable public ConfigMessage getMessage(@NotNull CommandSender sender, @NotNull String key) {
    return getMessage(getLanguage(sender), key);
  }

  /**
   * Send a ConfigMessage to a sender.
   * @param sender Sender to send message to
   * @param key Key the message is saved under in the config
   * @param args Replacements
   * @see Config#send(CommandSender, String, Object[])
   */
  public void send(@NotNull CommandSender sender, @NotNull String key, @NotNull Object[] args) {
    ConfigMessage message = getMessage(sender, key);
    if (message != null) {
      Object[] fixed = new Object[args.length + (staticArgs.size() * 2)];
      int i = 0 ;
      for (Object arg : args) {
        fixed[i++] = arg;
      }
      for (Map.Entry<String, String> entry : staticArgs.entrySet()) {
        fixed[i++] = entry.getKey();
        fixed[i++] = entry.getValue();
      }
      message.send(sender, fixed);
    }
  }

  /**
   * Sends a ConfigMessage to a sender with no replacements.
   * @param sender The sender
   * @param key The key of the message
   */
  public void send(@NotNull CommandSender sender, @NotNull String key) {
    send(sender, key, new Object[0]);
  }

}
