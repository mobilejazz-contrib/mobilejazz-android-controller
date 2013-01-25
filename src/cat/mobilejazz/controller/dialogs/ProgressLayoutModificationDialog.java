package cat.mobilejazz.controller.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public abstract class ProgressLayoutModificationDialog extends LayoutModificationDialog {

	private int mProgressBarId;
	private ProgressBar mProgressBar;

	public ProgressLayoutModificationDialog(int layoutId, int progressBarId, int titleResId,
			int positiveButtonLabelResId, int neutralButtonLabelResId) {
		super(layoutId, titleResId, positiveButtonLabelResId, neutralButtonLabelResId);
		mProgressBarId = progressBarId;
		mProgressBar = null;
	}

	@Override
	protected View createContentView(Bundle savedInstanceState, LayoutInflater inflater) {
		View view = super.createContentView(savedInstanceState, inflater);
		mProgressBar = (ProgressBar) view.findViewById(mProgressBarId);
		mProgressBar.setVisibility(View.GONE);
		return view;
	}

	@Override
	protected void onStartTask() {
		super.onStartTask();
		mProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onCompleteTask() {
		mProgressBar.setVisibility(View.GONE);
		super.onCompleteTask();
	}

	@Override
	protected void onCancelTask() {
		mProgressBar.setVisibility(View.GONE);
		super.onCancelTask();
	}

}
