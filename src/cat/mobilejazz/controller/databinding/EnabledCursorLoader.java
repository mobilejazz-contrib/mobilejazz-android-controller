/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cat.mobilejazz.controller.databinding;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import cat.mobilejazz.utilities.debug.Debug;

/**
 * Static library support version of the framework's
 * {@link android.content.CursorLoader}. Used to write apps that run on
 * platforms prior to Android 3.0. When running on Android 3.0 or above, this
 * implementation is still used; it does not try to switch to the framework's
 * implementation. See the framework SDK documentation for a class overview.
 */
public class EnabledCursorLoader extends ObservableCursorLoader {

	public final class CachedLoadContentObserver extends ContentObserver implements OnEnabledChangedListener {

		private LoaderParent mParent;
		private boolean mCachedContentChange;

		public CachedLoadContentObserver(LoaderParent parent) {
			super(new Handler());
			mParent = parent;
		}

		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}

		@Override
		public void onChange(boolean selfChange) {
			if (mParent.isEnabled()) {
				Debug.debug("onChange: reload");
				onContentChanged();
			} else {
				Debug.debug("onChange: cached");
				mCachedContentChange = true;
			}
		}

		@Override
		public void onEnabledChanged(boolean on) {
			if (on && mCachedContentChange) {
				mCachedContentChange = false;
				onContentChanged();
			}
		}

	}

	final CachedLoadContentObserver mObserver;

	public EnabledCursorLoader(LoaderParent parent) {
		super(parent.getContext());
		mObserver = new CachedLoadContentObserver(parent);
	}

	public EnabledCursorLoader(LoaderParent parent, Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		super(parent.getContext(), uri, projection, selection, selectionArgs, sortOrder);
		mObserver = new CachedLoadContentObserver(parent);
	}

	protected void registerContentObserver(Cursor cursor) {
		cursor.registerContentObserver(mObserver);
	}

}
