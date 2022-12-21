package com.beyound.dsem.meta.utils;

import org.bukkit.ChatColor;
import org.bukkit.Utility;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Utils {
    public String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public ItemStack updateItemStack(ItemStack inputStack, Map<String, Object> replacements) {
        ItemStack stack = inputStack.clone();
        ItemMeta meta = getItemMeta(stack);
        if (meta == null) {
            return stack;
        }

        if (meta.hasDisplayName()) {
            setDisplayName(stack, meta.getDisplayName(), replacements);
        }

        if (meta.hasLore()) {
            setLore(stack, meta.getLore(), replacements);
        }

        return stack;
    }

    private void setDisplayName(ItemStack stack, String name, Map<String, Object> replacements) {
        ItemMeta meta = getItemMeta(stack);
        if (meta == null) {
            return;
        }

        String formattedLine = formatLine(name, replacements);
        meta.setDisplayName(formattedLine);

        stack.setItemMeta(meta);
    }

    private void setLore(ItemStack stack, List<String> lore, Map<String, Object> replacements) {
        ItemMeta meta = getItemMeta(stack);
        if (meta == null) {
            return;
        }

        List<String> replacedLore = lore.stream()
                .map(line -> formatLine(line, replacements))
                .toList();
        meta.setLore(replacedLore);

        stack.setItemMeta(meta);
    }

    private String formatLine(String line, Map<String, Object> replacements) {
        String colored = color(line);
        return replace(colored, replacements);
    }

    private String replace(String value, Map<String, Object> replacements) {
        String replacedLine = value;
        for (Map.Entry<String, Object> e : replacements.entrySet()) {
            replacedLine = replacedLine.replace(e.getKey(), String.valueOf(e.getValue()));
        }

        return replacedLine;
    }

    private ItemMeta getItemMeta(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            return null;
        }

        return stack.getItemMeta();
    }
}
