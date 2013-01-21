package cat.mobilejazz.controller.dialogs;


/**
 * This class can be used as a base class for all dialogs that are used to let
 * the user input some data.
 * 
 * @author hannes
 * 
 */
public abstract class InputDialog<T> extends DialogModificationTask {

	

	public InputDialog(int titleResId, int positiveButtonLabelResId, int neutralButtonLabelResId) {
		super(titleResId, positiveButtonLabelResId, neutralButtonLabelResId);
	}
	
	protected abstract T readResult();
	
	protected abstract void performTask(T input);

	@Override
	protected void performTask() throws Exception {
		performTask(readResult());
	}
}
