package cat.mobilejazz.controller.databinding;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class ObservableLoader<D> extends AsyncTaskLoader<D> {

	private OnLoadInitiatedListener<D> listener;

	public ObservableLoader(Context context) {
		super(context);
	}

	public void setOnLoadInitiatedListener(OnLoadInitiatedListener<D> listener) {
		this.listener = listener;
	}

	@Override
	protected void onForceLoad() {
		super.onForceLoad();
		if (listener != null) {
			listener.onLoadInitiated(this);
		}
	}

}
