package com.perengano99.PinkiRanks.nametag;

import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityArmorStand;
import net.minecraft.server.v1_13_R1.World;

public class MobNamed extends EntityArmorStand {

	public float bj;

	public MobNamed(World world) {
		super(world);

		this.setSize(0.2F, 0.1F);
		this.setSilent(true);
		this.dropDeathLoot(false, 0);
		this.setInvisible(true);
		this.setBasePlate(false);
		this.addScoreboardTag("PinkiMobNamer");
		
	}
	
	@Override
	protected void checkBlockCollisions() {
		return;

	}
	
	@Override
	public boolean isInteractable() {
		return false;
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}

	@Override
	public void stopRiding() {
		Entity entity = this.getVehicle();
		if (entity != null && entity != this.getVehicle() && !this.world.isClientSide) {
			this.A(entity);
		}

	}
	
}