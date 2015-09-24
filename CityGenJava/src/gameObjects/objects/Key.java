package gameObjects.objects;

import gameObjects.strategies.KeyInteractStrategy;

public class Key extends Tool {

	
	public Key(String name, String desc) {		
		super(name, desc);		
		super.setStrategy(new KeyInteractStrategy(this));
	}
	
	@Override
	public void equip() {
		// TODO Auto-generated method stub
		
	}		

}
