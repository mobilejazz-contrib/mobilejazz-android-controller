package cat.mobilejazz.controller.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListAdapter<T> extends BaseAdapter {

	protected List<T> data;
	protected Context context;
	private int resourceId;
	private int resourceIdEmpty;
	private LayoutInflater inflater;

	public ListAdapter(List<T> data, int resourceId, int resourceIdEmpty, Context context) {
		if (data != null) {
			this.data = data;
		} else {
			this.data = new ArrayList<T>();
		}
		this.resourceId = resourceId;
		this.resourceIdEmpty = resourceIdEmpty;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}
	
	public ListAdapter(List<T> data, int resourceId, Context context) {
		this(data, resourceId, 0, context);
	}
	
	public ListAdapter(int resourceId, Context context) {
		this(null, resourceId, 0, context);
	}

	protected abstract int getObjectId(T obj);

	protected abstract void updateView(View v, T obj);

	@Override
	public int getCount() {
		if (!data.isEmpty()) {
			return data.size();
		} else {
			return resourceIdEmpty > 0 ? 1 : 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (!data.isEmpty()) {
			return data.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		if (!data.isEmpty()) {
			return getObjectId(data.get(position));
		} else {
			return -1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (!data.isEmpty()) {
			if (view == null || view.getId() != resourceId)
				view = inflater.inflate(resourceId, null);
			updateView(view, data.get(position));
		} else {
			if (view == null || view.getId() != resourceIdEmpty)
				view = inflater.inflate(resourceIdEmpty, null);
		}
		return view;
	}

	public void updateList(List<T> newList) {
		data = newList;
		notifyDataSetChanged();
	}

}
