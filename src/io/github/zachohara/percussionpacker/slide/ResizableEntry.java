package io.github.zachohara.percussionpacker.slide;

import javafx.scene.layout.Region;

public class ResizableEntry implements IncrementalChangeEntry<Region> {
	
	private IncrementalProgressListener<Region> notifyableParent;
	private Region resizingRegion;
	
	private TimeChangeableQuantity widthQuantity;
	private TimeChangeableQuantity heightQuantity;
	
	public ResizableEntry(IncrementalProgressListener<Region> parent, Region resizingRegion, double newWidth, double newHeight, long duration) {
		this.notifyableParent = parent;
		this.resizingRegion = resizingRegion;
		this.widthQuantity = new TimeChangeableQuantity(resizingRegion.getWidth(),
				newWidth, duration);
		this.heightQuantity = new TimeChangeableQuantity(resizingRegion.getHeight(),
				newHeight, duration);
	}
	
	public Region getChangedObject() {
		return this.resizingRegion;
	}

	@Override
	public IncrementalProgressListener<Region> getNotifyableParent() {
		return this.notifyableParent;
	}

	@Override
	public void run() {
		this.doIncrementalChange();
	}
	
	public void doIncrementalChange() {
		this.resizingRegion.setPrefWidth(this.widthQuantity.getCurrentValue());
		this.resizingRegion.setPrefHeight(this.heightQuantity.getCurrentValue());
	}
	
	public boolean isFinished() {
		return this.widthQuantity.isFinished() && this.heightQuantity.isFinished();
	}

}
