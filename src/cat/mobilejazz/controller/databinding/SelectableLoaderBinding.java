package cat.mobilejazz.controller.databinding;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import cat.mobilejazz.utilities.ObjectUtils;
import cat.mobilejazz.utilities.debug.Debug;

public abstract class SelectableLoaderBinding<T extends Adapter, D, E> extends LoaderDataBinding<T, D> {

	public interface OnLoadListener {

		public void onLoad(Adapter adapter);

	}

	private E entryCache;
	private E initialSelection;

	private OnLoadListener listener;

	public SelectableLoaderBinding(AdapterView<T> view, T adapter, LoaderFactory<D> loaderFactory,
			LoaderManager loaderManager) {
		this(view, adapter, loaderFactory, loaderManager, null);
	}

	public SelectableLoaderBinding(AdapterView<T> view, T adapter, LoaderFactory<D> loaderFactory,
			LoaderManager loaderManager, OnLoadListener listener) {
		super(view, adapter, loaderFactory, loaderManager);
		this.listener = listener;
	}

	public SelectableLoaderBinding(View parent, int resId, T adapter, LoaderFactory<D> loaderFactory,
			LoaderManager loaderManager) {
		this(parent, resId, adapter, loaderFactory, loaderManager, null);
	}

	public SelectableLoaderBinding(View parent, int resId, T adapter, LoaderFactory<D> loaderFactory,
			LoaderManager loaderManager, OnLoadListener listener) {
		super(parent, resId, adapter, loaderFactory, loaderManager);
		this.listener = listener;
	}

	@Override
	public void bind() {
		super.bind();
		if (getAdapter().getCount() > 0) {
			setSelection(0);
		}
	}

	/**
	 * This method sets the selection. It is used after data is known to be
	 * available (e.g. in {@link #onLoadFinished(Loader, Object)}. It sets the
	 * selection, but if the desired item is not part of the data, it just
	 * selects the first item.
	 * 
	 * @param entry
	 *            The entry to be selected.
	 */
	private void setSelectionForce(E entry) {
		int index = indexOf(entry);
		if (index < 0) {
			index = 0;
		}
		setSelection(index);
		entryCache = null;
	}

	/**
	 * Selects the entry in the view that represents the given element. If the
	 * entry is not yet present, it is assumed that it will be available after
	 * the next data update. In this case this call has no immediate effect, but
	 * the binder will try to select the entry after the next data update.
	 * 
	 * @param entry
	 *            The entry to be selected.
	 */
	public void setSelection(E entry) {
		int index = indexOf(entry);
		if (index >= 0) {
			// the element we are looking for is in the spinner
			setSelection(index);
		} else {
			// the element is supposed to be still loading
			entryCache = entry;
		}
	}

	@SuppressWarnings("unchecked")
	public E getSelection() {
		if (entryCache != null) {
			return entryCache;
		} else {
			return (E) getView().getSelectedItem();
		}
	}

	public boolean selectionChanged() {
		return (!ObjectUtils.equals(getSelection(), initialSelection));
	}

	protected abstract int indexOf(E entry);

	@Override
	public void onLoadFinished(Loader<D> loader, D data) {
		E selection = getSelection();
		super.onLoadFinished(loader, data);
		setSelectionForce(selection);
		
		Debug.debug(String.valueOf(getSelection()));
		
		if (initialSelection == null) {
			initialSelection = getSelection();
		}
		entryCache = null;
		if (listener != null) {
			listener.onLoad(getAdapter());
		}
	};

}
