package cn.huhuiyu.frame.panel;

import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 可以一次添加多个组件的使用FlowLayout布局的JPanel
 * 
 * @author huhuiyu
 * 
 */
public class FlowPanel extends JPanel {
	private static final long serialVersionUID = 9069533930878168160L;

	/**
	 * 构造FlowPanel并添加多个组件
	 * 
	 * @param components
	 *            要添加的组件
	 */
	public FlowPanel(JComponent... components) {
		super();
		this.setLayout(new FlowLayout());
		this.addComponents(components);
	}

	/**
	 * 构造FlowPanel，指定对齐方式并添加多个组件，
	 * 
	 * @param align
	 *            组件对齐方式(参考FlowLayout)
	 * @param components
	 *            要添加的组件
	 */
	public FlowPanel(int align, JComponent... components) {
		super();
		this.setLayout(new FlowLayout(align));
		this.addComponents(components);
	}

	/**
	 * 添加组件到FlowPanel
	 * 
	 * @param components
	 *            要添加的组件
	 */
	public void addComponents(JComponent... components) {
		if (components == null) {
			return;
		}
		for (JComponent component : components) {
			this.add(component);
		}
	}
}
