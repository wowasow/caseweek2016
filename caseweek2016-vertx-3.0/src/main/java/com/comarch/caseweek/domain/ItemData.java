package com.comarch.caseweek.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemData {
	
	private static final AtomicInteger SEQUENCE = new AtomicInteger();

	private final Integer id;
	private String name;
	private String description;
	
	public ItemData() {
		this.id = SEQUENCE.incrementAndGet();
	}
	
	public ItemData(String name, String description) {
		this();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}
			
}
