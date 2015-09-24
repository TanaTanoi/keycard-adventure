package gameObjects.objects;
import javafx.geometry.BoundingBox;
import gameObjects.strategies.WeaponStrategy;

public class Weapon extends Tool {

	public Weapon(String name, String desc, int damage) {
		super(name, desc);
		super.setStrategy(new WeaponStrategy(damage));
	}

	@Override
	public void equip() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
