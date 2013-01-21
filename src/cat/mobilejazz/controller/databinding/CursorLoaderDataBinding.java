package cat.mobilejazz.controller.databinding;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

public class CursorLoaderDataBinding extends LoaderDataBinding<CursorAdapter, Cursor> {

	public CursorLoaderDataBinding(AdapterView<CursorAdapter> view, CursorAdapter adapter,
			LoaderFactory<Cursor> loaderFactory, LoaderManager loaderManager) {
		super(view, adapter, loaderFactory, loaderManager);
	}

	public CursorLoaderDataBinding(View parent, int resId, CursorAdapter adapter, LoaderFactory<Cursor> loaderFactory,
			LoaderManager loaderManager) {
		super(parent, resId, adapter, loaderFactory, loaderManager);
	}

	/**
	 * Sets the given {@link Cursor} on this binding's adapter.
	 */
	@Override
	protected void updateAdapter(Cursor data) {
		getAdapter().swapCursor(data);
	}

}
