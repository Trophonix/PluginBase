package com.trophonix.pluginbase.utils;

import com.trophonix.pluginbase.PluginBase;
import com.trophonix.pluginbase.config.ConfigMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ItemBuilder implements Cloneable {

  private @Delegate(excludes = DelegateExclusions.class) ItemStack stack;
  private ItemMeta meta;

  public ItemBuilder(ItemStack stack) {
    this.stack = stack.clone();
    meta = this.stack.getItemMeta();
  }

  public ItemBuilder(Material material, int amount) {
    stack = new ItemStack(material, amount);
    meta = stack.getItemMeta();
    if (meta == null) meta = Bukkit.getItemFactory().getItemMeta(material);
  }

  public ItemBuilder(Material material) {
    this(material, 1);
  }

  public ItemBuilder(ConfigurationSection section, String key) {
    stack = ItemStack.deserialize(section.getConfigurationSection(key).getValues(true));
    meta = stack.getItemMeta();
  }

  public ItemBuilder clone() {
    ItemStack stack = this.stack.clone();
    stack.setItemMeta(meta);
    return new ItemBuilder(stack);
  }

  public void setInConfig(ConfigurationSection config, String key) {
    stack.setItemMeta(meta);
    config.set(key, stack.serialize());
  }

  public ItemStack build() {
    stack.setItemMeta(meta);
    return stack;
  }

  public ItemStack build(PluginBase base, Player player, Object[] args) {
    String display = meta.getDisplayName();
    ConfigMessage displayMessage = base.i18n().getMessage(player, display);
    if (displayMessage != null) display = displayMessage.getAsString(args);
    meta.setDisplayName(display);
    List<String> lore = meta.getLore();
    if (lore != null && lore.size() == 1) {
      ConfigMessage loreMessage = base.i18n().getMessage(player, lore.get(0));
      if (loreMessage != null) lore = Arrays.asList(loreMessage.toArray(args));
      meta.setLore(lore);
    }
    stack.setItemMeta(meta);
    return stack;
  }

  public ItemBuilder displayName(String displayName) {
    meta.setDisplayName(Colors.color(displayName));
    return this;
  }

  public ItemBuilder lore(@Nullable List<String> lore) {
    meta.setLore(Colors.color(lore));
    return this;
  }

  public ItemBuilder lore(@Nullable String... lore) {
    return lore(Arrays.asList(lore != null ? lore : new String[]{}));
  }

  public ItemBuilder addLore(@NotNull String string) {
    if (!meta.hasLore()) {
      meta.setLore(new ArrayList<>(Collections.singletonList(string)));
    } else {
      List<String> lore = meta.getLore();
      lore.add(string);
      meta.setLore(lore);
    }
    return this;
  }

  public ItemBuilder replace(@NotNull String from, @Nullable Object arg) {
    String to = arg != null ? arg.toString() : "null";
    if (meta.hasDisplayName()) meta.setDisplayName(meta.getDisplayName().replace(from, to));
    if (meta.hasLore()) {
      List<String> lore = meta.getLore();
      for (int i = 0; i < lore.size(); i++) {
        lore.set(i, lore.get(i).replace(from, to));
      }
      meta.setLore(lore);
    }
    return this;
  }

  public ItemBuilder enchant(Enchantment enchant, int level) {
    meta.addEnchant(enchant, level, level > enchant.getMaxLevel());
    return this;
  }

  public ItemBuilder enchant(Enchantment... enchants) {
    for (Enchantment enchant : enchants) enchant(enchant, 1);
    return this;
  }

  public ItemBuilder flag(ItemFlag... flags) {
    meta.addItemFlags(flags);
    return this;
  }

  public ItemBuilder applyToStack(Consumer<ItemStack> consumer) {
    stack.setItemMeta(meta);
    consumer.accept(stack);
    meta = stack.getItemMeta();
    return this;
  }

  public ItemBuilder applyToMeta(Consumer<ItemMeta> consumer) {
    consumer.accept(meta);
    return this;
  }

  public <T> ItemBuilder applyToMetaAs(Class<T> clazz, Consumer<T> consumer) {
    if (clazz.isInstance(meta)) {
      consumer.accept((T)meta);
    }
    return this;
  }

  private static class DelegateExclusions {
    public ItemStack clone() { return null; }
  }

}
