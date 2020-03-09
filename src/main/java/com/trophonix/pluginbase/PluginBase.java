package com.trophonix.pluginbase;

import com.trophonix.pluginbase.commands.CommandHandler;
import com.trophonix.pluginbase.config.I18n;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

public class PluginBase extends JavaPlugin {

  private I18n i18n;
  private CommandHandler commandHandler;

  @Setter @Getter private @Builder.Default String defaultLanguage = "en_us";
  @Setter @Getter private Set<String> providedLanguages;

  public I18n i18n() {
    for (String lang : providedLanguages) {
      try {
        saveResource(String.format("lang/%s.yml", lang), true);
      } catch (IllegalArgumentException ignored) { }
    }
    if (i18n == null) {
      i18n = new I18n(defaultLanguage, new File(getDataFolder(), "lang"));
    }
    return i18n;
  }

  public CommandHandler commands() {
    if (commandHandler == null) {
      commandHandler = new CommandHandler(this);
    }
    return commandHandler;
  }

}
