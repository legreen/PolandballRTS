package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.AssaultRifle;

public class Russia extends Mob {

	public Russia(Player player) {
		super(4, player);
		
		maxHealth = health = 125;
		visRange = 10;
		
		weapon = new AssaultRifle(this);
	}

}
