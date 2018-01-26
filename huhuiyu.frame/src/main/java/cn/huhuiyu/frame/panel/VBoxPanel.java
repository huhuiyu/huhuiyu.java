package cn.huhuiyu.frame.panel;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 使用垂直分布的箱式布局JPanel
 * 
 * @author huhuiyu
 * 
 */
public class VBoxPanel extends JPanel {
	private static final long serialVersionUID = 2295535660569038660L;

	private Box box = Box.createVerticalBox();
	private int insert;

	/**
	 * 创建使用垂直分布的箱式布局JPanel并添加组件
	 * 
	 * @param insert
	 *            组件的之间的间距
	 * @param components
	 *            要添加的组件
	 */
	public VBoxPanel(int insert, JComponent... components) {
		super();
		if (components == null) {
			return;
		}
		this.insert = insert;
		box.add(Box.createVerticalStrut(insert));
		this.addComponents(components);
		this.add(box);
	}

	/**
	 * 创建使用垂直分布的箱式布局JPanel并添加组件
	 * 
	 * @param components
	 *            要添加的组件
	 */
	public VBoxPanel(JComponent... components) {
		this(1, components);
	}

	/**
	 * 添加组件
	 * 
	 * @param components
	 *            要添加的组件
	 */
	public void addComponents(JComponent... components) {
		for (JComponent component : components) {
			box.add(component);
			box.add(Box.createVerticalStrut(insert));
		}
	}
}
