package weapons;

import main.Handler;

public class GasGrenades extends Gun{

	//created so it can be in the box...
	public GasGrenades(Handler handler) {
		super(handler, 0, 0, 0, 0, 0, 0, 0);
		name = "Gas Grenades";
		originalName = name;
	}

}
