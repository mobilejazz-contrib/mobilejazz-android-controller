package cat.mobilejazz.controller.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import cat.mobilejazz.controller.lifecycle.ActivityStateHelper;

public abstract class LayoutInputDialog<T> extends InputDialog<T> {

	private static final String ARG_INITIAL_VALUE = "mInitialValue";

	private int mLayoutId;
	private T mInitialValue;

	public LayoutInputDialog(int layoutId, int titleResId, int positiveButtonLabelResId, int neutralButtonLabelResId,
			T initialValue) {
		super(titleResId, positiveButtonLabelResId, neutralButtonLabelResId);
		mLayoutId = layoutId;
		mInitialValue = initialValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mInitialValue = (T)savedInstanceState.get(ARG_INITIAL_VALUE);
			getArguments().putAll(savedInstanceState);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle out) {
		ActivityStateHelper.writeObject(out, ARG_INITIAL_VALUE, mInitialValue);
		out.putAll(getArguments());
		super.onSaveInstanceState(out);
	}

	protected abstract void onInitalizeContentView(View contentView, T initialValue);

	@Override
	protected View createContentView(Bundle savedInstanceState, LayoutInflater inflater) {
		View view = inflater.inflate(mLayoutId, null);
		onInitalizeContentView(view, mInitialValue);
		return view;
	}

}
