package cat.mobilejazz.controller.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A cursor adapter that wraps objects around cursor entries to allow for
 * transparent and more readable access to the cursor data. Note that this may
 * cause a lot of object/creation and destruction and you may only use this if
 * it is fast enough.
 * 
 * @author Hannes Widmoser
 * 
 */
public abstract class ObjectCursorAdapter<T extends CursorBindable> extends CursorAdapter {

	public static interface ViewBinder<T> {

		/**
		 * Fill the given view with contents defined by the given object. Note
		 * that the reference to the object is only temporarily valid. In
		 * particular, it is discouraged to hold references to this object whose
		 * scope exceeds this method.
		 * 
		 * @param view
		 *            The view to be filled.
		 * @param context
		 *            A context.
		 * @param object
		 *            The object defining the data that is to be bound for the
		 *            view.
		 */
		public void bindView(View view, Context context, LayoutInflater inflater, T object);

	}

	protected int mLayoutResId;
	protected LayoutInflater mInflater;

	protected ViewBinder<T> mViewBinder;

	private T objectHolder;

	public ObjectCursorAdapter(Context context, int layoutResId, ViewBinder<T> viewBinder) {
		super(context, null, 0);
		mLayoutResId = layoutResId;
		mInflater = LayoutInflater.from(context);

		mViewBinder = viewBinder;
	}

	/**
	 * Creates a new empty instance of the class <T>.
	 * 
	 * @return An new object instance.
	 */
	protected abstract T newInstance();

	/**
	 * Binds an already existing instance of an object to a new cursor entry.
	 * This is used for performance optimization, but the result should be the
	 * same as in {@link #newInstance(Cursor)}. More formally for any cursor c
	 * the following must always be true:
	 * {@code newInstance(c).equals(bindInstance(i, c))} for any instance
	 * {@code i}.
	 * 
	 * @param instance
	 *            An instance to reuse.
	 * @param c
	 *            The cursor.
	 * @return A changed object instance reflecting the contents of the cursor.
	 */
	protected T bindInstance(T instance, Cursor c) {
		instance.setValues(c);
		return instance;
	}

	private T fromCursor(Cursor c) {
		if (objectHolder == null) {
			objectHolder = newInstance();
		}
		bindInstance(objectHolder, c);
		return objectHolder;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		mViewBinder.bindView(view, context, mInflater, fromCursor(cursor));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(mLayoutResId, parent, false);
	}

	@Override
	public T getItem(int position) {
		T instance = newInstance();
		bindInstance(instance, (Cursor) super.getItem(position));
		return instance;
	}

}
