package cat.mobilejazz.controller.observer;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.SparseIntArray;

public class ObservableAsyncQueryHandler {

	public static final int TOKEN_GENERIC = -1;

	public static interface OnCompleteHandler {

		public void onQueryComplete(int token, Object cookie, Cursor result);

		public void onUpdateComplete(int token, Object cookie, int result);

		public void onDeleteComplete(int token, Object cookie, int result);

		public void onInsertComplete(int token, Object cookie, Uri result);

	}

	private class AsyncQueryHandlerWrapper extends AsyncQueryHandler {

		private Activity mContext;

		public AsyncQueryHandlerWrapper(Activity context) {
			super(context.getContentResolver());
			mContext = context;
		}

		@Override
		protected void onQueryComplete(final int token, final Object cookie, final Cursor cursor) {
			decrementPendingStatement(token);
			ObservableAsyncQueryHandler.this.onQueryComplete(token, cookie, cursor);
			if (mCompleteHandler != null) {
				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mCompleteHandler.onQueryComplete(token, cookie, cursor);
					}
				});
			}
		}

		@Override
		protected void onUpdateComplete(final int token, final Object cookie, final int result) {
			decrementPendingStatement(token);
			ObservableAsyncQueryHandler.this.onUpdateComplete(token, cookie, result);
			if (mCompleteHandler != null) {
				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mCompleteHandler.onUpdateComplete(token, cookie, result);
					}
				});
			}
		}

		@Override
		protected void onDeleteComplete(final int token, final Object cookie, final int result) {
			decrementPendingStatement(token);
			ObservableAsyncQueryHandler.this.onDeleteComplete(token, cookie, result);
			if (mCompleteHandler != null) {
				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mCompleteHandler.onDeleteComplete(token, cookie, result);
					}
				});
				
			}
		}

		@Override
		protected void onInsertComplete(final int token, final Object cookie, final Uri uri) {
			decrementPendingStatement(token);
			ObservableAsyncQueryHandler.this.onInsertComplete(token, cookie, uri);
			if (mCompleteHandler != null) {
				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mCompleteHandler.onInsertComplete(token, cookie, uri);
					}
				});
			}
		}

	}

	private AsyncQueryHandlerWrapper mDelegate;
	private OnCompleteHandler mCompleteHandler;
	private SparseIntArray mPendingSQLStatements;

	public ObservableAsyncQueryHandler(Activity context, OnCompleteHandler completeHandler) {
		mDelegate = new AsyncQueryHandlerWrapper(context);
		mCompleteHandler = completeHandler;
		mPendingSQLStatements = new SparseIntArray();
	}

	private void incrementPendingStatement(int token) {
		synchronized (mPendingSQLStatements) {
			int currentPending = mPendingSQLStatements.get(token);
			mPendingSQLStatements.put(token, ++currentPending);
		}
	}

	private void decrementPendingStatement(int token) {
		synchronized (mPendingSQLStatements) {
			int currentPending = mPendingSQLStatements.get(token);
			if (currentPending == 1) {
				mPendingSQLStatements.delete(token);
			} else {
				mPendingSQLStatements.put(token, --currentPending);
			}
		}
	}

	public boolean hasPendingStatements() {
		return mPendingSQLStatements.size() > 0;
	}

	public void startQuery(int token, Uri uri, String[] projection, String selection, String[] selectionArgs,
			String orderBy) {
		incrementPendingStatement(token);
		mDelegate.startQuery(token, null, uri, projection, selection, selectionArgs, orderBy);
	}

	public void startUpdate(int token, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		startUpdate(token, null, uri, values, selection, selectionArgs);
	}

	public void startDelete(int token, Uri uri, String selection, String[] selectionArgs) {
		startDelete(token, null, uri, selection, selectionArgs);
	}

	public void startInsert(int token, Uri uri, ContentValues values) {
		startInsert(token, null, uri, values);
	}

	public void startUpdate(int token, Object cookie, Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (values.size() > 0) {
			incrementPendingStatement(token);
			mDelegate.startUpdate(token, cookie, uri, values, selection, selectionArgs);
		}
	}

	public void startDelete(int token, Object cookie, Uri uri, String selection, String[] selectionArgs) {
		incrementPendingStatement(token);
		mDelegate.startDelete(token, cookie, uri, selection, selectionArgs);
	}

	public void startInsert(int token, Object cookie, Uri uri, ContentValues values) {
		incrementPendingStatement(token);
		mDelegate.startInsert(token, cookie, uri, values);
	}

	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	}

	protected void onUpdateComplete(int token, Object cookie, int result) {
	}

	protected void onDeleteComplete(int token, Object cookie, int result) {
	}

	protected void onInsertComplete(int token, Object cookie, Uri uri) {
	}

}
