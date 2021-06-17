package com.boot.enums;

public enum ContactType {
		EMAIL("EMAIL"),
		MOBILE("MOBILE");
		public final String name;
	
		ContactType(String name) {
			this.name=name;
		}
	
}
