package cat.mobilejazz.controller.adapter;

import android.database.Cursor;

public interface CursorBindable {

	public void setValues(Cursor c);

	public String getTableName();

}
