package io.github.zachohara.percussionpacker.slide;

public class TimeChangeableQuantity {
	
	private final long startTime;
	private long duration;
	
	private double startValue;
	private double endValue;
	private double increment;
	
	public TimeChangeableQuantity(double startValue, double endValue, long duration) {
		this.startTime = System.currentTimeMillis();
		this.duration = duration;
		
		this.startValue = startValue;
		this.endValue = endValue;
		
		double difference = this.endValue - this.startValue;
		
		this.increment = difference / this.duration;
	}
	
	public double getCurrentValue() {
		double currentValue = this.startValue + this.getProgress();
		if (this.increment > 0) {
			currentValue = Math.min(currentValue, this.endValue);
		} else if (this.increment < 0) {
			currentValue = Math.max(currentValue, this.endValue);
		}
		return currentValue;
	}
	
	public boolean isFinished() {
		return this.getElapsedTime() >= this.duration;
	}
	
	private double getProgress() {
		return this.increment * this.getElapsedTime();
	}
	
	private long getElapsedTime() {
		return System.currentTimeMillis() - this.startTime;
	}

}
