package com.bigcan.quartz;

import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Java托盘菜单
 */
public class SystemUi extends JFrame {

	private static final long serialVersionUID = 1L;

	/** 系统托盘 */
	private SystemTray systemTray;

	/** 托盘图标 */
	private TrayIcon trayIcon;

	public SystemUi() throws Exception {
		setTitle("About");
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(new JLabel("程序开发：BigCan工作室-王麟腾"));
		setSize(360, 120);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		if (SystemTray.isSupported()) { // 当前平台是否支持系统托盘
			// 创建一个右键弹出菜单
			PopupMenu popupMenu = new PopupMenu();
			MenuItem mi = new MenuItem("About");
			MenuItem exit = new MenuItem("Exit");
			popupMenu.add(mi);
			popupMenu.add(exit);
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(true);

				}

			});
			exit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			ClassLoader cl = this.getClass().getClassLoader();
			URL url = cl.getResource("icon.png");
			ImageIcon icon = new ImageIcon(url);
			// 创建托盘图标
			trayIcon = new TrayIcon(icon.getImage(), "联阳微信提醒", popupMenu);
			// 获取托盘菜单
			systemTray = SystemTray.getSystemTray();
			// 添加托盘图标
			systemTray.add(trayIcon);
		}
	}
}