package com.nutria2;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Bola de nieve que empuja pero NO quita vida. */
public class NutriaSnowballEntity extends ThrowableItemProjectile {
	// Ajusta aqui la fuerza del empuje
	private static final double NORMAL_PUSH = 1.2;
	private static final double ADMIN_PUSH = 3.5;
	private static final double ADMIN_UP = 0.6;

	public NutriaSnowballEntity(EntityType<? extends NutriaSnowballEntity> type, Level level) {
		super(type, level);
	}

	public NutriaSnowballEntity(Level level, LivingEntity owner) {
		super(ModEntities.SNOWBALL, owner, level);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.NUTRIA_SNOWBALL;
	}

	private boolean isAdmin() {
		return getItem().is(ModItems.ADMIN_SNOWBALL);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity target = result.getEntity();
		// 0 de dano: no baja vida
		target.hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
		if (!this.level().isClientSide && target instanceof LivingEntity living) {
			Vec3 v = this.getDeltaMovement();
			boolean admin = isAdmin();
			living.knockback(admin ? ADMIN_PUSH : NORMAL_PUSH, -v.x, -v.z);
			if (admin) {
				living.push(0, ADMIN_UP, 0);
			}
			living.hurtMarked = true; // sincroniza el empuje con el jugador
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!this.level().isClientSide) {
			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; i++) {
				this.level().addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}
}
