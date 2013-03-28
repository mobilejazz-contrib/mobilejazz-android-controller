package cat.mobilejazz.controller.adapter;

import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Adapter;

/**
 * Base class for all adapters that want to display a categorized list of items
 * where items are grouped based on the result of their respective
 * {@link #getItemCategoryId(int)}.
 * 
 * @author Hannes Widmoser
 * 
 */
public abstract class ItemIdCategorizedAdapter<Category, AdapterType extends Adapter> extends
		CategorizedAdapter<Category, AdapterType> {

	protected LayoutInflater mInflater;

	public ItemIdCategorizedAdapter(Context context, AdapterType baseAdapter) {
		super(context, baseAdapter);
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * Return the category of the item at the specified position.
	 * 
	 * @param position
	 *            The position of this item.
	 * @return The category of the item at the given position. May return
	 *         {@code null} to indicate the absence of a particular category.
	 */
	protected abstract Category getCategory(int position);

	/**
	 * Returns a unique id for the category associated with the item at the
	 * given position.
	 * 
	 * @param an
	 *            item position
	 * @return A unique id for the category of the item at the position
	 */
	protected abstract long getItemCategoryId(int position);

	@Override
	protected void recomputeHeaderIndices(TreeMap<Integer, Category> headerIndices, AdapterType delegate) {
		long currentId = Long.MIN_VALUE;
		for (int i = 0; i < delegate.getCount(); ++i) {
			long itemId = getItemCategoryId(i);
			if (itemId != currentId) {
				currentId = itemId;
				Category category = getCategory(i);
				if (category != null) {
					headerIndices.put(headerIndices.size() + i, category);
				}
			}
		}
	}

}
