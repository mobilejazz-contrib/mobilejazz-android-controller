package cat.mobilejazz.controller.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class Dialogs {

	public static void showErrorDialogAndFinish(final Activity context, int titleResId, int messageResId, int buttonLabelResId) {
		new AlertDialog.Builder(context).setMessage(messageResId).setTitle(titleResId)
				.setPositiveButton(buttonLabelResId, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						context.finish();
					}
				}).create().show();
	}
	
	public static void showErrorDialog(Context context, int titleResId, int messageResId, int buttonLabelResId) {
		new AlertDialog.Builder(context).setMessage(messageResId).setTitle(titleResId)
				.setPositiveButton(buttonLabelResId, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
	}
	
	public static void showErrorDialog(Context context, int titleResId, String message, int buttonLabelResId) {
		new AlertDialog.Builder(context).setMessage(message).setTitle(titleResId)
				.setPositiveButton(buttonLabelResId, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
	}

}
