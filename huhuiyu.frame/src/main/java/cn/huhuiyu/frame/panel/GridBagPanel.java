package cn.huhuiyu.frame.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * 简化的使用GridBagLayout布局的JPanel
 */
public class GridBagPanel extends JPanel {
	private static final long serialVersionUID = -2896983646063222319L;
	private GridBagConstraints g = new GridBagConstraints();
	private Insets insets; // 控件间距
	private boolean fill = false;

	/**
	 * 构造一个使用GridBagLayout布局的JPanel，控件之间没有间距
	 */
	public GridBagPanel() {
		this(new Insets(0, 0, 0, 0));
	}

	/**
	 * 构造一个使用GridBagLayout布局的JPanel，控件之间的间距由Insets参数决定
	 * 
	 * @param insets
	 *            控件之间的间距
	 */
	public GridBagPanel(Insets insets) {
		super(new GridBagLayout());
		this.insets = insets;
		g.gridx = 1;
		g.gridy = 1;
		resetConstraints();
	}

	/**
	 * 控件是否为填充方式
	 * 
	 * @return 控件是否为填充方式
	 */
	public boolean isFill() {
		return fill;
	}

	/**
	 * 设定控件是否为填充方式
	 * 
	 * @param fill
	 *            控件是否为填充方式
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}

	/**
	 * 添加组件到指定的行列位置
	 * 
	 * @param component
	 *            要添加的组件
	 * @param row
	 *            行数
	 * @param column
	 *            列数
	 */
	public void addComponent(Component component, int row, int column) {
		resetConstraints();
		g.gridx = column;
		g.gridy = row;
		this.add(component, g);
	}

	/**
	 * 添加组件到指定的行列位置,并指定横向通栏数量和纵向通栏数量
	 * 
	 * @param component
	 *            要添加的组件
	 * @param row
	 *            行数
	 * @param column
	 *            列数
	 * @param width
	 *            横向通栏数
	 * @param height
	 *            纵向通栏数量
	 */
	public void addComponent(Component component, int row, int column,
			int width, int height) {
		resetConstraints();
		g.gridx = column;
		g.gridy = row;
		g.gridwidth = width;
		g.gridheight = height;
		this.add(component, g);
	}

	/**
	 * 添加组件到指定的行列位置,并指定行列的权重
	 * 
	 * @param component
	 *            要添加的组件
	 * @param row
	 *            行数
	 * @param column
	 *            列数
	 * @param weightx
	 *            行的权重(不修改输入就输入1)
	 * @param weighty
	 *            列的权重(不修改输入就输入1)
	 */
	public void addComponent(Component component, int row, int column,
			double weightx, double weighty) {
		resetConstraints();
		g.gridx = column;
		g.gridy = row;
		g.weightx = weightx;
		g.weighty = weighty;
		this.add(component, g);
	}

	/**
	 * 添加组件到指定的行列位置,并指定横向通栏数量和纵向通栏数量，以及行列的权重值
	 * 
	 * @param component
	 *            要添加的组件
	 * @param row
	 *            行数
	 * @param column
	 *            列数
	 * @param width
	 *            横向通栏数
	 * @param height
	 *            纵向通栏数量
	 * @param weightx
	 *            行的权重(不修改输入就输入1)
	 * @param weighty
	 *            列的权重(不修改输入就输入1)
	 */
	public void addComponent(Component component, int row, int column,
			int width, int height, double weightx, double weighty) {
		resetConstraints();
		g.gridx = column;
		g.gridy = row;
		g.gridwidth = width;
		g.gridheight = height;
		g.weightx = weightx;
		g.weighty = weighty;
		this.add(component, g);
	}

	/**
	 * 初始化GridBagConstraints的状态
	 */
	private void resetConstraints() {
		g.insets = insets;
		g.weightx = 1; // 复原列的权重
		g.weighty = 1; // 复原行的权重
		g.gridwidth = 1; // 复原横向通栏
		g.gridheight = 1; // 复原纵向通栏
		g.anchor = GridBagConstraints.CENTER; // 中心对齐方式

		g.fill = fill ? GridBagConstraints.BOTH : GridBagConstraints.CENTER;
	}

}
