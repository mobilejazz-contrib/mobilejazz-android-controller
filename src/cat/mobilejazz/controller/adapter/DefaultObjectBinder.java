package cat.mobilejazz.controller.adapter;

import android.database.Cursor;

public class DefaultObjectBinder<T extends CursorBindable> extends ObjectBinder<T> {

	private Class<T> clazz;
	private String tableName;

	public DefaultObjectBinder(Class<T> clazz, String tableName) throws NoSuchMethodException {
		this.clazz = clazz;
		this.tableName = tableName;

		clazz.getConstructor();
	}

	@Override
	public void bindInstance(T obj, Cursor cursor) {
		obj.setValues(cursor);
	}

	@Override
	public T newInstance() {
		try {
			clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

}
