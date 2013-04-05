package cat.mobilejazz.controller.databinding;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.SparseArray;

/**
 * Realises a loader callbacks that measures the time it takes to perform
 * operations. This can be used to profile the application's performance.
 * 
 * @author Hannes Widmoser
 * 
 * @param <T>
 */
public abstract class ProfiledLoaderCallbacks<T> implements LoaderCallbacks<T> {

	public static final int FINISHED = 0;
	public static final int RESET = 1;

	private SparseArray<Long> startTimes = new SparseArray<Long>();

	/**
	 * Subclasses must create and return a loader here. Same semantics as
	 * {@link #onCreateLoader(int, Bundle)}.
	 */
	protected abstract Loader<T> createLoader(int id, Bundle args);

	/**
	 * Process the time it took to perform the given operation. The operation is
	 * identified by the respective loader.
	 * 
	 * @param duration
	 *            The time it took to perform the operation. Given in
	 *            milliseconds.
	 * @param loader
	 *            The loader that performed the operation.
	 * @param data
	 *            The data that has been retrieved. May be {@link null} to
	 *            indicate a reset.
	 */
	protected abstract void sendTime(long duration, Loader<T> loader, T data);

	@Override
	public Loader<T> onCreateLoader(int id, Bundle args) {
		startTimes.put(id, System.currentTimeMillis());
		return createLoader(id, args);
	}

	@Override
	public void onLoadFinished(Loader<T> loader, T data) {
		long duration = System.currentTimeMillis() - startTimes.get(loader.getId());
		sendTime(duration, loader, data);
	}

	@Override
	public void onLoaderReset(Loader<T> loader) {
		long duration = System.currentTimeMillis() - startTimes.get(loader.getId());
		sendTime(duration, loader, null);
	}

}
