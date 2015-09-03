package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.*;

public class SSNCustomBorder implements Border {
	protected int m_w = SSNConstants.DEFAULT_TEXT_FIELD_WIDTH;
	protected int m_h = SSNConstants.DEFAULT_TEXT_FIELD_HEIGHT;
        
        // Do we want rounded corners on the border?
	protected boolean roundc = false; 
	protected Color backgroundColor;

	public SSNCustomBorder(boolean round_corners, Color fieldBackgroundColor, int height, int width) {
		roundc = round_corners;
		this.backgroundColor = fieldBackgroundColor;
		if(width != 0){
			m_w = width;
			m_h = height;
		}
	}
        
        public SSNCustomBorder(boolean round_corners,  int height, int width) {
		roundc = round_corners;
		if(width != 0){
			m_w = width;
			m_h = height;
		}
                
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(m_h, m_w, m_h, m_w);
		//return new Insets(c.getHeight(),c.getWidth(),c.getHeight(),c.getWidth());
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		w = w - 3;
		h = h - 3;
		x++;
		y++;
		//c.setBackground(backgroundColor);
		
		Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(SSNConstants.TEXT_FIELD_BORDER_COLOR);
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(4,
                BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
		
		// Rounded corners
		if (roundc) {
			// diagonal
			g2d.setColor(SSNConstants.TEXT_FIELD_BORDER_COLOR);
			g2d.drawRoundRect(x, y, w, h, 30, 30);
                        //g2d.fillRoundRect(x, y, w, h, 30, 30);
			
		}
		// Square corners
		else {
			g.setColor(new Color(243,165,9));
			g.drawLine(x, y, x, y + h);
			g.drawLine(x, y, x + w, y);
			g.setColor(new Color(243,165,9));
			g.drawLine(x + w, y, x + w, y + h);
			g.drawLine(x, y + h, x + w, y + h);
		}
	}
	
	
	
}
