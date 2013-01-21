package cat.mobilejazz.controller.dialogs;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;

public abstract class LayoutModificationDialog<T> extends DialogModificationTask implements LoaderCallbacks<T> {

	private int mLayoutId;
	private T mValue;
	private View mContentView;

	public LayoutModificationDialog(int layoutId, int titleResId, int positiveButtonLabelResId,
			int neutralButtonLabelResId) {
		super(titleResId, positiveButtonLabelResId, neutralButtonLabelResId);
		mLayoutId = layoutId;
	}

	/**
	 * Gets the loaded value.
	 * 
	 * @return
	 */
	public T getValue() {
		return mValue;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			getArguments().putAll(savedInstanceState);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle out) {
		out.putAll(getArguments());
		super.onSaveInstanceState(out);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().restartLoader(0, getArguments(), this);
	}

	@Override
	public void onStart() {
		super.onStart();
		
	}

	protected abstract void onInitalizeContentView(View contentView, T initialValue);

	@Override
	protected View createContentView(Bundle savedInstanceState, LayoutInflater inflater) {
		mContentView = inflater.inflate(mLayoutId, null);
		return mContentView;
	}

	public void onLoadFinished(Loader<T> loader, T value) {
		mValue = value;
		onInitalizeContentView(mContentView, mValue);
	};

	@Override
	public void onLoaderReset(Loader<T> loader) {
		mValue = null;
	}
}
