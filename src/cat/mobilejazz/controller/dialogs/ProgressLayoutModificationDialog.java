package cat.mobilejazz.controller.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public abstract class ProgressLayoutModificationDialog extends LayoutModificationDialog {

	private static final String ARG_PROGRESS_VISIBLE = "cat.mobilejazz.controller.dialogs.ProgressLayoutModificationDialog.ARG_PROGRESS_VISIBLE";

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
		if (savedInstanceState != null) {
			mProgressBar.setVisibility(savedInstanceState.getInt(ARG_PROGRESS_VISIBLE));
		} else {
			mProgressBar.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mProgressBar.getVisibility() == View.VISIBLE) {
			getDialog().setCancelable(false);
			((AlertDialog) getDialog()).getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);
		}
	}

	@Override
	protected boolean onStartTask() {
		super.onStartTask();
		mProgressBar.setVisibility(View.VISIBLE);
		getDialog().setCancelable(false);
		((AlertDialog) getDialog()).getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);
		return true;
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

	@Override
	public void onSaveInstanceState(Bundle out) {
		super.onSaveInstanceState(out);
		out.putInt(ARG_PROGRESS_VISIBLE, mProgressBar.getVisibility());
	}

}
