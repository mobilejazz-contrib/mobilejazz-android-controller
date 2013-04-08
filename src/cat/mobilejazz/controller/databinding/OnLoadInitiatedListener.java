package cat.mobilejazz.controller.databinding;

import android.support.v4.content.Loader;

public interface OnLoadInitiatedListener<D> {

	public void onLoadInitiated(Loader<D> loader);

}
