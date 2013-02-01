package cat.mobilejazz.controller.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public abstract class ProgressLayoutInputDialog<T> extends LayoutInputDialog<T> {

	private int mProgressBarId;
	private ProgressBar mProgressBar;

	public ProgressLayoutInputDialog(int layoutId, int progressBarId, int titleResId, int positiveButtonLabelResId,
			int neutralButtonLabelResId, T defaultInitialValue) {
		super(layoutId, titleResId, positiveButtonLabelResId, neutralButtonLabelResId, defaultInitialValue);
		mProgressBarId = progressBarId;
		mProgressBar = null;
	}

	protected abstract void enableInputWidget(boolean on);

	@Override
	protected View createContentView(Bundle savedInstanceState, LayoutInflater inflater) {
		View view = super.createContentView(savedInstanceState, inflater);
		mProgressBar = (ProgressBar) view.findViewById(mProgressBarId);
		mProgressBar.setVisibility(View.GONE);
		enableInputWidget(true);
		return view;
	}

	@Override
	protected void onStartTask() {
		super.onStartTask();
		mProgressBar.setVisibility(View.VISIBLE);
		enableInputWidget(false);
	}

	@Override
	protected void onCompleteTask() {
		mProgressBar.setVisibility(View.GONE);
		super.onCompleteTask();
		enableInputWidget(true);
	}

	@Override
	protected void onCancelTask() {
		mProgressBar.setVisibility(View.GONE);
		super.onCancelTask();
		enableInputWidget(true);
	}

}
