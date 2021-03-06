package cat.mobilejazz.controller.databinding;

import android.content.Context;

public interface LoaderParent {

	public boolean isEnabled();

	public void addOnEnabledChangedListener(OnEnabledChangedListener listener);

	public void removeOnEnabledChangedListener(OnEnabledChangedListener listener);

	public Context getContext();

}
