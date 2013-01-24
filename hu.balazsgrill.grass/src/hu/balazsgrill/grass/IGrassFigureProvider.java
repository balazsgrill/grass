/**
 * 
 */
package hu.balazsgrill.grass;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * @author balazs.grill
 *
 */
public interface IGrassFigureProvider extends ILabelProvider{

	/**
	 * Create figure for the given element
	 * 
	 * @param element
	 * @return
	 */
	public IFigure createFigure(Object element);
	
}
