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
public class ObjectCursorAdapter<T> extends CursorAdapter {

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
		public void bindView(View view, Context context, LayoutInflater inflater, T object, int position);

	}

	protected int mLayoutResId;
	protected LayoutInflater mInflater;

	protected ObjectBinder<T> mObjectBinder;
	protected ViewBinder<T> mViewBinder;

	private T objectHolder;

	public ObjectCursorAdapter(Context context, int layoutResId, ViewBinder<T> viewBinder, ObjectBinder<T> objectBinder) {
		super(context, null, 0);
		mLayoutResId = layoutResId;
		mInflater = LayoutInflater.from(context);

		mViewBinder = viewBinder;
		mObjectBinder = objectBinder;
	}

	private T fromCursor(Cursor c) {
		if (objectHolder == null) {
			objectHolder = mObjectBinder.newInstance();
		}
		mObjectBinder.bindInstance(objectHolder, c);
		return objectHolder;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		mViewBinder.bindView(view, context, mInflater, fromCursor(cursor), cursor.getPosition());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(mLayoutResId, parent, false);
	}

	@Override
	public T getItem(int position) {
		T instance = mObjectBinder.newInstance();
		mObjectBinder.bindInstance(instance, (Cursor) super.getItem(position));
		return instance;
	}

}
