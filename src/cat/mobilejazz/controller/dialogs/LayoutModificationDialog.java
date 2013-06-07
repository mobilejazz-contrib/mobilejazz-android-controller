package cat.mobilejazz.controller.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public abstract class LayoutModificationDialog extends DialogModificationTask {

	private int mLayoutId;
	private View mContentView;

	public LayoutModificationDialog(int layoutId, int titleResId, int positiveButtonLabelResId,
			int neutralButtonLabelResId) {
		super(titleResId, positiveButtonLabelResId, neutralButtonLabelResId);
		mLayoutId = layoutId;
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
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	protected abstract void onInitalizeContentView(View contentView);

	@Override
	protected View createContentView(Bundle savedInstanceState, LayoutInflater inflater) {
		mContentView = inflater.inflate(mLayoutId, null);
		onInitalizeContentView(mContentView);
		return mContentView;
	}

}
