package cn.huhuiyu.frame.panel;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 折叠显示的JPanel
 * 
 * @author huhuiyu
 * 
 */
public class FoldPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = -7015114749652871773L;
	private GridBagPanel gbp = new GridBagPanel();
	private List<JComponent> components = new ArrayList<JComponent>();
	private JLabel lblEmpty = new JLabel("");
	private boolean fold = false;

	/**
	 * 构造FoldPanel
	 */
	public FoldPanel() {
		super(new BorderLayout());
		this.add(gbp);
		gbp.setFill(true);
		lblEmpty.setVisible(false);
	}

	/**
	 * 获取面板是否为折叠模式
	 * 
	 * @return 是否为折叠模式
	 */
	public boolean isFold() {
		return fold;
	}

	/**
	 * 设置面板是否为折叠模式
	 * 
	 * @param fold
	 *            是否为折叠模式
	 */
	public void setFold(boolean fold) {
		this.fold = fold;
	}

	/**
	 * 添加一组控件到FoldPanel
	 * 
	 * @param title
	 *            标题组件，点击可以显示item组件
	 * @param item
	 *            title配对显示的组件
	 */
	public void addComponents(JComponent title, JComponent item) {
		// 清除所有组件
		for (JComponent component : components) {
			gbp.remove(component);
		}
		// 添加新的组件
		components.add(title);
		components.add(item);
		// 设置事件和状态
		title.addMouseListener(this);
		item.setVisible(false);
		// 添加组件到面板
		for (int i = 0; i < components.size(); i++) {
			if (i % 2 == 0) {
				gbp.addComponent(components.get(i), i + 1, 1, 1, 1.0);
			} else {
				gbp.addComponent(components.get(i), i + 1, 1, 1, 10000.0);
			}
		}
		// 添加空置面板
		gbp.addComponent(lblEmpty, components.size() + 1, 1, 1, 10000.0);
		if (!checkVisible()) {
			changeFold(0);
		}
		this.updateUI();
	}

	/**
	 * 移除指定的组件并返回配对的组件，移除失败返回null
	 * 
	 * @param component
	 *            要移除的组件
	 * 
	 * @return 配对的组件
	 */
	public JComponent removeComponent(JComponent component) {
		int index = components.indexOf(component);
		if (index < 0) {
			return null;
		}
		JComponent comp;
		if (index % 2 == 1) {
			comp = components.get(index - 1);
		} else {
			comp = components.get(index + 1);
		}
		components.remove(component);
		components.remove(comp);
		gbp.remove(component);
		gbp.remove(comp);
		if (components.size() > 0 && !checkVisible()) {
			changeFold(0);
		} else {
			this.updateUI();
		}
		return comp;
	}

	/**
	 * 展开index指定的组件
	 * 
	 * @param index
	 *            组件的索引
	 */
	private void changeFold(int index) {
		if (fold) {
			JComponent comp = components.get(index + 1);
			comp.setVisible(!comp.isVisible());
			lblEmpty.setPreferredSize(gbp.getSize());
			lblEmpty.setVisible(!checkVisible());
		} else {
			for (int i = 1; i < components.size(); i = i + 2) {
				components.get(i).setVisible(false);
			}
			components.get(index + 1).setVisible(true);
		}
	}

	/**
	 * 获取是否有配对组件是显示的
	 * 
	 * @return 是否有配对组件是显示的
	 */
	private boolean checkVisible() {
		if (components.isEmpty()) {
			return false;
		}
		for (int i = 1; i < components.size(); i = i + 2) {
			if (components.get(i).isVisible()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		changeFold(components.indexOf(e.getSource()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
