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

	private static final String KEY_START_TIME_IDS = "cat.mobilejazz.ProfileLoaderCallbacks.startTimeIds";
	private static final String KEY_START_TIME_VALUES = "cat.mobilejazz.ProfileLoaderCallbacks.startTimeValues";

	private static class MyOnLoadInitiatedListener<T> implements OnLoadInitiatedListener<T> {

		private SparseArray<Long> startTimes = new SparseArray<Long>();

		@Override
		public void onLoadInitiated(Loader<T> loader) {
			long startTime = System.currentTimeMillis();
			int id = loader.getId();
			startTimes.put(id, startTime);
		}

		public void initFromSavedInstanceState(Bundle savedInstanceState) {
			if (savedInstanceState != null) {
				int[] ids = savedInstanceState.getIntArray(KEY_START_TIME_IDS);
				long[] values = savedInstanceState.getLongArray(KEY_START_TIME_VALUES);

				for (int i = 0; i < ids.length; ++i) {
					startTimes.put(ids[i], values[i]);
				}
			}
		}

		public void onSaveInstanceState(Bundle out) {
			int[] ids = new int[startTimes.size()];
			long[] values = new long[startTimes.size()];
			for (int i = 0; i < startTimes.size(); ++i) {
				ids[i] = startTimes.keyAt(i);
				values[i] = startTimes.valueAt(i);
			}
			out.putIntArray(KEY_START_TIME_IDS, ids);
			out.putLongArray(KEY_START_TIME_VALUES, values);
		}

	}

	MyOnLoadInitiatedListener<T> onLoadListener = new MyOnLoadInitiatedListener<T>();

	public void initFromSavedInstanceState(Bundle savedInstanceState) {
		onLoadListener.initFromSavedInstanceState(savedInstanceState);
	}

	/**
	 * Subclasses must create and return a loader here. Same semantics as
	 * {@link #onCreateLoader(int, Bundle)}.
	 */
	protected abstract ObservableLoader<T> createLoader(int id, Bundle args);

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
		ObservableLoader<T> loader = createLoader(id, args);
		loader.setOnLoadInitiatedListener(onLoadListener);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<T> loader, T data) {
		Long startTime = onLoadListener.startTimes.get(loader.getId());
		long now = System.currentTimeMillis();
		if (startTime != null) {
			long duration = now - startTime;
			sendTime(duration, loader, data);
		}
	}

	@Override
	public void onLoaderReset(Loader<T> loader) {
	}

	public void onSaveInstanceState(Bundle out) {
		onLoadListener.onSaveInstanceState(out);
	}

}
