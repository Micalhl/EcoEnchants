package com.willfp.ecoenchants.display;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.util.Logger;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

@SuppressWarnings("deprecation")
public class EnchantmentCache {
    private static final Set<CacheEntry> CACHE = new HashSet<>();

    public static CacheEntry getEntry(Enchantment enchantment) {
        Optional<CacheEntry> matching = CACHE.stream().filter(entry -> entry.getEnchantment().getKey().getKey().equals(enchantment.getKey().getKey())).findFirst();
        return matching.orElse(new CacheEntry(enchantment, EnchantDisplay.PREFIX + "§7" + enchantment.getKey().getKey(), enchantment.getKey().getKey(), Collections.singletonList(EnchantDisplay.PREFIX + "No Description Found"), EcoEnchant.EnchantmentType.NORMAL));
    }

    public static Set<CacheEntry> getCache() {
        return new HashSet<>(CACHE);
    }

    public static void update() {
        CACHE.clear();
        Arrays.asList(Enchantment.values()).parallelStream().forEach(enchantment -> {
            String name;
            String color;
            EcoEnchant.EnchantmentType type;
            List<String> description;
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment);
                description = ecoEnchant.getDescription();
                name = ecoEnchant.getName();
                type = ecoEnchant.getType();
            } else {
                description = Arrays.asList(
                        WordUtils.wrap(
                                String.valueOf(ConfigManager.getLang().getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".description")),
                                ConfigManager.getConfig().getInt("lore.describe.wrap"),
                                "\n", false
                        ).split("\\r?\\n")
                );
                name = String.valueOf(ConfigManager.getLang().getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".name"));
                type = enchantment.isCursed() ? EcoEnchant.EnchantmentType.CURSE : EcoEnchant.EnchantmentType.NORMAL;
            }

            color = type.getColor();

            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                EnchantmentRarity rarity = EcoEnchants.getFromEnchantment(enchantment).getRarity();
                if(rarity != null) {
                    if (rarity.hasCustomColor() && type != EcoEnchant.EnchantmentType.CURSE) {
                        color = rarity.getCustomColor();
                    }
                } else {
                    Logger.warn("Enchantment " + enchantment.getKey().getKey() + " has an invalid rarity");
                }
            }

            String rawName = name;
            name = color + name;
            description.replaceAll(line -> EnchantDisplay.PREFIX + EnchantDisplay.descriptionColor + line);
            CACHE.add(new CacheEntry(enchantment, name, rawName, description, type));
        });
    }


    public static class CacheEntry {
        private final Enchantment enchantment;
        private final String name;
        private final String rawName;
        private final List<String> description;
        private final String stringDescription;
        private final EcoEnchant.EnchantmentType type;

        public CacheEntry(Enchantment enchantment, String name, String rawName, List<String> description, EcoEnchant.EnchantmentType type) {
            this.enchantment = enchantment;
            this.name = name;
            this.rawName = rawName;
            this.description = description;
            this.type = type;


            StringBuilder descriptionBuilder = new StringBuilder();

            description.forEach(s -> {
                descriptionBuilder.append(s);
                descriptionBuilder.append(" ");
            });

            String stringDescription = descriptionBuilder.toString();
            stringDescription = stringDescription.replaceAll("§w", "");
            this.stringDescription = stringDescription.replaceAll(EnchantDisplay.descriptionColor, "");
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public String getName() {
            return name;
        }

        public String getRawName() {
            return rawName;
        }

        public List<String> getDescription() {
            return description;
        }

        public String getStringDescription() {
            return stringDescription;
        }

        public EcoEnchant.EnchantmentType getType() {
            return type;
        }

        @Override
        public String toString() {
            return "CacheEntry{" +
                    "enchantment=" + enchantment +
                    "§f, name='" + name + '\'' +
                    "§f, rawName='" + rawName + '\'' +
                    "§f, description=" + description +
                    "§f, stringDescription='" + stringDescription + '\'' +
                    "§f}";
        }
    }
}
