package com.perengano99.PinkiRanks.nametag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.google.common.collect.Maps;
import com.perengano99.PinkiRanks.PC;
import com.perengano99.PinkiRanks.NametagAPI.packets.ChannelPacketHandler;
import com.perengano99.PinkiRanks.NametagAPI.packets.IPacketHandler;
import com.perengano99.PinkiRanks.files.ConfigFile;
import com.perengano99.PinkiRanks.listener.Rank;
import com.perengano99.PinkiRanks.placeholder.LegacyPlaceholders;

public class CreateNametag implements Listener {
    
    private static PC pc = PC.p;
    private static IPacketHandler packets = new ChannelPacketHandler(pc);
    
    private static int idUN = -1;
    
    public static HashMap<UUID, Entity> namerOwner = Maps.newHashMap();
    public static HashMap<Entity, UUID> Owner = Maps.newHashMap();
    private static HashMap<UUID, ArmorStand> namerMob = Maps.newHashMap();
    private static HashMap<UUID, Boolean> spawn = Maps.newHashMap();
    
    @EventHandler
    public static void armSpawn(PlayerJoinEvent event) {
	Player player = event.getPlayer();
	
	Entity namer = null;
	namerOwner.put(player.getUniqueId(), namer);
    }
    
    public static void updateNamer() {
	
	idUN = Bukkit.getScheduler().scheduleSyncRepeatingTask(PC.p, new Runnable() {
	    
	    public void run() {
		
		Set<UUID> uuid = namerOwner.keySet();
		for (UUID udid : uuid) {
		    Player player = Bukkit.getPlayer(udid);
		    Rank group = Rank.getRank(player);
		    
		    Entity namer = namerOwner.get(player.getUniqueId());
		    
		    if (ConfigFile.getConfig().getBoolean("general.use-nametags", true)) {
			if ((namer == null || namer.isDead()) && !player.isDead()) {
			    ArmorStand newNamer = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
			    newNamer.setGravity(false);
			    newNamer.setVisible(false);
			    newNamer.setBasePlate(false);
			    newNamer.setSmall(false);
			    namer = newNamer;
			    
			    namer.addScoreboardTag("PinkiMobNamer");
			    namerOwner.put(player.getUniqueId(), namer);
			    Owner.put(newNamer, player.getUniqueId());
			    namerMob.put(player.getUniqueId(), (ArmorStand) namer);
			    spawn.put(player.getUniqueId(), true);
			    
			    if (!ConfigFile.getConfig().getBoolean("general.see-proper-nametag", true)) {
				packets.sendEntityDestroyPacket(namer.getEntityId(), player);
			    }
			}
		    }
		    
		    if (namer == null)
			return;
		    
		    if (!ConfigFile.getConfig().getBoolean("general.use-nametags", true)) {
			for (Player allPlayers : PC.p.getServer().getOnlinePlayers()) {
			    packets.sendEntityDestroyPacket(namer.getEntityId(), allPlayers);
			}
			Location locdeath = player.getLocation().add(0, -3000, 0);
			((LivingEntity) namer).teleport(locdeath);
			((LivingEntity) namer).setHealth(0);
			((LivingEntity) namer).remove();
			return;
		    }
		    
		    String name;
		    
		    String playerName;
		    String prefix;
		    String suffix;
		    
		    if (player.isSneaking()) {
			namer.setCustomNameVisible(false);
		    } else {
			namer.setCustomNameVisible(true);
		    }
		    
		    prefix = group.getNTPrefix();
		    playerName = "&r" + player.getName();
		    suffix = group.getNTSuffix();
		    
		    playerName = group.getNTColor() + player.getName();
		    
		    if (NametagUtil.isRenamed(player)) {
			playerName = NametagUtil.getNickName(player);
		    }
		    
		    name = pc.color(playerName);
		    
		    if (group.hasNametag()) {
			name = pc.color(LegacyPlaceholders.applyVariables(player, prefix + playerName + suffix));
		    }
		    
		    if (group.hasNTAnim() && group.hasNametag()) {
			name = pc.color(LegacyPlaceholders.apllyAnimations((LegacyPlaceholders.applyVariables(player, prefix + playerName + suffix))));
			
		    }
		    namer.setCustomName(name);
		    
		}
	    }
	}, 0, ConfigFile.getConfig().getInt("general.update-delay"));
    }
    
