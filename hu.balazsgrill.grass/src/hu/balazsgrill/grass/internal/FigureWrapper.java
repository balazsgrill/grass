/**
 * 
 */
package hu.balazsgrill.grass.internal;

import hu.balazsgrill.grass.GrassViewer;
import hu.balazsgrill.grass.IGrassFigureProvider;
import hu.balazsgrill.grass.IUpdateableFigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author balazs.grill
 *
 */
public class FigureWrapper {

	private final Object element;
	private final GrassViewer viewer;
	private final boolean root;
	private final IFigure figure;
	
	private final LinkedList<FigureWrapper> subFigures = new LinkedList<FigureWrapper>();
	
	public Object getElement() {
		return element;
	}
	
	public IFigure getFigure() {
		return figure;
	}
	
	public FigureWrapper(Object element, GrassViewer viewer, boolean root) {
		this.viewer = viewer;
		this.element = element;
		this.root = root;
		figure = viewer.getLabelProvider().createFigure(element);
		if (figure == null){
			throw new IllegalStateException("Figure cannot be null! (element: "+element+")");
		}
		//System.out.println("Figure for "+element);
	}
	
	private List<Object> getChildrenElements(){
		List<Object> elements = new ArrayList<Object>(subFigures.size());
		for(FigureWrapper f : subFigures){
			elements.add(f.element);
		}
		return elements;
	}
	 
	public void setFigureLabel(String label){
		if (figure instanceof IUpdateableFigure){
			((IUpdateableFigure) figure).setFigureLabel(label);
			figure.invalidate();
		}
	}
	
	public void setFigureImage(Image image){
		if (figure instanceof IUpdateableFigure){
			((IUpdateableFigure) figure).setFigureImage(image);
			figure.invalidate();
		}
	}
	
	public void setSelectionState(boolean selected){
		if (figure instanceof IUpdateableFigure){
			((IUpdateableFigure) figure).setSelectionState(selected);
			figure.invalidate();
		}
	}
	
 	public void update(){
		ITreeContentProvider cp = viewer.getContentProvider();
		IGrassFigureProvider lp = viewer.getLabelProvider();
		
		setFigureLabel(lp.getText(element));
		setFigureImage(lp.getImage(element));
		setSelectionState(false);
		
		Object[] children = root ? cp.getElements(element) : cp.getChildren(element);
		List<Object> chlist = Arrays.asList(children);
		List<Object> current = new ArrayList<Object>(getChildrenElements());
		
		ListDiff diff = Diffs.computeListDiff(current, chlist);
		for(ListDiffEntry de : diff.getDifferences()){
			if (de.isAddition()){
				FigureWrapper f = new FigureWrapper(de.getElement(), viewer, false);
				if (f.getFigure() != null){
					figure.add(f.getFigure(), de.getPosition());
					subFigures.add(de.getPosition(), f);
					f.update();
				}
			}else{
				figure.remove(subFigures.get(de.getPosition()).getFigure());
				subFigures.remove(de.getPosition());
			}
		}
		
		figure.invalidateTree();
	}
 	
}
