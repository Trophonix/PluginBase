package com.trophonix.pluginbase.config;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {

  private Map<String, ConfigMessage> messages = new HashMap<>();

  public Config(FileConfiguration yaml) {
    for (String key : yaml.getKeys(true)) {
      if (yaml.isString(key) || yaml.isList(key)) {
        messages.put(key, new ConfigMessage(yaml, key));
      }
    }
  }

  /**
   * Get a ConfigMessage from the config
   * @param key Key the message is saved under in the config
   * @return Specified ConfigMessage
   */
  public ConfigMessage getMessage(String key) {
    return messages.get(key);
  }

  /**
   * Sends a ConfigMessage to a sender
   * @param sender Sender to send it to
   * @param key Key the message is saved under in the config
   * @param args Replacements
   * @see ConfigMessage#send(CommandSender, Object[])
   */
  public void send(CommandSender sender, String key, Object[] args) {
    getMessage(key).send(sender, args);
  }

}