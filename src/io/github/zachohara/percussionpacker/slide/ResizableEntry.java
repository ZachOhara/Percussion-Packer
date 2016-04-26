package io.github.zachohara.percussionpacker.slide;

import javafx.scene.layout.Region;

public class ResizableEntry implements Runnable {
	
	private Region resizingRegion;
	
	private TimeChangeableQuantity widthQuantity;
	private TimeChangeableQuantity heightQuantity;
	
	public ResizableEntry(Region resizingRegion, double newWidth, double newHeight, long duration) {
		this.resizingRegion = resizingRegion;
		this.widthQuantity = new TimeChangeableQuantity(resizingRegion.getWidth(),
				newWidth, duration);
		this.heightQuantity = new TimeChangeableQuantity(resizingRegion.getHeight(),
				newHeight, duration);
	}
	
	public Region getRegion() {
		return this.resizingRegion;
	}

	@Override
	public void run() {
		this.doIncrementalChange();
	}
	
	public void doIncrementalChange() {
		this.resizingRegion.setPrefWidth(this.widthQuantity.getCurrentValue());
		this.resizingRegion.setPrefHeight(this.heightQuantity.getCurrentValue());
	}

}
