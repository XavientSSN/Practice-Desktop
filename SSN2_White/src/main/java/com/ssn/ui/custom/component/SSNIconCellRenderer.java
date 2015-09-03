package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author pkumar2
 */
public class SSNIconCellRenderer extends JLabel implements TreeCellRenderer

{

  protected Color m_textSelectionColor;

  protected Color m_textNonSelectionColor;

  protected Color m_bkSelectionColor;

  protected Color m_bkNonSelectionColor;

  protected Color m_borderSelectionColor;

  protected boolean m_selected;
private Border border = BorderFactory.createEmptyBorder ( 3, 3, 3, 3 );
  public SSNIconCellRenderer() {

    super();

    m_textSelectionColor = UIManager.getColor(Color.WHITE);

    m_textNonSelectionColor = UIManager.getColor(Color.WHITE);

    m_bkSelectionColor = UIManager.getColor(

      Color.WHITE);

    m_bkNonSelectionColor = UIManager.getColor(

      Color.WHITE);

    m_borderSelectionColor = UIManager.getColor(

      Color.WHITE);

    setOpaque(false);

  }

  public Component getTreeCellRendererComponent(JTree tree,

   Object value, boolean sel, boolean expanded, boolean leaf,

   int row, boolean hasFocus)        

  {

    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

    Object obj = node.getUserObject();

    setText(obj.toString());
    
    setForeground(SSNConstants.SSN_WHITE_FONT_COLOR); 
      setBorder(border);
    if (obj instanceof Boolean)

      setText("Retrieving data...");

    if (obj instanceof SSNIconData) {

      SSNIconData idata = (SSNIconData)obj;

      if (expanded)

        setIcon(idata.getExpandedIcon());

      else

        setIcon(idata.getIcon());

    }

    else

    //  setIcon(null);

    //setFont(tree.getFont());

    //setForeground(sel ? m_textSelectionColor :

    //  m_textNonSelectionColor);

   // setBackground(sel ? m_bkSelectionColor :

    //  m_bkNonSelectionColor);

    m_selected = sel;

    return this;

  }

  public void paintComponent(Graphics g) {

    Color bColor = getBackground();

    Icon icon = getIcon();

    //g.setColor(bColor);

    int offset = 0;

    if(icon != null && getText() != null)

      offset = (icon.getIconWidth() + getIconTextGap());

   // g.fillRect(offset, 0, getWidth() - 1 - offset,

     // getHeight() - 1);

    if (m_selected) {

      g.setColor(m_borderSelectionColor);

      g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);

    }

    super.paintComponent(g);

  }
}

