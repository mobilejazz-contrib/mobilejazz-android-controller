package cat.mobilejazz.controller.adapter;

import android.database.Cursor;
import android.widget.SpinnerAdapter;

public interface ICursorAdapter<E> extends SpinnerAdapter {

	public void setCursor(Cursor data);
	
	public int getPosition(E entry);
	
}
