package ru.bukvica.events;

public class BasicEvent implements Event {

	private String type;
	@Override
    public String getType() { return type; }
	public void setType(String type) { 
		this.type = type; 
	}
	
	protected Object source;
	@Override public Object getSource() {
		return source;
	}
	@Override public void setSource(Object source) {
		this.source = source;
	}
	
	public BasicEvent(String type) {
		this.type = type;
	}

}
