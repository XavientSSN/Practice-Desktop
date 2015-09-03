/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssn.util;

import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.schedule.SSNScheduleListForm;
import com.ssn.schedule.SSNScheduleTagController;
import com.ssn.schedule.SSNScheduleTagPanelForm;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

public class SSNScheduleTagJTree extends JPanel {

    private static final long serialVersionUID = 1L;
    private final JTree tree;
    static JTable table = null;
    boolean showHeading;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNScheduleTagJTree.class);
    public SSNScheduleTagJTree(SSNHomeForm homeForm, SSNScheduleTagController sSNScheduleTagController) throws SQLException {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode parent = null;
        DefaultMutableTreeNode newChild;
        DefaultMutableTreeNode parentHeading = null;
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 30);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        List<SSNScheduleListForm> parentList = treeParent();
        parentHeading = new DefaultMutableTreeNode(SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ALBUM.toUpperCase() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_TITLE.toUpperCase() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_DATE.toUpperCase() + " & " + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_TIME.toUpperCase() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_END_DATE.toUpperCase() + " & " + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_TIME.toUpperCase() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_LOC.toUpperCase() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_KEY.toUpperCase());
        root.add(parentHeading);
        if (parentList.size() > 0) {
            for (SSNScheduleListForm item : parentList) {

                parent = new DefaultMutableTreeNode(item.getSsnAlbum() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + item.getSsnTitle() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + item.getSsnStrfromDate() + "  " + item.getSsnStartTime() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + item.getSsnStrtoDate() + "  " + item.getSsnEndTime() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + item.getSsnLocation() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + item.getSsnkeyWords());

                List<SSNScheduleListForm> childList = treeChild(item.getTagId());
                for (SSNScheduleListForm itemChild : childList) {
                    newChild = new DefaultMutableTreeNode(itemChild.getSsnAlbum() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + itemChild.getSsnSubtags() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + itemChild.getSsnStrfromDate() + "  " + itemChild.getSsnStartTime() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + itemChild.getSsnStrtoDate() + "  " + itemChild.getSsnEndTime() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + itemChild.getSsnLocation() + SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR + itemChild.getSsnkeyWords());
                    parent.add(newChild);
                }
                root.add(parent);
            }
        }
        tree = new JTree(root);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        
        tree.addTreeSelectionListener(new SelectionListener(sSNScheduleTagController));
        tree.setRowHeight(0);
        tree.setCellRenderer(new MyTableInTreeCellRenderer(root, parent, homeForm));
        
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        tree.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        MyTableInTreeCellRenderer renderer =(MyTableInTreeCellRenderer) tree.getCellRenderer();
        renderer.setTextSelectionColor(SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
        
        jsp.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        add(tree, BorderLayout.CENTER);
        tree.setSize(jsp.getWidth(), jsp.getHeight());
        tree.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
    }

    private static void setWidthAsPercentages(JTable table, SSNHomeForm homeForm,
            double... percentages) {
        double factor = 0;

        int scrWid = homeForm.getSsnHomeCenterPanel().getWidth();
       
        if(scrWid>864)
         factor = 864;  
        else
        factor = scrWid;

        TableColumnModel model = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
            int val = (int) (percentages[columnIndex] * factor * 1.0);
            TableColumn column = model.getColumn(columnIndex);
            column.setPreferredWidth(val);
        }
    }

    final List<SSNScheduleListForm> treeParent() throws SQLException {
        SSNScheduleTagEventListModel eventListModelObj = new SSNScheduleTagEventListModel();
        List<SSNScheduleListForm> eventListModelList = eventListModelObj.getParentEventList();
        return eventListModelList;
    }

    final List<SSNScheduleListForm> treeChild(String title) throws SQLException {
        SSNScheduleTagEventListModel eventListModelObj = new SSNScheduleTagEventListModel();
        List<SSNScheduleListForm> eventListModelList = eventListModelObj.getEventListChild(title);
        return eventListModelList;
    }

    final List<SSNScheduleListForm> treeChild(int tagId) throws SQLException {
        SSNScheduleTagEventListModel eventListModelObj = new SSNScheduleTagEventListModel();
        List<SSNScheduleListForm> eventListModelList = eventListModelObj.getEventListChild(tagId);
        return eventListModelList;
    }

    class MyTableInTreeCellRenderer extends JPanel implements TreeCellRenderer {

        private SSNHomeForm homeForm;

        public MyTableInTreeCellRenderer(DefaultMutableTreeNode root, DefaultMutableTreeNode parent, SSNHomeForm form) {

            super(new BorderLayout());
            table = new JTable();
            table.setBorder(BorderFactory.createLineBorder(Color.yellow));
            JPanel panel = new JPanel();
            panel.add(table);
            panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            add(panel);
            this.homeForm = form;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, final int row, boolean hasFocus) {

            String tableData = ((DefaultMutableTreeNode) value).getUserObject().toString();
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setGridColor(new Color(215, 255, 0));
            table.getTableHeader().setVisible(true);
            table.setForeground(SSNConstants.SSN_WHITE_BACKGROUND_COLOR);
            table.setRowHeight(25);
            
            final String[] params = tableData.split(SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR);

            table.setModel(new DefaultTableModel(params, row) {
                private static final long serialVersionUID = 1L;

                @Override
                public int getRowCount() {
                    table.getTableHeader().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                    return 1;
                }

                @Override
                public int getColumnCount() {
                    return params.length;
                }

                @Override
                public Object getValueAt(int row, int column) {
                    table.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                    return params[column];
                }

            });
            setWidthAsPercentages(table, homeForm, 0.10, 0.15, 0.18, 0.18, 0.10, 0.15);

            if (params[2].toUpperCase().contains(SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_DATE.toUpperCase())) {
                table.setFont(new Font("Verdana", Font.BOLD, 12));
            } else {
                table.setFont(new Font("Verdana", Font.PLAIN, 12));
            }

            return this;
        }
        

        private void setTextSelectionColor(Color SSN_TOOLBAR_WHITE_FONT_COLOR) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             tree.setBackground(Color.red);
        }
    }

}

