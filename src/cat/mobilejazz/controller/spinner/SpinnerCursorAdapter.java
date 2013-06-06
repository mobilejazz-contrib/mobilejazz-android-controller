package cat.mobilejazz.controller.spinner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cat.mobilejazz.utilities.CompatibilityUtils;

public abstract class SpinnerCursorAdapter<T> extends ViewArrayAdapter<T> {

	public class MyDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			clear();
			for (T staticObject : mStaticObjects) {
				add(staticObject);
			}
			if (mCursor != null && !mCursor.isClosed()) {
				mCursor.moveToFirst();
				while (!mCursor.isAfterLast()) {
					add(getObject(mCursor));
					mCursor.moveToNext();
				}
			}
			notifyDataSetChanged();
		}

	}

	private Cursor mCursor;
	private DataSetObserver mDataSetObserver;
	private boolean mShowProgress;

	private T[] mStaticObjects;

	private static <T> List<T> modifiableList(T... staticObjects) {
		List<T> result = new ArrayList<T>();
		for (T obj : staticObjects) {
			result.add(obj);
		}
		return result;
	}

	protected abstract T getObject(Cursor cursor);

	private void init(T... staticObjects) {
		setNotifyOnChange(false);
		mDataSetObserver = new MyDataSetObserver();
		mStaticObjects = staticObjects;
	}

	public SpinnerCursorAdapter(Context context, int layoutResId, int textViewResId, boolean showProgress,
			T... staticObjects) {
		super(context, layoutResId, textViewResId, modifiableList(staticObjects));
		init(staticObjects);
		mShowProgress = showProgress;
	}

	public SpinnerCursorAdapter(Context context, int textViewResId, boolean showProgress, T... staticObjects) {
		super(context, textViewResId, modifiableList(staticObjects));
		init(staticObjects);
		mShowProgress = showProgress;
	}

	public void setCursor(Cursor cursor) {
		mCursor = cursor;
		mDataSetObserver.onChanged();
	}

	public boolean hasCursor() {
		return mCursor != null;
	}

	protected boolean loadInProgress() {
		return !hasCursor();
	}

	protected View getProgressView(View convertView, ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		} else {
			ProgressBar progress = new ProgressBar(parent.getContext());
			progress.setIndeterminate(true);
			int d = CompatibilityUtils.getPixelsInt(parent.getContext(), 48);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(d, d);
			progress.setLayoutParams(lp);
			int p = CompatibilityUtils.getPixelsInt(parent.getContext(), 8);
			progress.setPadding(p, p, p, p);
			return progress;
		}
	}

	protected View getNoElementsView(View convertView, ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		} else {
			TextView textView = new TextView(parent.getContext());
			textView.setText("No element available");
			return textView;
		}
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (loadInProgress() && mShowProgress && position == mStaticObjects.length) {
			return getProgressView(convertView, parent);
		} else {
			if (convertView != null && convertView instanceof ProgressBar) {
				convertView = null;
			}
			return super.getDropDownView(position, convertView, parent);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (loadInProgress() && mShowProgress && position == mStaticObjects.length) {
			return getProgressView(convertView, parent);
		} else {
			if (convertView != null && convertView instanceof ProgressBar) {
				convertView = null;
			}
			return super.getView(position, convertView, parent);
		}
	}

	@Override
	public T getItem(int position) {
		if (loadInProgress() && mShowProgress) {
			if (position == mStaticObjects.length) {
				return null;
			} else {
				return super.getItem(position);
			}
		} else {
			return super.getItem(position);
		}
	}

	@Override
	public int getCount() {
		if (loadInProgress() && mShowProgress) {
			return super.getCount() + 1;
		} else {
			return super.getCount();
		}
	}

	@Override
	protected void bindView(Context context, View view, T object, int position, boolean dropDown) {
		// default: do nothing
	}

	@Override
	public boolean isEnabled(int position) {
		return areAllItemsEnabled() || (position != mStaticObjects.length);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return !(loadInProgress() && mShowProgress);
	}

}
