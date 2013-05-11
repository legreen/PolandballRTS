package de.cirrus.polandball.level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.Sprite;
import de.cirrus.polandball.Team;
import de.cirrus.polandball.entities.Bullet;
import de.cirrus.polandball.entities.Entity;
import de.cirrus.polandball.particles.Explosion;
import de.cirrus.polandball.particles.Particle;
import de.cirrus.polandball.units.Unit;

public class Level {
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Particle> particles = new ArrayList<Particle>();

	public final int w, h;
	public double maxHeight = 64;
	public int[] tiles;
	public int xs, ys;
	public Blockmap blockmap;

	public List<Unit> units = new ArrayList<Unit>();


	public Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
		public int compare(Sprite s0, Sprite s1) {
			if (s0.y + s0.x < s1.y + s1.x) return -1;
			if (s0.y + s0.x > s1.y + s1.x) return 1;
			if (s0.x < s1.x) return -1;
			if (s0.x > s1.x) return 1;
			if (s0.y < s1.y) return -1;
			if (s0.y > s1.y) return 1;
			if (s0.z < s1.z) return -1;
			if (s0.z > s1.z) return 1;
			return 0;
		}
	};

	public Level(int w, int h) {
		this.w = w;
		this.h = h;

		blockmap = new Blockmap(w, h, 16);
		
		
		xs = w / 8 + 1;
		ys = h / 8;
		
		tiles = new int[xs*ys];
		
		for (int i = 0; i < xs*ys ; i++) {
			tiles[i] = 0;
		}
		
		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.allied);
			u.x = 16;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.allied);
			u.x = 24;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.allied);
			u.x = 32;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.allied);
			u.x = 48;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.soviet);
			u.x = 64;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.soviet);
			u.x = 128;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.soviet);
			u.x = 256;
			u.y = 16 + i * 24;
			add(u);
		}

		for (int i = 0; i < 8; i++) {
			Unit u = Unit.create(i, Team.soviet);
			u.x = 512;
			u.y = 16 + i * 24;
			add(u);
		}
	}

	public void add(Entity e) {
		entities.add(e);
		blockmap.add(e);
		e.init(this);
	}

	public void add(Particle p) {
		particles.add(p);
		p.init(this);
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);

			if (!e.removed) e.tick();
			if (e.removed) {
				blockmap.remove(e);
				e.onRemove();
				entities.remove(i--);
			} else {
				blockmap.update(e);
			}
		}

		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if (!p.removed) p.tick();
			if (p.removed) particles.remove(i--);
		}
	}

	public void renderBg(Bitmap bm) {
		for (int y = 0; y < ys; y++) {
			for (int x = 0; x < xs; x++) {
				bm.draw(Art.i.tiles[tiles[x+y*xs]][0], x*8, y*8);
			}
		}
	}

	public void renderSprites(Bitmap bm) {
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
		sortedSprites.addAll(entities);
		sortedSprites.addAll(particles);
		for (Sprite s : sortedSprites) {
			s.render(bm);
		}
	}

	public void renderShadows(Bitmap bm) {
		for (Sprite s : entities) {
			s.renderShadows(bm);
		}
		for (Sprite s : particles) {
			s.renderShadows(bm);
		}
	}

	public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) {
		return blockmap.getEntities(x0, y0, z0, x1, y1, z1);
	}

	public List<Unit> getUnitScreenSpace(double x0, double y0, double x1, double y1) {
		List <Unit> result = new ArrayList<Unit>();
		for (Unit u : units) {
			if (u.intersectsScreenSpace(x0, y0, x1, y1)) {
				result.add(u);
			}
		}
		return result;
	}

	public void explode(Bullet rocket, double x, double y, double z, int dmg, double radius) {
		double r = radius;
		List<Entity> entities = getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			//if (!(e instanceof Unit)) continue;
			//Unit u = (Unit) e;
			double xd = e.x - x;
			double yd = e.y - y;
			double zd = (e.z + e.zh / 2) - z;
			if (xd * xd + yd * yd + zd * zd > r * r) {
				double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
				xd /= dd;
				yd /= dd;
				zd /= dd;
				dd /= r;
				double falloff = (1 - dd) * 0.5 + 0.5;
				e.handleExplosion(rocket, (int) (dmg * falloff), xd * 5 * (1 - dd), yd * 5 * (1 - dd), zd * 5 * (1 - dd));
			}
		}
		add(new Explosion(x, y, z));
	}
}
