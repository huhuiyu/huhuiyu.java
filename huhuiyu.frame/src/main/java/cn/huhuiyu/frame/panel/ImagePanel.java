package cn.huhuiyu.frame.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 可以设置背景图片的JPanel
 * 
 * @author huhuiyu
 * 
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -8617675898391383624L;
	private Image image;
	private boolean fill = true;

	public ImagePanel() {
		super();
	}

	/**
	 * 图片是否为填充方式
	 * 
	 * @return 图片是否为填充方式
	 */
	public boolean isFill() {
		return fill;
	}

	/**
	 * 设定图片是否为填充方式
	 * 
	 * @param fill
	 *            图片是否为填充方式
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}

	/**
	 * 设置图片内容
	 * 
	 * @param imageBytes
	 *            二进制图片数据
	 */
	public void setImage(byte[] imageBytes) {
		image = new ImageIcon(imageBytes).getImage();
		updateImage();
	}

	/**
	 * 设置图片内容
	 * 
	 * @param imageFile
	 *            图片文件
	 */
	public void setImage(File imageFile) {
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		updateImage();
	}

	/**
	 * 设置图片内容
	 * 
	 * @param imageStream
	 *            图片的二进制流
	 */
	public void setImage(InputStream imageStream) {
		try {
			image = ImageIO.read(imageStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		updateImage();
	}

	/**
	 * 设置图片内容
	 * 
	 * @param imageUrl
	 *            图片的url
	 */
	public void setImage(URL imageUrl) {
		try {
			image = ImageIO.read(imageUrl);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		updateImage();
	}

	/**
	 * 更新图片信息
	 */
	private void updateImage() {
		if (image == null) {
			return;
		}
		Dimension d = new Dimension();
		d.setSize(image.getWidth(this), image.getHeight(this));
		this.setPreferredSize(d);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		if (fill) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		} else {
			g.drawImage(image, 0, 0, image.getWidth(this),
					image.getHeight(this), this);
		}
	}
}
