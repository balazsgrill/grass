/**
 * 
 */
package hu.balazsgrill.grass.test;

import hu.balazsgrill.grass.GrassViewer;
import hu.balazsgrill.grass.IGrassFigureProvider;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author balazs.grill
 *
 */
public class TestApplication extends LabelProvider implements IApplication, IGrassFigureProvider, ITreeContentProvider {

	private Object[] data1 = new Object[]{"A","B","C"};
	private Object[] data2 = new Object[]{"1","2","3"};
	private Object input = "input";
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setBounds(100, 100, 600, 400);

		GrassViewer viewer = new GrassViewer(shell, SWT.BORDER);
		viewer.setContentProvider(this);
		viewer.setLabelProvider(this);
		viewer.setInput(input);
		viewer.refresh();

		shell.open();
		while( !shell.isDisposed() ) {
			if( ! display.readAndDispatch() ) {
				display.sleep();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return data1;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if ("A".equals(parentElement))
		return data2;
		return new Object[]{};
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return ("A".equals(element)) || input.equals(element);
	}

	@Override
	public IFigure createFigure(Object element) {
		Label figure = new Label();
		figure.setText(element+"");
		figure.setLayoutManager(new FlowLayout());
		return figure;
	}

}
