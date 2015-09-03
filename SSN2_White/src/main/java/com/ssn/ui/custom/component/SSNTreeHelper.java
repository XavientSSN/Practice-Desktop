
package com.ssn.ui.custom.component;


import com.ssn.ui.custom.component.SSNFileNode;
import com.ssn.ui.custom.component.SSNIconData;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author pkumar2
 */
public class SSNTreeHelper {
    
    
    public SSNTreeHelper()
    {
    }
    
    public DefaultMutableTreeNode getTreeNode(TreePath path) {

    return (DefaultMutableTreeNode) (path.getLastPathComponent());

  }

  public SSNFileNode getFileNode(DefaultMutableTreeNode node) {

    if (node == null)

      return null;

    Object obj = node.getUserObject();

    if (obj instanceof SSNIconData)

      obj = ((SSNIconData)obj).getObject();

    if (obj instanceof SSNFileNode)

      return (SSNFileNode)obj;

    else

      return null;

  }
}
