package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Corrosive extends EcoEnchant {
    public Corrosive() {
        super(
                new EcoEnchantBuilder("corrosive", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;

        if(!(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        if (event.isCancelled())
            return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();
        Player victim = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!EnchantChecks.arrow(arrow, this)) return;

        int level = EnchantChecks.getArrowLevel(arrow, this);

        ArrayList<ItemStack> armor = new ArrayList<ItemStack>(Arrays.asList(victim.getInventory().getArmorContents()));
        if (armor.isEmpty())
            return;

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null)
                continue;


            if(armorPiece.equals(victim.getInventory().getHelmet())) {
                ItemDurability.damageItem(player, player.getInventory().getHelmet(), level, 39);
            }
            if(armorPiece.equals(victim.getInventory().getChestplate())) {
                ItemDurability.damageItem(player, player.getInventory().getChestplate(), level, 38);
            }
            if(armorPiece.equals(victim.getInventory().getLeggings())) {
                ItemDurability.damageItem(player, player.getInventory().getLeggings(), level, 37);
            }
            if(armorPiece.equals(victim.getInventory().getBoots())) {
                ItemDurability.damageItem(player, player.getInventory().getBoots(), level, 36);
            }
        }
    }
}
