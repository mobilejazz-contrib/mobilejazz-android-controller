package cat.mobilejazz.controller.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public abstract class ProgressLayoutInputDialog<T> extends LayoutInputDialog<T> {

	private static final String ARG_PROGRESS_VISIBLE = "cat.mobilejazz.controller.dialogs.ProgressLayoutInputDialog.ARG_PROGRESS_VISIBLE";

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
		if (savedInstanceState != null) {
			mProgressBar.setVisibility(savedInstanceState.getInt(ARG_PROGRESS_VISIBLE));
			enableInputWidget(mProgressBar.getVisibility() != View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
			enableInputWidget(true);
		}

		return view;
	}

	private void disableUI() {
		getDialog().setCancelable(false);
		enableInputWidget(false);
		((AlertDialog) getDialog()).getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mProgressBar.getVisibility() == View.VISIBLE) {
			disableUI();
		}
	}

	@Override
	protected boolean onStartTask() {
		super.onStartTask();
		mProgressBar.setVisibility(View.VISIBLE);
		disableUI();
		return true;
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

	@Override
	public void onSaveInstanceState(Bundle out) {
		super.onSaveInstanceState(out);
		out.putInt(ARG_PROGRESS_VISIBLE, mProgressBar.getVisibility());
	}

}
