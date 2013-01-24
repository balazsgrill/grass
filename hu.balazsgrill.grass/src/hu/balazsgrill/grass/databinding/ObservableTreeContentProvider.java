/**
 * 
 */
package hu.balazsgrill.grass.databinding;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author balazs.grill
 *
 */
public class ObservableTreeContentProvider implements ITreeContentProvider {

	private final IObservableFactory observableFactory;
	private final Map<Object, IObservableList> children = new HashMap<Object, IObservableList>();
	
	private Viewer viewer;
	
	private final IListChangeListener listener = new IListChangeListener() {
		
		@Override
		public void handleListChange(ListChangeEvent event) {
			event.diff.accept(new ListDiffVisitor() {
				
				@Override
				public void handleRemove(int index, Object element) {
					disposeList(element);
				}
				
				@Override
				public void handleAdd(int index, Object element) {
				}
			});
			if (viewer != null){
				viewer.refresh();
			}
		}
	};
	
	private IObservableList getChildrenList(Object object){
		IObservableList list = children.get(object);
		if (list == null){
			list = (IObservableList) observableFactory.createObservable(object);
			children.put(object, list);
			list.addListChangeListener(listener);
		}
		return list;
	}
	
	private void disposeList(Object o){
		IObservableList list = children.remove(o);
		if (list != null) list.dispose();
	}
	
	private void disposeAll(){
		for(Object o : children.keySet().toArray()){
			disposeList(o);
		}
	}
	
	/**
	 * 
	 */
	public ObservableTreeContentProvider(IObservableFactory observableFactory) {
		this.observableFactory = observableFactory;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		disposeAll();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		disposeAll();
		this.viewer = viewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		return getChildrenList(parentElement).toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return getChildrenList(element).isEmpty();
	}

}
