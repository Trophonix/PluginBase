package com.trophonix.pluginbase.utils;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum Colors {

  BLACK(ChatColor.BLACK, DyeColor.BLACK, Color.BLACK),
  DARK_BLUE(ChatColor.DARK_BLUE, DyeColor.BLUE, Color.BLUE),
  DARK_GREEN(ChatColor.DARK_GREEN, DyeColor.GREEN, Color.GREEN),
  DARK_AQUA(ChatColor.DARK_AQUA, DyeColor.CYAN, Color.AQUA),
  DARK_RED(ChatColor.DARK_RED, DyeColor.RED, Color.RED),
  DARK_PURPLE(ChatColor.DARK_PURPLE, DyeColor.PURPLE, Color.PURPLE),
  GOLD(ChatColor.GOLD, DyeColor.ORANGE, Color.ORANGE),
  GRAY(ChatColor.GRAY, DyeColor.GRAY, Color.GRAY),
  DARK_GRAY(ChatColor.DARK_GRAY, DyeColor.GRAY, Color.GRAY),
  BLUE(ChatColor.BLUE, DyeColor.BLUE, Color.BLUE),
  GREEN(ChatColor.GREEN, DyeColor.GREEN, Color.GREEN),
  AQUA(ChatColor.AQUA, DyeColor.LIGHT_BLUE, Color.BLUE),
  RED(ChatColor.RED, DyeColor.RED, Color.RED),
  PINK(ChatColor.LIGHT_PURPLE, DyeColor.PINK, Color.PURPLE),
  YELLOW(ChatColor.YELLOW, DyeColor.YELLOW, Color.YELLOW),
  WHITE(ChatColor.WHITE, DyeColor.WHITE, Color.WHITE)
  ;

  public final ChatColor chat;
  public final DyeColor dye;
  public final Color bukkitColor;

  @Contract("_, null -> null")
  public static String color(@NotNull char c, @Nullable String string) {
    if (string == null) return null;
    return ChatColor.translateAlternateColorCodes(c, string);
  }

  @Contract("null -> null")
  public static String color(@Nullable String string) {
    return color('&', string);
  }

  @Contract("null -> null")
  public static List<String> color(@Nullable List<String> list) {
    if (list == null) return null;
    return list.stream().map(Colors::color).collect(Collectors.toList());
  }

}
