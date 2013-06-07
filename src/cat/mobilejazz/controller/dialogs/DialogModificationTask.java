package cat.mobilejazz.controller.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import cat.mobilejazz.utilities.concurrency.AsyncDialogFragment;

public abstract class DialogModificationTask extends AsyncDialogFragment {

	protected OnClickListener mOnPositiveClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				if (onStartTask()) {
					performTask();
				}
			} catch (Exception e) {
				// TODO: show error message:
				abort();
			}
		}
	};

	protected OnClickListener mOnNegativeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			abort();
		}
	};

	public static interface OnTaskFinishedListener {

		public void onFinish(DialogModificationTask dialog);

	}

	private int mTitleResId;
	private int mPositiveButtonLabelResId;
	private int mNeutralButtonLabelResId;

	private OnTaskFinishedListener mListener;

	public DialogModificationTask(int titleResId, int positiveButtonLabelResId, int neutralButtonLabelResId) {
		mTitleResId = titleResId;
		mPositiveButtonLabelResId = positiveButtonLabelResId;
		mNeutralButtonLabelResId = neutralButtonLabelResId;
	}

	public void setOnTaskFinishListener(OnTaskFinishedListener listener) {
		mListener = listener;
	}

	protected abstract View createContentView(Bundle savedInstanceState, LayoutInflater inflater);

	protected void performTask() {
		getTask().execute();
	}

	protected boolean onStartTask() {
		return true;
	}

	protected void onCancelTask() {
	}

	protected void onCompleteTask() {
		if (mListener != null) {
			mListener.onFinish(this);
		}
	}

	@Override
	protected void finish() {
		super.finish();
		onCompleteTask();
	}

	public void abort() {
		getDialog().cancel();
		dismiss();
		onCancelTask();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle(mTitleResId)
				.setView(createContentView(savedInstanceState, getActivity().getLayoutInflater()))
				.setPositiveButton(mPositiveButtonLabelResId, null).setNeutralButton(mNeutralButtonLabelResId, null)
				.create();
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		AlertDialog dialog = (AlertDialog) getDialog();
		dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(mOnPositiveClick);
		dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(mOnNegativeClick);
	}

}
