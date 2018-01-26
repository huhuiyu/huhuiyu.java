package cn.huhuiyu.frame;

import javax.swing.JFrame;

public interface RunCallBack<T extends JFrame> {
	public void back(T frame);
}
