/**
 * 
 */
package hu.balazsgrill.grass;

import org.eclipse.draw2d.IFigure;

/**
 * @author balazs.grill
 *
 */
public interface IGrassFigureProvider {

	/**
	 * Create figure for the given element
	 * 
	 * @param element
	 * @return
	 */
	public IFigure createFigure(Object element);
	
}
