package main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.WindowUtil;

/**
 * Puzzle about dialog
 */
public class AboutDialog extends JDialog {

	/**
	 * Create about dialog
	 * 
	 * @param owner
	 *            owner of the dialog
	 */
	public AboutDialog(JFrame owner) {
		super(owner, "About Lines", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Create puzzle information label
		JLabel infoLabel = new JLabel(
				"<html><table>" + "<tr><td colspan=2 align=center><font size=5 color=#fc035e>LINES 98</font></td></tr>" 
				+ "<tr><td>Author:</td><td>LINES DEV TEAM</td><td></td></tr>" + "<tr><td>Version:</td><td>"
				+ VERSION + "</td><td></td></tr>" + "</table></html>");
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		infoPanel.add(infoLabel);
		add(infoPanel, BorderLayout.NORTH);

		final String enteredVisitLabel = "<html><a href='" + address + "'><i>" + address
				+ "</i></a></html>";
		final String exitedVisitLabel = "<html><a href='" + address + "'>" + address + "</a></html>";

		// Create my home page panel
		JPanel visitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		visitPanel.add(new JLabel("Source:"));

		JLabel visitLabel = new JLabel(exitedVisitLabel);
		visitLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// Make entered/exited animate and process my home page link clicked
		visitLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// Open my home page with default browser
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Action.BROWSE)) {
					try {
						desktop.browse(new URI(address));
					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (URISyntaxException ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				((JLabel) e.getSource()).setText(enteredVisitLabel);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				((JLabel) e.getSource()).setText(exitedVisitLabel);
			}
		});
		visitPanel.add(visitLabel);
		add(visitPanel, BorderLayout.CENTER);

		// Create panel container OK button
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		JButton okButton = new JButton("OK");
		okButton.addActionListener((e) -> {

			// Destroy about dialog
			setVisible(false);
			dispose();
		});
		buttonPanel.add(okButton);
		add(buttonPanel, BorderLayout.SOUTH);

		pack();
		WindowUtil.centerOwner(this);
		setResizable(false);
	}

	private static final String VERSION = "1.1.3";
	/**
	 * My home page address
	 */
	private static final String address = "https://github.com/herorule/DSA_Line98";
	private static final long serialVersionUID = 1L;
}
