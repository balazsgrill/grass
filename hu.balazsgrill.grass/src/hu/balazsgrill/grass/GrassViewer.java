/**
 * 
 */
package hu.balazsgrill.grass;

import hu.balazsgrill.grass.internal.FigureWrapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author balazs.grill
 *
 */
public class GrassViewer extends ContentViewer {

	FigureCanvas canvas;

	private FigureWrapper rootFigure = null;

	private final List<IFigure> selectedFigures = new ArrayList<IFigure>();

	private final LayeredPane layeredFigure = new LayeredPane();
	private final Layer contentLayer = new Layer();
	private final Layer connectionLayer = new Layer();

	/**
	 * 
	 */
	public GrassViewer(Composite parent) {
		canvas = new FigureCanvas(parent);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Point p = new Point(e.x, e.y);
				canvas.getViewport().translateFromParent(p);
				IFigure f = canvas.getContents().findFigureAt(p.x, p.y);
				if ((e.stateMask & SWT.CONTROL) == SWT.CONTROL) {
					if (f != null) {
						if (selectedFigures.contains(f)) {
							selectedFigures.remove(f);
							//setVisuals(f, FigureStates.NORMAL);
						} else {
							selectedFigures.add(f);
							//setVisuals(f, FigureStates.HIGHLIGHTED);
						}
					}
				} else {
					//for (IFigure c : selectedFigures) {
						//setVisuals(c, FigureStates.NORMAL);
					//}
					selectedFigures.clear();
					selectedFigures.add(f);
					//setVisuals(f, FigureStates.HIGHLIGHTED);
				}
				fireSelectionChanged();
				canvas.redraw();
				super.mouseUp(e);
			}
		});
		canvas.setContents(layeredFigure);
		layeredFigure.add(contentLayer);
		layeredFigure.add(connectionLayer);
	}

	@Override
	public ITreeContentProvider getContentProvider() {
		return (ITreeContentProvider)super.getContentProvider();
	}

	private ISelection computeSelection() {
		List<Object> s = new ArrayList<Object>(selectedFigures.size());
		for (IFigure f : selectedFigures) {
			//TODO s.add(figureToModel.get(f));
		}
		return new StructuredSelection(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl() {
		return canvas;
	}

	public IFigure getFigureByModel(Object model) {
		return null;//TODO modelToFigure.get(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return computeSelection();
	}
	
	public IGrassFigureProvider getFigureProvider() {
		IBaseLabelProvider lp = super.getLabelProvider();
		return lp instanceof IGrassFigureProvider ? (IGrassFigureProvider) lp
				: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {

		IGrassFigureProvider lp = getFigureProvider();
		ITreeContentProvider cp = getContentProvider();
		Object input = getInput();

		if (lp != null && cp != null){
			if (rootFigure != null){
				if (!input.equals(rootFigure.getElement())){
					contentLayer.remove(rootFigure);
					rootFigure = null;
				}
			}
			
			if (rootFigure == null){
				rootFigure = new FigureWrapper(input, this, true);
				contentLayer.add(rootFigure);
			}
			
			rootFigure.update();
			redraw();
		}

		fireSelectionChanged();
	}

	public void redraw() {
		canvas.getViewport().invalidateTree();
		canvas.getLightweightSystem().getUpdateManager()
				.addInvalidFigure(canvas.getViewport());
		canvas.getViewport().validate();

		// canvas.getViewport().validate();
		// canvas.getLightweightSystem().getUpdateManager().performUpdate();
		canvas.redraw();
		// canvas.
		// canvas.update();
	}

//	private void setVisuals(IFigure f, FigureStates state) {
//		Object model = figureToModel.get(f);
//		f.setBackgroundColor(getLabelProvider().getColor(model, state));
//		f.setForegroundColor(getLabelProvider().getBorderColor(model, state));
//		if (f instanceof ILabeledFigure) {
//			ILabeledFigure lf = (ILabeledFigure) f;
//			lf.setLabel(getLabelProvider().getText(model));
//		}
//	}

	private void fireSelectionChanged() {
		fireSelectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers
	 * .ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		if (selection instanceof IStructuredSelection) {
			selectedFigures.clear();
			for (Object o : ((IStructuredSelection) selection).toArray()) {
//				IFigure f = modelToFigure.get(o);
//				if (f != null && !selectedFigures.contains(f)) {
//					selectedFigures.add(f);
//				}
			}
			fireSelectionChanged();
		}

	}

	public void paint(Graphics graphics) {
		IFigure f = canvas.getContents();
		if (f != null) {
			f.paint(graphics);
		}
	}

}
