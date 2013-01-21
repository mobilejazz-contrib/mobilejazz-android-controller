package cat.mobilejazz.controller.spinner;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

public class ConverterSpinnerCursorAdapter<T> extends SpinnerCursorAdapter<T> {

	public static interface CursorConverter<T> {

		public T convert(Cursor cursor);

	}

	private CursorConverter<T> mConverter;

	public ConverterSpinnerCursorAdapter(Context context, int layoutResId, int textViewResId, boolean showProgress,
			CursorConverter<T> converter, T... staticObjects) {
		super(context, layoutResId, textViewResId, showProgress, staticObjects);
		mConverter = converter;
	}

	public ConverterSpinnerCursorAdapter(Context context, int textViewResId, boolean showProgress,
			CursorConverter<T> converter, T... staticObjects) {
		super(context, textViewResId, showProgress, staticObjects);
		mConverter = converter;
	}

	@Override
	protected T getObject(Cursor cursor) {
		return mConverter.convert(cursor);
	}

	@Override
	protected void bindView(Context context, View view, T object, int position, boolean dropDown) {
		// do nothing
	}

}
