package cat.mobilejazz.controller.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomCursorAdapter extends CursorAdapter {

	public static interface ViewBinder {

		/**
		 * Modifies the given view, to reflect the data represented by the
		 * cursor.
		 * 
		 * @param view
		 *            The view that should display the data.
		 * @param context
		 *            A {@link Context}
		 * @param inflater
		 *            A {@link LayoutInflater}
		 * @param cursor
		 *            The cursor representing the data that is to be displayed.
		 */
		public void bindView(View view, Context context, LayoutInflater inflater, Cursor cursor);

	}

	private ViewBinder mViewBinder;
	protected LayoutInflater mInflater;

	private int mLayoutResId;

	public CustomCursorAdapter(Context context, Cursor c, int layoutResId, ViewBinder viewBinder) {
		super(context, c, 0);
		mViewBinder = viewBinder;
		mInflater = LayoutInflater.from(context);
		mLayoutResId = layoutResId;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		mViewBinder.bindView(view, context, mInflater, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mInflater.inflate(mLayoutResId, parent, false);
		return view;
	}

}
