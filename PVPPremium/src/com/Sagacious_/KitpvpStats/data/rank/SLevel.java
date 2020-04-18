package com.Sagacious_.KitpvpStats.data.rank;

import java.util.List;

public class SLevel {
	private String id;
	
	private String name;
	private int xp_needed;
	private List<String> cmd;
	
	public SLevel(String id, String name, int xp_needed, List<String> cmd) {
		this.name = name;
		this.xp_needed = xp_needed;
		this.cmd = cmd;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getXPNeeded() {
		return xp_needed;
	}
	
	public List<String> getCMD(){
		return cmd;
	}

}
