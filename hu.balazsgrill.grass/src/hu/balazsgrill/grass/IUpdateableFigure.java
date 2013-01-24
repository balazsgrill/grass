/**
 * 
 */
package hu.balazsgrill.grass;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Image;

/**
 * Extension to the {@link IFigure} interface, which adds methods to update certain properties of the figure
 * 
 * @author balazs.grill
 *
 */
public interface IUpdateableFigure extends IFigure {

	public void setFigureLabel(String label);
	
	public void setFigureImage(Image image);
	
	public void setSelectionState(boolean selected);
	
}
