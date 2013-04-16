package cat.mobilejazz.controller.adapter;

import android.database.Cursor;

public abstract class ObjectBinder<T> {

	/**
	 * Binds an already existing instance of an object to a new cursor entry.
	 * 
	 * @param instance
	 *            An instance to reuse.
	 * @param c
	 *            The cursor.
	 * @return A changed object instance reflecting the contents of the cursor.
	 */
	public abstract void bindInstance(T obj, Cursor cursor);

	/**
	 * Creates a new unassigned instance of {@code T}.
	 * 
	 * @return An new empty object of type {@code T}.
	 */
	public abstract T newInstance();

	/**
	 * This binder can only accept {@link Cursor}s that point to the table
	 * returned by this method.
	 * 
	 * @return The table associated with this binder.
	 */
	public abstract String getTableName();

	/**
	 * Same as {@link #bindInstance(Object, Cursor)} but creates a new instance
	 * if {@code obj} is {@code null}.
	 * 
	 * @param obj
	 *            An instance to reuse. May be {@code null}.
	 * @param cursor
	 *            The cursor.
	 * @return A changed object instance reflecting the contents of the cursor.
	 *         If {@code obj} was null the result reflects a new instance.
	 */
	public T getInstance(T obj, Cursor cursor) {
		if (obj == null) {
			obj = newInstance();
		}
		bindInstance(obj, cursor);
		return obj;
	}

	/**
	 * @return A new instance reflecting the given cursor.
	 */
	public T newInstance(Cursor cursor) {
		T obj = newInstance();
		bindInstance(obj, cursor);
		return obj;
	}
}