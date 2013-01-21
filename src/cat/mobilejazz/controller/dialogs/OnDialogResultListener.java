package cat.mobilejazz.controller.dialogs;

public interface OnDialogResultListener<T> {
	
	public void onResult(T result);
	
	public void onCancel();

}
