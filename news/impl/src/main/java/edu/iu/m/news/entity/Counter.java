package edu.iu.m.news.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	private AtomicInteger count;
	
	public Counter() {
		this.count = new AtomicInteger(0);
	}
	
	public int getNext() {
		return count.incrementAndGet();
	}
	
	public int getCount() {
		return count.get();
	}

}
