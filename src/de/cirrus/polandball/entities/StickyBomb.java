package de.cirrus.polandball.entities;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.particles.SmokeDebris;
import de.cirrus.polandball.units.Unit;
import de.cirrus.polandball.weapons.Weapon;

public class StickyBomb extends Bullet {

	public StickyBomb(Unit owner, Weapon weapon, double xa, double ya, double za, int dmg) {
		super(owner, weapon, xa, ya, za, dmg);
		this.xa = xa * 1;
		this.ya = ya * 1;
		this.za = za * 1 + 1;
	}

	public boolean blocks(Entity e) {
		return false;
	}

	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		super.tick();
		za -= 0.08;
		attemptMove();
	}

	public void renderShadows(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		b.fill(xp - 1, yp, xp, yp, 1);
	}

	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) (y - z);
		int frame = 0;
		if (xa == 0 && ya == 0 && za == 0) frame = 1;
		b.draw(Art.i.projectiles[frame][1], xp - 4, yp - 4);
	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (e != null) {
			if (xxa != 0) xa *= -0.7;
			if (yya != 0) ya *= -0.7;
			if (zza != 0) za *= -0.7;
		} else {
			xa = ya = za = 0;
		}
	}

	public void detonate() {
		level.explode(this, x, y, z, dmg, 32);
		remove();
	}

	public void handleExplosion(Bullet source, int dmg, double xd, double yd, double zd) {
		if (owner.team == source.owner.team) {
			return;
		}
		if (xa == 0 && ya == 0 && za == 0) {
			xa+=xd*0.5;
			ya+=xd*0.5;
			za+=Math.sqrt(xd*xd+yd*yd)*0.2;
		}
	}
	
	public void fizzle() {
		remove();
		level.add(new SmokeDebris(x, y, z));
	}
}
