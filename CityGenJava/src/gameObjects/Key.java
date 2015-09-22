package gameObjects;

public class Key extends Tool {

	
	public Key(String name, String desc) {
		super(name, desc, new KeyInteractStrategy());		
	}

	@Override
	public void interact(Item i) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void equip() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canUse() {
		// TODO Auto-generated method stub
		return false;
	}
		
	@Override
	public String getName() {
		return getName();
	}

	@Override
	public String getDescription() {
		return getDescription();
	}

}
