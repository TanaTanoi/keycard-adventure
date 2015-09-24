package gameObjects;

public class Potion extends Tool{

	public Potion(String name, String desc, int effect) {
		super(name, desc);
		super.setStrategy(new PotionStrategy(effect));
	}

	@Override
	public void equip() {
		// TODO Auto-generated method stub
		
	}

}
