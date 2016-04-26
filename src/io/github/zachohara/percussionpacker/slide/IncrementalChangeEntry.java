package io.github.zachohara.percussionpacker.slide;

public interface IncrementalChangeEntry<T> extends Runnable {
	
	public T getChangedObject();
	
	public IncrementalProgressListener<T> getNotifyableParent();
	
	public void doIncrementalChange();
	
	public boolean isFinished();

}
