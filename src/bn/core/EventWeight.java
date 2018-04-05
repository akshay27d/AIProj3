package bn.core;


public class EventWeight {
	
	public double weight;
	public Object[] event;

	public EventWeight(Object[] event, double weight) {
		this.event = event;
		this.weight = weight;
	}
}