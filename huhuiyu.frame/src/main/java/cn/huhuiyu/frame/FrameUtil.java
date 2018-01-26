package cn.huhuiyu.frame;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import cn.huhuiyu.beanutil.MyBeanUtils;

public class FrameUtil {
	private FrameUtil() {
	}

	/**
	 * 移动JFrame到屏幕中心
	 * 
	 * @param frame
	 *            要移动的JFrame
	 */
	public static void moveFrameToCenter(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension size = toolkit.getScreenSize();
		Dimension frameSize = frame.getSize();
		int left = (size.width - frameSize.width) / 2;
		int top = (size.height - frameSize.height) / 2;
		frame.setLocation(left, top);
	}

	/**
	 * 移动JDialog到屏幕中心
	 * 
	 * @param dialog
	 *            要移动的JDialog
	 */
	public static void moveDialogToCenter(JDialog dialog) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension size = toolkit.getScreenSize();
		Dimension frameSize = dialog.getSize();
		int left = (size.width - frameSize.width) / 2;
		int top = (size.height - frameSize.height) / 2;
		dialog.setLocation(left, top);
	}

	/**
	 * 获取剪贴板中的文本
	 * 
	 * @return 剪贴板中的文本
	 * @throws UnsupportedFlavorException
	 * @throws IOException
	 */
	public static String getClipboardText() throws UnsupportedFlavorException,
			IOException {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		Transferable tran = clipboard.getContents(null);
		if (tran.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return (String) tran.getTransferData(DataFlavor.stringFlavor);
		}
		return null;
	}

	/**
	 * 放置文本数据到剪贴板
	 * 
	 * @param data
	 *            放置到剪贴板的数据
	 */
	public static void sendClipboardText(String data) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		clipboard.setContents(new StringSelection(data), null);
	}

	/**
	 * 获取剪贴板中的图片
	 * 
	 * @return 剪贴板中的图片
	 * @throws UnsupportedFlavorException
	 * @throws IOException
	 */
	public static Image getClipboardImage() throws UnsupportedFlavorException,
			IOException {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		Transferable tran = clipboard.getContents(null);
		if (tran.isDataFlavorSupported(DataFlavor.imageFlavor)) {
			return (Image) tran.getTransferData(DataFlavor.imageFlavor);
		}
		return null;
	}

	/**
	 * 放置图片数据到剪贴板
	 * 
	 * @param data
	 *            放置到剪贴板的数据
	 */
	public static void sendClipboardImage(Image image) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		clipboard.setContents(new ImageSelection(image), null);
	}

	/**
	 * 显示一个JFrame窗口
	 * 
	 * @param frame
	 *            要显示的JFrame类
	 */
	public static <T extends JFrame> void showSwingFrame(Class<T> frame) {
		showSwingFrame(frame, true, null);
	}

	/**
	 * 显示一个JFrame窗口
	 * 
	 * @param frame
	 *            要显示的JFrame类
	 * @param moveToScreenCenter
	 *            是否移动到屏幕中心
	 */
	public static <T extends JFrame> void showSwingFrame(Class<T> frame,
			boolean moveToScreenCenter) {
		showSwingFrame(frame, moveToScreenCenter, null);
	}

	/**
	 * 显示一个JFrame窗口
	 * 
	 * @param frame
	 *            要显示的JFrame类
	 * @param callBack
	 *            回调函数
	 */
	public static <T extends JFrame> void showSwingFrame(Class<T> frame,
			RunCallBack<T> callBack) {
		showSwingFrame(frame, true, callBack);
	}

	/**
	 * 显示一个JFrame窗口
	 * 
	 * @param frame
	 *            要显示的JFrame类
	 * @param moveToScreenCenter
	 *            是否移动到屏幕中心
	 * @param callBack
	 *            回调函数
	 */
	public static <T extends JFrame> void showSwingFrame(Class<T> frame,
			boolean moveToScreenCenter, RunCallBack<T> callBack) {
		RunJFrame<T> run = new RunJFrame<T>(frame, moveToScreenCenter, callBack);
		SwingUtilities.invokeLater(run);
	}

	public static void setDefaultLookAndFeelDecorated() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
	}

	/**
	 * 设定一个窗体用Enter为焦点切换键
	 * 
	 * @param frame
	 *            要设定的窗体
	 */
	public static void setEnterToFocus(JFrame frame) {
		Set<AWTKeyStroke> oldKeys = frame
				.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		HashSet<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>();
		forwardKeys.addAll(oldKeys);
		forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		frame.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
	}

	/**
	 * 设定一个集合中所有的组件为同一种字体
	 * 
	 * @param components
	 *            要设定字体的组件集合
	 * @param font
	 *            要设定的字体
	 * @param <T>
	 */
	public static <T extends JComponent> void setComponentsFont(
			Collection<T> components, Font font) {
		if (font == null)
			return;
		for (JComponent component : components)
			component.setFont(font);
	}

	/**
	 * 将JComponent中所有的JTextComponent添加FocusListener
	 * 
	 * @param parent
	 *            父组件
	 * @param action
	 *            FocusListener
	 */
	public static void addTextFocusListener(JComponent parent,
			FocusListener action) {
		for (Component component : parent.getComponents()) {
			if (Arrays.asList(component.getClass().getInterfaces()).contains(
					JTextComponent.class)
					|| MyBeanUtils.getSuperClasses(component.getClass())
							.contains(JTextComponent.class)
					|| component.getClass().equals(JTextComponent.class)) {
				JTextComponent text = (JTextComponent) component;
				text.addFocusListener(action);
			}
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				addTextFocusListener(jcomponent, action);
			}
		}
	}

	/**
	 * 为JComponent中所有按钮组件添加ActionListener
	 * 
	 * @param <T>
	 * @param parent
	 *            父组件
	 * @param c
	 *            组件类型
	 * @param ActionListener
	 *            字体
	 */
	public static <T extends JComponent> void addComponentsAction(
			JComponent parent, ActionListener action) {
		Class<JButton> c = JButton.class;
		for (Component component : parent.getComponents()) {
			if (Arrays.asList(component.getClass().getInterfaces()).contains(c)
					|| MyBeanUtils.getSuperClasses(component.getClass())
							.contains(c) || component.getClass().equals(c)) {
				JButton b = (JButton) component;
				b.addActionListener(action);
			}
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				addComponentsAction(jcomponent, action);
			}
		}
	}

	/**
	 * 设定JComponent中所有c类型的组件字体为font
	 * 
	 * @param <T>
	 * @param parent
	 *            父组件
	 * @param c
	 *            组件类型
	 * @param font
	 *            字体
	 */
	public static <T extends JComponent> void setComponentsFont(
			JComponent parent, Class<T> c, Font font) {
		for (Component component : parent.getComponents()) {
			if (Arrays.asList(component.getClass().getInterfaces()).contains(c)
					|| MyBeanUtils.getSuperClasses(component.getClass())
							.contains(c) || component.getClass().equals(c))
				component.setFont(font);
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				setComponentsFont(jcomponent, c, font);
			}
		}
	}

	/**
	 * 给JComponent中所有Text类型的组件添加FocusListener
	 * 
	 * @param parent
	 *            父组件
	 * @param focusListener
	 *            FocusListener
	 */
	public static void setTextAddFocusListener(JComponent parent,
			FocusListener focusListener) {
		for (Component component : parent.getComponents()) {
			if (MyBeanUtils.getSuperClasses(component.getClass()).contains(
					JTextComponent.class)
					|| component.getClass().equals(JTextComponent.class)) {
				JTextComponent text = (JTextComponent) component;
				text.addFocusListener(focusListener);
			}
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				setTextAddFocusListener(jcomponent, focusListener);
			}
		}
	}

	/**
	 * 获取一个容器类的父窗体
	 * 
	 * @param container
	 *            Container
	 * @return container的父窗体
	 */
	public static JFrame getParentFrame(Container container) {
		if (container == null)
			return null;
		if (container instanceof JFrame)
			return (JFrame) container;
		else
			return getParentFrame(container.getParent());
	}

	/**
	 * 设定parent中所有隶属于c的组件的可用状态为enabled
	 * 
	 * @param <T>
	 * @param parent
	 *            父组件
	 * @param c
	 *            组件类型
	 * @param enabled
	 *            可用状态
	 */
	public static <T extends JComponent> void setComponentState(
			JComponent parent, Class<T> c, boolean enabled) {
		if (parent == null || c == null)
			return;
		if (parent.getClass().equals(c))
			parent.setEnabled(enabled);
		for (Component component : parent.getComponents()) {
			if (Arrays.asList(component.getClass().getInterfaces()).contains(c)
					|| MyBeanUtils.getSuperClasses(component.getClass())
							.contains(c) || component.getClass().equals(c))
				component.setEnabled(enabled);
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				setComponentState(jcomponent, c, enabled);
			}
		}
	}

	/**
	 * 设定parent中所有隶属于c的组件的Border
	 * 
	 * @param <T>
	 * @param parent
	 *            父组件
	 * @param c
	 *            组件类型
	 * @param border
	 *            Border
	 */
	public static <T extends JComponent> void setComponentBorder(
			JComponent parent, Class<T> c, Border border) {
		if (parent == null || c == null)
			return;
		if (parent.getClass().equals(c))
			parent.setBorder(border);
		for (Component component : parent.getComponents()) {
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				setComponentBorder(jcomponent, c, border);
			}
		}
	}
}
