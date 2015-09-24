package gameObjects.objects;

import javafx.geometry.BoundingBox;
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

	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}		

}
