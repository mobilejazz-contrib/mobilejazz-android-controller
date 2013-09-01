package cat.mobilejazz.controller.spinner;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.view.View;
import android.widget.AdapterView;
import cat.mobilejazz.controller.adapter.ICursorAdapter;
import cat.mobilejazz.controller.databinding.SelectableLoaderBinding;

public class SpinnerDataBinding<E> extends SelectableLoaderBinding<ICursorAdapter<E>, Cursor, E> {

	public SpinnerDataBinding(AdapterView<ICursorAdapter<E>> view, ICursorAdapter<E> adapter,
			LoaderFactory<Cursor> loaderFactory, LoaderManager loaderManager) {
		super(view, adapter, loaderFactory, loaderManager);
	}

	public SpinnerDataBinding(AdapterView<ICursorAdapter<E>> view, ICursorAdapter<E> adapter,
			LoaderFactory<Cursor> loaderFactory, LoaderManager loaderManager, OnLoadListener listener) {
		super(view, adapter, loaderFactory, loaderManager, listener);
	}

	public SpinnerDataBinding(View parent, int resId, ICursorAdapter<E> adapter,
			LoaderFactory<Cursor> loaderFactory, LoaderManager loaderManager) {
		super(parent, resId, adapter, loaderFactory, loaderManager);
	}

	public SpinnerDataBinding(View parent, int resId, ICursorAdapter<E> adapter,
			LoaderFactory<Cursor> loaderFactory, LoaderManager loaderManager, OnLoadListener listener) {
		super(parent, resId, adapter, loaderFactory, loaderManager, listener);
	}

	@Override
	protected void updateAdapter(Cursor data) {
		getAdapter().setCursor(data);
	}

	@Override
	protected int indexOf(E entry) {
		return getAdapter().getPosition(entry);
	}

}
