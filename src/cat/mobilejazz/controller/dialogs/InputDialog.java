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

	public abstract T readResult();

}
