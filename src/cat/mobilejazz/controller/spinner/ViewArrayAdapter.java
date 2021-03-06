package cat.mobilejazz.controller.spinner;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * A dynamic array adapter that allows to customize the dispalying of items.
 * 
 * @author Hannes Widmoser
 * 
 * @param <T>
 */
public abstract class ViewArrayAdapter<T> extends ArrayAdapter<T> {

	private boolean mHasOwnLayout;

	public ViewArrayAdapter(Context context, int layoutResId, int textViewResId, T... initialObjects) {
		super(context, layoutResId, textViewResId, initialObjects);
		mHasOwnLayout = true;
	}

	public ViewArrayAdapter(Context context, int layoutResId, int textViewResId, List<T> initialObjects) {
		super(context, layoutResId, textViewResId, initialObjects);
		mHasOwnLayout = true;
	}

	public ViewArrayAdapter(Context context, int textViewResId, T... initialObjects) {
		super(context, textViewResId, initialObjects);
		mHasOwnLayout = false;
	}

	public ViewArrayAdapter(Context context, int textViewResId, List<T> initialObjects) {
		super(context, textViewResId, initialObjects);
		mHasOwnLayout = false;
	}

	protected boolean hasOwnLayout() {
		return mHasOwnLayout;
	}

	@Override
	public void addAll(Collection<? extends T> collection) {
		for (T t : collection) {
			add(t);
		}
	}

	@Override
	public void addAll(T... items) {
		for (T t : items) {
			add(t);
		}
	};

	protected abstract void bindView(Context context, View view, T object, int position, boolean dropDown);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		bindView(getContext(), view, getItem(position), position, false);
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = super.getDropDownView(position, convertView, parent);
		bindView(getContext(), view, getItem(position), position, true);
		return view;
	}

}
