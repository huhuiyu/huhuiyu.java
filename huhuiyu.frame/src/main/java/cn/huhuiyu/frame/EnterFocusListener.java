package cn.huhuiyu.frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;

/**
 * 一个键盘事件监听类,当按下Enter键时component组件获得焦点
 * 
 * @author 胡辉煜
 */
public class EnterFocusListener implements KeyListener {
	private JComponent component;

	public EnterFocusListener(JComponent component) {
		this.component = component;
	}

	public static EnterFocusListener newInstance(JComponent component) {
		return new EnterFocusListener(component);
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) component.requestFocusInWindow();
	}

	public void keyReleased(KeyEvent e) {}
}
