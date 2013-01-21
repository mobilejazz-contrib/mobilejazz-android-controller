package cat.mobilejazz.controller.databinding;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class LoaderDataBinding<T extends Adapter, D> extends DataBinding<T> implements LoaderCallbacks<D>,
		OnItemSelectedListener {

	public interface LoaderFactory<D> {

		public Loader<D> getLoader(Bundle args);

		public int getId();

	}

	public interface MultiLoaderFactory<D> {

		public Loader<D> getLoader(int id, Bundle args);

	}

	public abstract static class AbstractLoaderFactory<D> implements LoaderFactory<D> {

		private int id;

		public AbstractLoaderFactory(int id) {
			this.id = id;
		}

		@Override
		public int getId() {
			return id;
		}

	}

	public static class DefaultLoaderFactory<D> extends AbstractLoaderFactory<D> {

		private MultiLoaderFactory<D> multiFactory;

		public DefaultLoaderFactory(int id, MultiLoaderFactory<D> multiFactory) {
			super(id);
			this.multiFactory = multiFactory;
		}

		@Override
		public Loader<D> getLoader(Bundle args) {
			return multiFactory.getLoader(getId(), args);
		}

	}

	private boolean dataReady;
	private boolean pendingLoad;
	private LoaderFactory<D> loaderFactory;
	private LoaderManager loaderManager;

	private List<LoaderDataBinding<?, ?>> dependencies;
	private List<LoaderDataBinding<?, ?>> dependsOn;
	
	private OnItemSelectedListener itemSelectedListener;

	private void init(LoaderFactory<D> loaderFactory, LoaderManager loaderManager) {
		this.loaderFactory = loaderFactory;
		this.loaderManager = loaderManager;
		this.dataReady = false;
		this.pendingLoad = false;
		dependencies = new ArrayList<LoaderDataBinding<?, ?>>();
		dependsOn = new ArrayList<LoaderDataBinding<?, ?>>();
	}

	public LoaderDataBinding(View parent, int resId, T adapter, LoaderFactory<D> loaderFactory,
			LoaderManager loaderManager) {
		super(parent, resId, adapter);
		init(loaderFactory, loaderManager);
	}

	public LoaderDataBinding(AdapterView<T> view, T adapter, LoaderFactory<D> loaderFactory, LoaderManager loaderManager) {
		super(view, adapter);
		init(loaderFactory, loaderManager);
	}
	
	@Override
	public void bind() {
		super.bind();
		getView().setOnItemSelectedListener(this);
	}

	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		itemSelectedListener = listener;
	}
	
	public void addDependency(LoaderDataBinding<?, ?> binding) {
		dependencies.add(binding);
		binding.dependsOn.add(this);
	}

	public void initLoader(Bundle args) {
		pendingLoad = true;
		loaderManager.initLoader(loaderFactory.getId(), args, this);
	}

	public void restartLoader(Bundle args) {
		pendingLoad = true;
		loaderManager.restartLoader(loaderFactory.getId(), args, this);
	}

	protected boolean isDataReady() {
		return dataReady;
	}

	protected boolean isLoading() {
		return pendingLoad;
	}

	protected abstract void updateAdapter(D data);

	@Override
	public Loader<D> onCreateLoader(int id, Bundle args) {
		if (id == loaderFactory.getId()) {
			return loaderFactory.getLoader(args);
		} else {
			throw new IllegalArgumentException("Invalid loader id for this binding: " + id);
		}
	}

	@Override
	public void onLoadFinished(Loader<D> loader, D data) {
		pendingLoad = false;
		if (loader.getId() == loaderFactory.getId()) {
			Object selectedItem = getView().getSelectedItem();
			updateAdapter(data);
			if (selectedItem == null && getView().getSelectedItem() != null) {
				// selected item was null before updateAdapter. This means that
				// there was a onItemSelected in the past where the dependencies
				// were not updated. We need to update them now
				updateDependencies();
			}
			dataReady = true;
		} else {
			throw new IllegalArgumentException("Invalid loader id for this binding: " + loader.getId());
		}
	}

	@Override
	public void onLoaderReset(Loader<D> loader) {
		if (loader.getId() == loaderFactory.getId()) {
			dataReady = false;
			updateAdapter(null);
		} else {
			throw new IllegalArgumentException("Invalid loader id for this binding: " + loader.getId());
		}
	}
	
	protected void updateDependencies() {
		for (LoaderDataBinding<?, ?> binding : dependencies) {
			binding.restartLoader(null);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (getView().getSelectedItem() != null) {
			updateDependencies();
			if (itemSelectedListener != null) {
				itemSelectedListener.onItemSelected(parent, view, position, id);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public String toString() {
		return String.format("LoaderDatabinding [%d]", loaderFactory.getId());
	}

}
