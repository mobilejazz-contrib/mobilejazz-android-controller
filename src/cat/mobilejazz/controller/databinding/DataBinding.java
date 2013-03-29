package cat.mobilejazz.controller.databinding;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * This class represents a binding between an adapter and a corresponding
 * adapter view in its most general form. It allows to bind and unbind the
 * adapter as well as select an item by providing an index.
 * 
 * @author Hannes Widmoser
 * 
 * @param <T>
 *            The adapter type.
 */
public class DataBinding<T extends Adapter> {

	private AdapterView<T> view;
	private T adapter;

	/**
	 * Creates a new data binding by providing the resource id of the adapter
	 * view.
	 * 
	 * @param parent
	 *            A parent view that contains the adapter view.
	 * @param resId
	 *            The resource identifier of the adapter view.
	 * @param adapter
	 *            The adapter that provides the data.
	 */
	@SuppressWarnings("unchecked")
	public DataBinding(View parent, int resId, T adapter) {
		this((AdapterView<T>) parent.findViewById(resId), adapter);
	}

	/**
	 * Creates a data binding by providing the adapter view directly.
	 * 
	 * @param view
	 *            The adapter view that should display the data.
	 * @param adapter
	 *            The adapter that provides the data.
	 */
	public DataBinding(AdapterView<T> view, T adapter) {
		if (view == null) {
			throw new IllegalArgumentException("AdapterView may not be null.");
		}
		this.view = view;
		this.adapter = adapter;
	}

	public AdapterView<T> getView() {
		return view;
	}

	public T getAdapter() {
		return adapter;
	}

	/**
	 * Binds the adapter to the view. After this method has been invoked, the
	 * view is assumed to display the data of the adapter.
	 */
	public void bind() {
		view.setAdapter(adapter);
	}

	/**
	 * Unbinds the adapter from the view.
	 */
	public void unbind() {
		view.setAdapter(null);
	}

	/**
	 * Sets the selection of the view. See {@link AdapterView#setSelection(int)}
	 * .
	 * 
	 * @param position
	 *            The index in the adapter.
	 */
	public void setSelection(int position) {
		view.setSelection(position);
	}

}
