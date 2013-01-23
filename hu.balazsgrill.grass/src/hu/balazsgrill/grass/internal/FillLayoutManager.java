/**
 * 
 */
package hu.balazsgrill.grass.internal;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author balazs.grill
 *
 */
public class FillLayoutManager extends AbstractLayout {

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure container) {
		Rectangle r = container.getClientArea();
		for(Object f : container.getChildren()){
			if (f instanceof IFigure){
				((IFigure) f).setBounds(new Rectangle(r));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint,
			int hHint) {
		Dimension size = new Dimension(wHint, hHint);
		for(Object f : container.getChildren()){
			if (f instanceof IFigure){
				Dimension d = ((IFigure) f).getPreferredSize(wHint, hHint);
				size = new Dimension(Math.max(size.width, d.width), Math.max(size.height, d.height));
			}
		}
		return size;
	}

}
