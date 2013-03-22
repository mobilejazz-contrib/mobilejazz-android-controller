package cat.mobilejazz.controller.adapter;

import java.util.TreeMap;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

/**
 * This class implements an adapter that allows to visually order it's elements
 * into groups or categories. Each category is started by a separate list item
 * which represents the category header, typically showing the name of the
 * category.
 * 
 * @author Hannes Widmoser
 * 
 * @param <Category>
 *            A class containing the essential information of the category. Note
 *            that it need not store all sub elements, as these are stored in
 *            the corresponding cursor.
 */
public abstract class CategorizedAdapter<Category, AdapterType extends Adapter> extends BaseAdapter {

	public static final int VIEW_TYPE_ITEM = 0;
	public static final int VIEW_TYPE_CATEGORY_HEADER = 1;

	private Context mContext;
	private AdapterType mDelegate;
	private TreeMap<Integer, Category> mHeaderIndices;

	private void initialize(AdapterType delegate) {
		mHeaderIndices = new TreeMap<Integer, Category>();
		mHeaderIndices.clear();
		recomputeHeaderIndices(mHeaderIndices, delegate);
	}

	public CategorizedAdapter(Context context, AdapterType delegate) {
		mContext = context;
		mDelegate = delegate;
		mDelegate.registerDataSetObserver(new HeaderUpdater());
		initialize(delegate);
	}

	/**
	 * Subclasses have to implement this method to categorize the cursor. It is
	 * assumed, that cursor items are already sorted such that each category
	 * appears as a consecutive block of items. This method only defines at
	 * which indices those categories begin, and also specifies the essential
	 * data for each category stored in the respective instance of
	 * {@link Category}.
	 * 
	 * @param headerIndices
	 *            An empty map from list positions to category objects that is
	 *            to be filled by this method.
	 * @param cursor
	 *            The new cursor.
	 */
	protected abstract void recomputeHeaderIndices(TreeMap<Integer, Category> headerIndices, AdapterType delegate);

	public int getCursorIndex(int position) {
		return position - mHeaderIndices.headMap(position).size();
	}

	@Override
	public int getCount() {
		return mDelegate.getCount() + mHeaderIndices.size();
	}

	@Override
	public Object getItem(int position) {
		Category header = mHeaderIndices.get(position);
		if (header != null) {
			// this position points to a header:
			return header;
		} else {
			// this position points to a cursor element. Compute the cursor
			// index:

			return mDelegate.getItem(getCursorIndex(position));
		}
	}

	@Override
	public long getItemId(int position) {
		Category header = mHeaderIndices.get(position);
		if (header != null) {
			// this position points to a header:
			return 0;
		} else {
			// this position points to a cursor element. Compute the cursor
			// index:
			return mDelegate.getItemId(getCursorIndex(position));
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		Category header = mHeaderIndices.get(position);
		if (header != null) {
			return VIEW_TYPE_CATEGORY_HEADER;
		} else {
			return VIEW_TYPE_ITEM;
		}
	}

	public abstract View newView(Context context, Category category, ViewGroup parent);

	public abstract void bindView(View view, Context context, Category category);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Category header = mHeaderIndices.get(position);
		if (header != null) {
			View v;
			if (convertView == null) {
				v = newView(mContext, header, parent);
			} else {
				v = convertView;
			}
			bindView(v, mContext, header);
			return v;
		} else {
			return mDelegate.getView(getCursorIndex(position), convertView, parent);
		}
	}

	public AdapterType getDelegate() {
		return mDelegate;
	}

	private class HeaderUpdater extends DataSetObserver {

		@Override
		public void onChanged() {
			super.onChanged();
			mHeaderIndices.clear();
			recomputeHeaderIndices(mHeaderIndices, mDelegate);

			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
			notifyDataSetInvalidated();
		}

	}
}
