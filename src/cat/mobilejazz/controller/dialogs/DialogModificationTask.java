package cat.mobilejazz.controller.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import cat.mobilejazz.controller.observer.ObservableAsyncQueryHandler;
import cat.mobilejazz.controller.observer.ObservableAsyncQueryHandler.OnCompleteHandler;

public abstract class DialogModificationTask extends DialogFragment implements OnCompleteHandler {

	protected class ButtonListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					onStartTask();
					performTask();
				} catch (Exception e) {
					// TODO: show error message:
					abort();
				}
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				abort();
				break;
			default:
				break;
			}
		}

	}
	
	public static interface OnTaskFinishedListener {
		
		public void onFinish(DialogModificationTask dialog);
		
	}

	private ObservableAsyncQueryHandler mQueryHandler;

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
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQueryHandler = new ObservableAsyncQueryHandler(getActivity(), this);
	}

	protected abstract View createContentView(Bundle savedInstanceState, LayoutInflater inflater);

	protected abstract void performTask() throws Exception;

	protected void onStartTask() {
	}

	protected void onCancelTask() {
	}

	protected void onCompleteTask() {
		if (mListener != null) {
			mListener.onFinish(this);
		}
	}

	public void finish() {
		//dismiss();
		onCompleteTask();
	}
	
	public void abort() {
		//dismiss();
		onCancelTask();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ButtonListener listener = new ButtonListener();
		AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle(mTitleResId)
				.setView(createContentView(savedInstanceState, getActivity().getLayoutInflater()))
				.setPositiveButton(mPositiveButtonLabelResId, listener)
				.setNeutralButton(mNeutralButtonLabelResId, listener).create();
		return dialog;
	}

	@Override
	public void onQueryComplete(int token, Object cookie, Cursor result) {
		finish();
	}

	@Override
	public void onUpdateComplete(int token, Object cookie, int result) {
		finish();
	}

	@Override
	public void onDeleteComplete(int token, Object cookie, int result) {
		finish();
	}

	@Override
	public void onInsertComplete(int token, Object cookie, Uri result) {
		finish();
	}
	
	public ObservableAsyncQueryHandler getQueryHandler() {
		return mQueryHandler;
	}

}