class SelectionListener implements TreeSelectionListener {

    private SSNScheduleTagController sSNScheduleTagController;

    public SelectionListener(SSNScheduleTagController sSNScheduleTagController) {
        this.sSNScheduleTagController = sSNScheduleTagController;
        setsSNScheduleTagController(this.sSNScheduleTagController);
    }

    @Override
    public void valueChanged(TreeSelectionEvent se) {
        SSNScheduleTagController.preventOverlap = false;
        JTree tree = (JTree) se.getSource();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        
        if (selectedNode != null) {
            getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagTitleTxt().setEditable(true);
            getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagSaveBtn().setText("Save");
            try {
                String selectedNodeName = selectedNode.toString();
                SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
                String nodeItem[] = selectedNodeName.split(SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR);
                if (!nodeItem[2].toUpperCase().contains(SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_DATE.toUpperCase())) {
                    SSNScheduleListForm sSNScheduleListForm = SSNDao.getScheduledTag(selectedNodeName);
                    if(sSNScheduleListForm != null)
                    {
                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnTagIdLabel().setText("" + sSNScheduleListForm.getTagId());

                    SSNHomeForm.tagId = sSNScheduleListForm.getTagId() != null ? sSNScheduleListForm.getTagId() : 0;
                    SSNHomeForm.subTagId = sSNScheduleListForm.getSubTagId() != null ? sSNScheduleListForm.getSubTagId() : 0;
                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagAlbumCombo().setSelectedItem(sSNScheduleListForm.getSsnAlbum());
                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagTitleTxt().setText(sSNScheduleListForm.getSsnTitle());
                    if (!nodeItem[2].trim().equalsIgnoreCase(SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_DATE + " & " + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_TIME)) {
                        getsSNScheduleTagController().getScheduleTagPanelFormMerger().getStartDateChooser().setDate(sdf.parse(sSNScheduleListForm.getSsnStrfromDate()));
                        SSNScheduleTagPanelForm.setsDate(sdf.parse(sSNScheduleListForm.getSsnStrfromDate()));
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        getsSNScheduleTagController().getScheduleTagPanelFormMerger().getStartTimeSpinner().setValue(format.parseObject(sSNScheduleListForm.getSsnStartTime()));
                    }
                    if (!nodeItem[3].trim().equalsIgnoreCase(SSNConstants.SSN_SCHEDULAR_TAG_HEAD_END_DATE + " & " + SSNConstants.SSN_SCHEDULAR_TAG_HEAD_ST_TIME)) {
                        getsSNScheduleTagController().getScheduleTagPanelFormMerger().getEndDateChooser().setDate(sdf.parse(sSNScheduleListForm.getSsnStrtoDate()));
                        SSNScheduleTagPanelForm.seteDate(sdf.parse(sSNScheduleListForm.getSsnStrtoDate()));
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        getsSNScheduleTagController().getScheduleTagPanelFormMerger().getEndTimeSpinner().setValue(format.parseObject(sSNScheduleListForm.getSsnEndTime()));

                    }
                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagLocationTxt().setText(sSNScheduleListForm.getSsnLocation());

                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagKeywordTxt().setText(sSNScheduleListForm.getSsnkeyWords());
                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagVDOFileNameTxt().setText(sSNScheduleListForm.getSsnVideoPrefix());
                    getsSNScheduleTagController().getScheduleTagPanelFormMerger().getSsnScheduleTagFileNameTxt().setText(sSNScheduleListForm.getSsnImagePrefix());

                    if (selectedNode.getLevel() == 1) {
                        getsSNScheduleTagController().getScheduleTagPanelFormMerger().enableActionPanel(SSNConstants.SSN_SCHEDULAR_PARENT_TAG);
                        SSNScheduleTagPanelForm.setGlobalParentTitleName(nodeItem[1]);
                        SSNScheduleTagPanelForm.setGlobalParentAlbumName(nodeItem[0]);

                    } else if (selectedNode.getLevel() == 2) {
                        getsSNScheduleTagController().getScheduleTagPanelFormMerger().enableActionPanel(SSNConstants.SSN_SCHEDULAR_CHILD_TAG);
                        SSNScheduleTagPanelForm.setGlobalParentTitleName(nodeItem[1]);
                    }
                    }
                    
                }
            } catch (ParseException ex) {
                Logger.getLogger(SelectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public SSNScheduleTagController getsSNScheduleTagController() {
        return sSNScheduleTagController;
    }

    private void setsSNScheduleTagController(SSNScheduleTagController sSNScheduleTagController) {
        this.sSNScheduleTagController = sSNScheduleTagController;
    }
}
