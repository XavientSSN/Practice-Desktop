
package com.ssn.listener;


import com.ssn.helper.SSNHelper;
import com.ssn.helper.SSNNotificationHelper;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNFileNode;
import com.ssn.ui.custom.component.SSNTreeHelper;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;

/**
 *
 * @author pkumar2
 */
public class SSNDirExpansionListener implements TreeExpansionListener{
    private SSNTreeHelper treeHelper;
    private SSNFileExplorer fileTree;
    static Logger logger = Logger.getLogger(SSNDirExpansionListener.class);
   
   public SSNDirExpansionListener(SSNFileExplorer fileTree) {
        this.treeHelper=new SSNTreeHelper();
        this.fileTree=fileTree;
   }
   
 // Make sure expansion is threaded and updating the tree model

  // only occurs within the event dispatching thread.

 
    
   public void treeExpanded(TreeExpansionEvent event) {
       
       

      final DefaultMutableTreeNode node = treeHelper.getTreeNode(event.getPath());

      final SSNFileNode fnode = treeHelper.getFileNode(node);

      Thread runner = new Thread() {

        public void run() {

          if (fnode != null && fnode.expand(node)) {

            Runnable runnable = new Runnable() {

              public void run() {

                fileTree.m_model.reload(node);

              }

            };

            SwingUtilities.invokeLater(runnable);

          }

        }

      };

      runner.start();
      fileTree.m_tree.setSelectionRow(1);
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        fileTree.m_tree.setSelectionPath(null);
    }
    
}
