package com.redpois0n.gscrot.ui;

import iconlib.IconUtils;

import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import nativewindowlib.WindowsUtils;
import nativewindowlib.WindowsUtils.NativeWindow;

import com.redpois0n.gscrot.BinaryImageProcessor;
import com.redpois0n.gscrot.CaptureUploader;
import com.redpois0n.gscrot.Config;
import com.redpois0n.gscrot.GraphicsImageProcessor;
import com.redpois0n.gscrot.ImageProcessor;
import com.redpois0n.gscrot.Main;
import com.redpois0n.gscrot.ScreenshotHelper;
import com.redpois0n.gscrot.ui.components.CaptureUploaderCheckBoxMenuItem;
import com.redpois0n.gscrot.ui.components.ImageProcessorCheckBoxMenuItem;
import com.redpois0n.gscrot.ui.settings.FrameSettings;
import com.redpois0n.oslib.OperatingSystem;

public class GlobalPopupMenu {
	
	public static final List<JMenuItem> SETTINGS_ITEMS = new ArrayList<JMenuItem>();
	
	public static JPopupMenu getPopupMenu() {
	    JPopupMenu popup = new JPopupMenu();
	    
	    // Capture category
	    JMenu mnCapture = new JMenu("Capture");
	    mnCapture.setIcon(IconUtils.getIcon("camera"));
	    
	    JMenuItem mntmRegion = new JMenuItem("Region", IconUtils.getIcon("region-select"));
	    mntmRegion.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try {
					Main.createBackground();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	    	}
	    });
	    mnCapture.add(mntmRegion);
	    
	    // All monitors
	    JMenuItem mntmAllMonitors = new JMenuItem("All Monitors");
	    mntmAllMonitors.setIcon(IconUtils.getIcon("monitor-all"));
	    
	    mntmAllMonitors.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try {
					ScreenshotHelper.process(ScreenshotHelper.capture(ScreenshotHelper.getWholeDesktop()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
    		}
    	});
	    
	    mnCapture.add(mntmAllMonitors);
	    
	    // Monitor item
	    JMenu mntmMonitor = new JMenu("Monitor");
	    mntmMonitor.setIcon(IconUtils.getIcon("monitor"));
	    
	    GraphicsDevice[] devices = ScreenshotHelper.getScreens();
	    
	    JMenuItem mntmPickMonitor = new JMenuItem("Pick Monitor...", IconUtils.getIcon("monitor-select"));
	    mntmPickMonitor.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			new MonitorPicker().setVisible(true);
    		}
    	});
	    mntmMonitor.add(mntmPickMonitor);
	    mntmMonitor.addSeparator();
	    
	    for (int i = 0; i < devices.length; i++) {
	    	final GraphicsDevice device = devices[i];
	    	
	    	JMenuItem item = new JMenuItem("Monitor " + i, IconUtils.getIcon("monitor"));
	    	
	    	item.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			ScreenshotHelper.captureScreen(device);
	    		}
	    	});
	    	
	    	mntmMonitor.add(item);
	    }
	    
	    mnCapture.add(mntmMonitor);
	    
	    popup.add(mnCapture);  
	    
	    if (OperatingSystem.getOperatingSystem().getType() == OperatingSystem.WINDOWS) {
	    	// Windows item
		    JMenu mntmWindows = new JMenu("Window");
		    mntmWindows.setIcon(IconUtils.getIcon("window"));
		    
		    List<NativeWindow> windows = WindowsUtils.getWindows();
		    
		    for (final NativeWindow window : windows) {	    	
		    	if (window.getTitle().length() > 0) {
		    		JMenuItem item = new JMenuItem(window.getTitle(), window.getIcon());
			    	
			    	item.addActionListener(new ActionListener() {
			    		public void actionPerformed(ActionEvent e) {
			    			try {
								ScreenshotHelper.process(ScreenshotHelper.capture(window));
							} catch (Exception ex) {
								ex.printStackTrace();
							}
			    		}
			    	});
			    	
			    	mntmWindows.add(item);
		    	}
		    }
		    
		    mnCapture.add(mntmWindows);
	    }
	    
	    popup.add(new JSeparator(JSeparator.HORIZONTAL));

	    // Image uploaders
	    ButtonGroup group = new ButtonGroup();
	    
	    JMenu mnImageUploaders = new JMenu("Image Uploaders");
	    mnImageUploaders.setIcon(IconUtils.getIcon("drive-upload"));
	    	    
	    CaptureUploader selectedUploader = CaptureUploader.getSelected();
	    for (CaptureUploader uploader : CaptureUploader.getAllUploaders()) {
	    	CaptureUploaderCheckBoxMenuItem mntmUploader = new CaptureUploaderCheckBoxMenuItem(uploader);
	    	mntmUploader.setIcon(uploader.getIcon());
	    	
	    	mntmUploader.setSelected(uploader == selectedUploader);

		    mnImageUploaders.add(mntmUploader);
		    
		    group.add(mntmUploader);
	    }
	    
	    popup.add(mnImageUploaders);
	    
	    // Image processors
	    	    
	    JMenu mnImageProcessors = new JMenu("Image Processors");
	    mnImageProcessors.setIcon(IconUtils.getIcon("image-processor"));
	    	    
	    for (BinaryImageProcessor processor: ImageProcessor.getBinaryProcessors()) {
	    	ImageProcessorCheckBoxMenuItem mntmProcessor = new ImageProcessorCheckBoxMenuItem(processor);
	    	mntmProcessor.setIcon(processor.getIcon());
	    	mnImageProcessors.add(mntmProcessor);
	    }
	    
	    for (GraphicsImageProcessor processor: ImageProcessor.getGraphicsProcessors()) {
	    	ImageProcessorCheckBoxMenuItem mntmProcessor = new ImageProcessorCheckBoxMenuItem(processor);
	    	mntmProcessor.setIcon(processor.getIcon());
	    	mnImageProcessors.add(mntmProcessor);
	    }
	    
	    popup.add(mnImageProcessors);
	    
	    popup.add(new JSeparator(JSeparator.HORIZONTAL));
	    
	    // After upload    
	    JMenu mnAfterCapture = new JMenu("After Capture");
	    mnAfterCapture.setIcon(IconUtils.getIcon("after-capture"));
	    	    
	    JCheckBoxMenuItem mntmImageClipboard = new JCheckBoxMenuItem("Copy Image to clipboard");
	    mntmImageClipboard.setSelected(Config.get(Config.KEY_COPY_IMAGE_TO_CLIPBOARD, "true").equalsIgnoreCase("true"));
	    mntmImageClipboard.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		boolean b = ((JCheckBoxMenuItem) e.getSource()).isSelected();
	    		
	    		Config.put(Config.KEY_COPY_IMAGE_TO_CLIPBOARD, b + "");
	    	}
	    });
	    mnAfterCapture.add(mntmImageClipboard);
	    popup.add(mnAfterCapture);
	    	    
	    // After upload    
	    JMenu mnAfterUpload = new JMenu("After Upload");
	    mnAfterUpload.setIcon(IconUtils.getIcon("after-upload"));
	    
	    JCheckBoxMenuItem mntmURLClipboard = new JCheckBoxMenuItem("Copy URL to clipboard");
	    mntmURLClipboard.setSelected(Config.get(Config.KEY_COPY_URL_TO_CLIPBOARD, "true").equalsIgnoreCase("true"));
	    mntmURLClipboard.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		boolean b = ((JCheckBoxMenuItem) e.getSource()).isSelected();
	    		
	    		Config.put(Config.KEY_COPY_URL_TO_CLIPBOARD, b + "");
	    	}
	    });
	    mnAfterUpload.add(mntmURLClipboard);
	    
	    popup.add(mnAfterUpload);
	    popup.add(new JSeparator(JSeparator.HORIZONTAL));
	    
	    // Settings item
	    JMenu mnSettings = new JMenu("Settings");
	    mnSettings.setIcon(IconUtils.getIcon("settings"));

	    JMenuItem mntmApplicationSettings = new JMenuItem("Application Settings", IconUtils.getIcon("settings"));
	    mntmApplicationSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new FrameSettings().setVisible(true);
			} 	
	    });
	    
	    mnSettings.add(mntmApplicationSettings);
	    
	    if (SETTINGS_ITEMS.size() > 0) {
	    	mnSettings.addSeparator();
	    	for (JMenuItem item : SETTINGS_ITEMS) {
	    		mnSettings.add(item);
	    	}
	    }
	    	    
	    popup.add(mnSettings);
	    
	    popup.add(new JSeparator(JSeparator.HORIZONTAL));
	    
	    popup.add(Box.createVerticalGlue());
	    
	    // Exit item
	    JMenuItem mntmExit = new JMenuItem("Exit", IconUtils.getIcon("cross"));
	    mntmExit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	    popup.add(mntmExit);
	    
	    return popup;
	}

}