    @EventHandler
    public void tpName(PlayerMoveEvent event) {
	Player player = event.getPlayer();
	
	Entity namer = namerOwner.get(player.getUniqueId());
	if (!(namer == null)) {
	    
	    if (!namer.getLocation().equals(player.getLocation())) {
		if (player.isSwimming()) {
		    if (((ArmorStand) namer).isSmall() == false) {
			((ArmorStand) namer).setSmall(true);
		    }
		    Location underWaterL;
		    underWaterL = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.2, player.getLocation().getZ());
		    namer.teleport(underWaterL);
		    return;
		}
		if (((ArmorStand) namer).isSmall() == true) {
		    ((ArmorStand) namer).setSmall(false);
		}
		namer.teleport(player.getLocation());
	    }
	}
    }
    
    @EventHandler
    public void teleporttpName(PlayerTeleportEvent event) {
	Player player = event.getPlayer();
	
	Entity namer = namerOwner.get(player.getUniqueId());
	if (!(namer == null)) {
	    namer.teleport(event.getTo());
	}
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
	Player player = event.getEntity();
	
	Set<UUID> uuid = namerOwner.keySet();
	
	for (UUID udid : uuid) {
	    Player owner = Bukkit.getPlayer(udid);
	    Entity namer = namerOwner.get(owner.getUniqueId());
	    
	    if (!(namer == null)) {
		if (player.equals(owner)) {
		    namer.remove();
		}
	    }
	}
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void preventDamage(EntityDamageEvent event) {
	Entity entity = event.getEntity();
	
	Set<UUID> uuid = namerOwner.keySet();
	
	for (UUID udid : uuid) {
	    Player player = Bukkit.getPlayer(udid);
	    Entity namer = namerOwner.get(player.getUniqueId());
	    Player owner = Bukkit.getPlayer(Owner.get(namer));
	    
	    if (entity.equals(namer)) {
		if (event.getCause() == (DamageCause.BLOCK_EXPLOSION) || event.getCause() == (DamageCause.CONTACT)
			|| event.getCause() == (DamageCause.ENTITY_EXPLOSION) || event.getCause() == (DamageCause.DRAGON_BREATH)
			|| event.getCause() == (DamageCause.LAVA) || event.getCause() == (DamageCause.MELTING)
			|| event.getCause() == (DamageCause.ENTITY_SWEEP_ATTACK) || event.getCause() == (DamageCause.FIRE)
			|| event.getCause() == (DamageCause.FIRE_TICK) || event.getCause() == (DamageCause.HOT_FLOOR)
			|| event.getCause() == (DamageCause.LIGHTNING) || event.getCause() == (DamageCause.MAGIC) || event.getCause() == (DamageCause.POISON)
			|| event.getCause() == (DamageCause.VOID)) {
		    
		    event.setCancelled(true);
		    return;
		}
		
		EntityDamageByEntityEvent eevent = new EntityDamageByEntityEvent(owner, namer, DamageCause.ENTITY_ATTACK, event.getDamage());
		Entity damager = eevent.getDamager();
		Entity damaged = eevent.getEntity();
		
		if (damaged.equals(namer)) {
		    if (damager.equals(owner)) {
			eevent.setCancelled(true);
			event.setCancelled(true);
			return;
		    }
		}
		
		double damage = event.getDamage();
		owner.damage(damage);
		
		event.setCancelled(true);
		
	    }
	}
    }
    
    @EventHandler
    public void armRemove(PlayerQuitEvent event) {
	Player player = event.getPlayer();
	
	Entity namer = namerOwner.get(player.getUniqueId());
	if (!(namer == null)) {
	    namerOwner.remove(player.getUniqueId());
	    Owner.remove(namer);
	    spawn.remove(player.getUniqueId());
	    namer.remove();
	}
	
    }
    
    public static boolean isUnderWater(Entity entity) {
	Location eloc = entity.getLocation();
	
	Block block = eloc.getBlock();
	
	List<Block> blocks = new ArrayList<Block>();
	
	Block blockY = block.getRelative(0, 1, 0);
	Block blocky = block.getRelative(0, 2, 0);
	blocks.add(blocky);
	blocks.add(blockY);
	
	for (Block checked : blocks) {
	    if (checked.isLiquid()) {
		return true;
	    }
	}
	
	return false;
    }
    
    private static void loader(Player player) {
	namerOwner.put(player.getUniqueId(), null);
	
	Set<UUID> uuid = namerOwner.keySet();
	for (UUID udid : uuid) {
	    Player pl = Bukkit.getPlayer(udid);
	    for (Entity entity : pl.getNearbyEntities(0, 0, 0)) {
		namerOwner.put(pl.getUniqueId(), entity);
		spawn.put(pl.getUniqueId(), true);
	    }
	}
    }
    
    public static void stopNamer() {
	Bukkit.getScheduler().cancelTask(idUN);
	idUN = -1;
	for (Player player : pc.getServer().getOnlinePlayers()) {
	    Entity namer = namerOwner.get(player.getUniqueId());
	    namer.remove();
	}
    }
    
    public static void loadNamer() {
	
	for (Player player : PC.p.getServer().getOnlinePlayers()) {
	    loader(player);
	}
	NametagUtil.clearNameTagNamers();
	updateNamer();
    }
    
}
