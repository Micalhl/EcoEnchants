package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
public class Rapid extends EcoEnchant {
    public Rapid() {
        super(
                new EcoEnchantBuilder("rapid", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onRapidShoot(EntityShootBowEvent event) {
        if (event.getProjectile().getType() != EntityType.ARROW)
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double multiplier = 1 - ((this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-faster-per-level")/100) * level);

        if(event.getForce() < multiplier)
            return;

        double force = 1/event.getForce();
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(force));
    }
}
