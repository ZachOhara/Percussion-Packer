package io.github.zachohara.percussionpacker.slide;

public interface IncrementalProgressListener<T> {
	
	public void finishIncrementalChange(T change);

}
