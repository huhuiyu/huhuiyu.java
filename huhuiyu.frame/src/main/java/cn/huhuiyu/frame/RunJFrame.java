package cn.huhuiyu.frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunJFrame<T extends JFrame> implements Runnable {
	private static Logger logger = LogManager.getLogger(RunJFrame.class);
	private Class<T> frame;
	private boolean moveToScreenCenter;
	private RunCallBack<T> runBack;

	private transient T f;

	public RunJFrame(Class<T> frame) {
		this.frame = frame;
	}

	public RunJFrame(Class<T> frame, RunCallBack<T> runBack) {
		this.frame = frame;
		this.runBack = runBack;
	}

	public RunJFrame(Class<T> frame, boolean moveToScreenCenter) {
		this.frame = frame;
		this.moveToScreenCenter = moveToScreenCenter;
	}

	public RunJFrame(Class<T> frame, boolean moveToScreenCenter, RunCallBack<T> runBack) {
		this.frame = frame;
		this.moveToScreenCenter = moveToScreenCenter;
		this.runBack = runBack;
	}

	public boolean isMoveToScreenCenter() {
		return moveToScreenCenter;
	}

	public void setMoveToScreenCenter(boolean moveToScreenCenter) {
		this.moveToScreenCenter = moveToScreenCenter;
	}

	public RunCallBack<T> getRunBack() {
		return runBack;
	}

	public void setRunBack(RunCallBack<T> runBack) {
		this.runBack = runBack;
	}

	public void setFrame(Class<T> frame) {
		this.frame = frame;
	}

	@Override
	public void run() {
		logger.debug(frame.getName() + "开始加载!");
		try {
			f = (T) frame.newInstance();
			if (moveToScreenCenter) {
				FrameUtil.moveFrameToCenter(f);
			}
			if (runBack != null) {
				runBack.back(f);
			}
			f.setVisible(true);
			logger.debug(frame.getName() + "加载完毕!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "错误！", JOptionPane.ERROR_MESSAGE);
		}
	}

	public T getFrame() {
		while (f == null) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				logger.debug("获取窗体" + frame.getName() + "失败！", e);
			}
		}
		return f;
	}

}
