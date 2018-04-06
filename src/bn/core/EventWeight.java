package bn.core;

import java.util.*;

public class EventWeight {
	
	public double weight;
	public HashMap<RandomVariable, Object> event;

	public EventWeight(HashMap<RandomVariable, Object> event, double weight) {
		this.event = event;
		this.weight = weight;
	}
}