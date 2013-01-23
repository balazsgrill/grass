/**
 * 
 */
package hu.balazsgrill.grass.internal;

import hu.balazsgrill.grass.GrassViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * @author balazs.grill
 *
 */
public class FigureWrapper extends Figure {

	private final Object element;
	private final GrassViewer viewer;
	private final boolean root;
	
	private final LinkedList<FigureWrapper> subFigures = new LinkedList<>();
	
	public Object getElement() {
		return element;
	}
	
	public FigureWrapper(Object element, GrassViewer viewer, boolean root) {
		this.viewer = viewer;
		setLayoutManager(new FillLayoutManager());
		this.element = element;
		this.root = root;
		IFigure figure = viewer.getFigureProvider().createFigure(element);
		this.add(figure);
	}
	
	private List<Object> getChildrenElements(){
		List<Object> elements = new ArrayList<>(subFigures.size());
		for(FigureWrapper f : subFigures){
			elements.add(f.element);
		}
		return elements;
	}
	 
 	public void update(){
		ITreeContentProvider cp = viewer.getContentProvider();
		Object[] children = root ? cp.getElements(element) : cp.getChildren(element);
		List<Object> chlist = Arrays.asList(children);
		List<Object> current = new ArrayList<>(getChildrenElements());
		
		ListDiff diff = Diffs.computeListDiff(current, chlist);
		for(ListDiffEntry de : diff.getDifferences()){
			if (de.isAddition()){
				FigureWrapper f = new FigureWrapper(de.getElement(), viewer, false);
				this.add(f, de.getPosition());
				subFigures.add(de.getPosition(), f);
			}else{
				this.remove(subFigures.get(de.getPosition()));
				subFigures.remove(de.getPosition());
			}
		}
		
		invalidateTree();
	}
 	
}
