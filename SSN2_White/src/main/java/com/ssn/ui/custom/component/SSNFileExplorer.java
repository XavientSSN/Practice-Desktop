package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.helper.SSNHelper;
import com.ssn.listener.SSNDirExpansionListener;
import com.ssn.listener.SSNDirSelectionListener;
import com.ssn.listener.SSNFacebookAlbumSelectionListener;
import com.ssn.listener.SSNInstagramSelectionListener;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.service.SSNSubscriptionDetails;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Album;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.MediaOperations;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

/**
 *
 * @author pkumar2
 */
public class SSNFileExplorer extends JPanel {

    public static final ImageIcon ICON_COMPUTER = new ImageIcon("computer.gif");

    public static final ImageIcon ICON_DISK = new ImageIcon("disk.gif");

    public static final ImageIcon ICON_FOLDER = new ImageIcon("folder.gif");

    public static final ImageIcon ICON_EXPANDEDFOLDER = new ImageIcon("expandedfolder.gif");

    public JTree m_tree;

    public DefaultTreeModel m_model;

    public JTextField m_display;

    private SSNHomeForm homeForm = null;

    private String selectedFolder;

    public SSNDirSelectionListener ssnDirSelectionListener = null;
    public Logger logger = Logger.getLogger(SSNFileExplorer.class);

    public SSNFileExplorer(SSNHomeForm homeForm) {
         ///ProgressEx.show();
        try {

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        JPanel treeContainer = new JPanel(new BorderLayout());

        //super("Directories Tree");
        setSize(new Dimension(200, 300));
        setLayout(new BorderLayout());
        setHomeForm(homeForm);
        //DefaultMutableTreeNode top = new DefaultMutableTreeNode(new SSNIconData(new ImageIcon(getClass().getResource("/images/ssn-hive-drive.png")), null, "OurHive"));
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new SSNIconData(null, null, "OURHIVE"));
      
        DefaultMutableTreeNode node;

        File theDir = new File(SSNHelper.getSsnHiveDirPath());

        ImageIcon imgIcon = new ImageIcon(getClass().getResource("/images/ssn-hive-folder.png"));

        if (theDir.exists()) {
            File[] roots = theDir.listFiles();
            for (int k = 0; k < roots.length; k++) {
                File rootFile = roots[k];
                if (rootFile.getName().equalsIgnoreCase("OurHive")) {
                    node = new DefaultMutableTreeNode(new SSNIconData(imgIcon, null, new SSNFileNode(rootFile)));
                    top.add(node);
                }
            }

            for (int k = 0; k < roots.length; k++) {
                File rootFile = roots[k];
                if (!rootFile.getName().equalsIgnoreCase("OurHive")) {
                    node = new DefaultMutableTreeNode(new SSNIconData(imgIcon, null, new SSNFileNode(rootFile)));
                    top.add(node);
                }
            }
        }
            m_model = new DefaultTreeModel(top);

        m_tree = new JTree(m_model);

        m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        TreeCellRenderer renderer = new SSNIconCellRenderer();

        m_tree.setCellRenderer(renderer);

        m_tree.addTreeExpansionListener(new SSNDirExpansionListener(this));

        ssnDirSelectionListener = new SSNDirSelectionListener(this, getHomeForm());
        getHomeForm().setSsnDirSelectionListener(ssnDirSelectionListener);

        m_tree.addTreeSelectionListener(ssnDirSelectionListener);

        m_tree.setShowsRootHandles(true);

        m_tree.setEditable(false);
        m_tree.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        m_tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (((event.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
                        && (m_tree.getSelectionCount() > 0)) {
                    showMenu(event.getX(), event.getY());
                }
            }
        });
        Border paddingBorder = BorderFactory.createEmptyBorder(-5, 10, 10, 10);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
//	
        File rootFile = new File(SSNHelper.getSsnHiveDirPath());

        double size = rootFile.list().length;

        double ratio = 1.0;
        if (size > 12) {
            ratio = 45.0 / size;

        }
        m_tree.setForeground(Color.WHITE);
        JScrollPane s = new JScrollPane();

        s.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

        s.getVerticalScrollBar().setUI(new SSNMyScrollbarUI(ratio, true));
        s.getHorizontalScrollBar().setUI(new SSNHorizontalScrollbarUI());
        Border borderVer = BorderFactory.createEmptyBorder(0, 0, 0, 2);
        s.setBorder(borderVer);
        treeContainer.add(m_tree, BorderLayout.NORTH);

        m_display = new JTextField();

        m_display.setEditable(false);
        m_display.setVisible(false);

        this.getHomeForm().setHiveTree(m_tree);
        add(m_display, BorderLayout.NORTH);

        WindowListener wndCloser = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {

                System.exit(0);

            }

        };

        //addWindowListener(wndCloser);
        this.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        treeContainer.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        addFacebookExplorer(treeContainer);
        addInstagramExplorer(treeContainer);
        s.getViewport().add(treeContainer);
        add(s, BorderLayout.CENTER);
        showCloudeUsage();
        setVisible(true);

    }

    protected void showMenu(int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem mi = new JMenuItem("Delete");
        mi.setFont(new Font("open sans", Font.BOLD, 12));
        mi.setBackground(new Color(232, 231, 230));
        mi.setForeground(new Color(134, 132, 133));

        TreePath path = m_tree.getSelectionPath();
        final Object node = path.getLastPathComponent();
        if (node == m_tree.getModel().getRoot()) {
            mi.setEnabled(false);
        }
        popup.add(mi);
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Object source = event.getSource();
                if (node.toString().equalsIgnoreCase("OurHive")) {
                    SSNMessageDialogBox message = new SSNMessageDialogBox();
                    message.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "You can not delete default album !");
                } else {
                    SSNConfirmationDialogBox deleteConfirmation = new SSNConfirmationDialogBox();
                    deleteConfirmation.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirmation : ", "", "Are you sure to delete the selected Album ?");
                    int result = deleteConfirmation.getResult();
                    if (result == JOptionPane.YES_OPTION) {
                        deleteSelectedItems();
                        getHomeForm().getHomeModel().deletePhotos();
                    }
                }
            }
        });
        popup.show(m_tree, x, y);
    }

    protected void deleteSelectedItems() {
        DefaultMutableTreeNode node;
        DefaultTreeModel model = (DefaultTreeModel) (m_tree.getModel());
        TreePath[] paths = m_tree.getSelectionPaths();

        for (int i = 0; i < paths.length; i++) {
            node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
            model.removeNodeFromParent(node);
        }
    }

    private void addFacebookExplorer(JPanel treeContainer) {
        if (getHomeForm().getFacebookAccessGrant() != null ) {
            if(isAurthorized()){
                AccessGrant accessGrant = getHomeForm().getFacebookAccessGrant();
                FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(
                        SSNConstants.SSN_FACEBOOK_API_KEY, SSNConstants.SSN_FACEBOOK_SECRET_KEY);
                Connection<Facebook> facebookConnection = connectionFactory.createConnection(accessGrant);
                Facebook facebook = facebookConnection.getApi();
                MediaOperations mediaOperations = facebook.mediaOperations();
                List<Album> albums = mediaOperations.getAlbums();

                DefaultMutableTreeNode top = new DefaultMutableTreeNode(new SSNIconData(new ImageIcon(getClass().getResource("/images/facebook.png")), null, "Facebook"));
                 if (albums.size() > 0) {
                    Collections.sort(albums, new Comparator<Album>() {
                       @Override
                       public int compare(final Album object1, final Album object2) {
                           return object1.getName().compareTo(object2.getName());
                       }
                      } );
                  }
                for (Album album : albums) {
                    DefaultMutableTreeNode albumNode = new DefaultMutableTreeNode(new SSNIconData(new ImageIcon(getClass().getResource("/images/ssn-hive-folder.png")), null, new SSNAlbumNode(album)));;
                    top.add(albumNode);
                }

                DefaultTreeModel treeModel = new DefaultTreeModel(top);
                JTree facebookTree = new JTree(treeModel);

                facebookTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                facebookTree.putClientProperty("JTree.lineStyle", "Angled");
                TreeCellRenderer renderer = new SSNIconCellRenderer();
                facebookTree.setCellRenderer(renderer);
                facebookTree.setShowsRootHandles(true);
                facebookTree.setEditable(false);
                facebookTree.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                facebookTree.setForeground(Color.WHITE);

                SSNFacebookAlbumSelectionListener albumSelectionListener = new SSNFacebookAlbumSelectionListener(this,
                        getHomeForm(), facebook);
                facebookTree.addTreeSelectionListener(albumSelectionListener);

                Border border = BorderFactory.createEmptyBorder(2, 0, 0, 0);
                facebookTree.setBorder(border);

                treeContainer.add(facebookTree, BorderLayout.SOUTH);
                this.getHomeForm().setFacebookTree(facebookTree);
                }
        }
    }

    private void addInstagramExplorer(JPanel treeContainer) {

        if (getHomeForm().getInstagramAccessGrant() != null) {
            try {
                AccessGrant accessGrant = getHomeForm().getInstagramAccessGrant();

                DefaultMutableTreeNode top = new DefaultMutableTreeNode(new SSNIconData(new ImageIcon(getClass().getResource("/images/instagram.png")), null, "Instagram"));
                DefaultTreeModel treeModel = new DefaultTreeModel(top);
                JTree instagramTree = new JTree(treeModel);

                instagramTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                instagramTree.putClientProperty("JTree.lineStyle", "Angled");
                TreeCellRenderer renderer = new SSNIconCellRenderer();
                instagramTree.setCellRenderer(renderer);
                instagramTree.setShowsRootHandles(true);
                instagramTree.setEditable(false);
                instagramTree.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                instagramTree.setForeground(Color.WHITE);

                SSNInstagramSelectionListener albumSelectionListener = new SSNInstagramSelectionListener(this,
                        getHomeForm(), accessGrant);
                instagramTree.addTreeSelectionListener(albumSelectionListener);

                treeContainer.add(instagramTree,BorderLayout.CENTER);
                this.getHomeForm().setInstagramTree(instagramTree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showCloudeUsage() {
        String accessToken = null;
        SSNSubscriptionDetails subscriptionDetails = null;
        DecimalFormat decimalFormat = new DecimalFormat("###.##");

        if (this.getHomeForm().getLoginResponse() != null && this.getHomeForm().getLoginResponse() != null
                && this.getHomeForm().getLoginResponse().getData() != null && this.getHomeForm().getLoginResponse().getData().getUser() != null
                && this.getHomeForm().getLoginResponse().getData().getUser().getAccess_token() != null) {
            accessToken = this.getHomeForm().getLoginResponse().getData().getUser().getAccess_token();
        }

        if (accessToken != null && !accessToken.isEmpty()) {
            subscriptionDetails = getSubscriptionDetails(accessToken);
        }

        if (subscriptionDetails != null) {
            try {

                GridBagLayout gridBagLayout = new GridBagLayout();
                GridBagConstraints gbc = new GridBagConstraints();

                JPanel usagePanel = new JPanel();
                usagePanel.setBorder(BorderFactory.createEmptyBorder());
                usagePanel.setLayout(gridBagLayout);
                //usagePanel.setBackground(SSNConstants.SSN_TOOL_BAR_BLACK_COLOR);
                usagePanel.setBackground(new Color(49, 49, 49));

                JProgressBar progressBar = null;
                LookAndFeel previousLF = UIManager.getLookAndFeel();
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

                    progressBar = new JProgressBar();
                    progressBar.setBackground(new Color(30, 32, 21));
                    progressBar.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    progressBar.setPreferredSize(new Dimension(this.getWidth() + 4, 12));
                    progressBar.setOpaque(true);

                    //progressBar.setBounds(0, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-20, this.getWidth(), 5);
                    UIManager.setLookAndFeel(previousLF);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (progressBar != null) {
                    progressBar.setValue((int) Float.parseFloat(subscriptionDetails.getSubscriptionPlans().getPercentage().replace("%", "").trim()));
                    progressBar.setBorder(BorderFactory.createEmptyBorder());
                }
                //progressBar.setValue(100 - (int) Float.parseFloat("50%".replace("%", "").trim()));
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(0, 4, 0, 4);
                gbc.gridx = 0;
                gbc.gridy = 0;

                String availabelSpace = subscriptionDetails.getSubscriptionPlans().getAvailble_space();
                //availabelSpace = availabelSpace.substring(0, availabelSpace.length() - 2);
                String totalSpace = subscriptionDetails.getSubscriptionPlans().getStorage_limit();
                String usedSpace = "";
                //if (totalSpace.contains("MB")) {
                    totalSpace = totalSpace;
                    usedSpace = availabelSpace.trim() + " Available/" + totalSpace.trim() + " ";
//                } else {
//                    totalSpace = totalSpace.substring(0, totalSpace.length() - 2);
//                    usedSpace = decimalFormat.format(Float.parseFloat(totalSpace.trim()) - Float.parseFloat(availabelSpace.trim())) + "GB used/" + totalSpace.trim() + " GB";
//                }

                    //System.out.println("total space "+ totalSpace);
                   // System.out.println("Available space " + availabelSpace);
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.NORTH;
                JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                p2.setBackground(new Color(49, 49, 49));
                JLabel consumedSpace = new JLabel(usedSpace);
                consumedSpace.setFont(new Font("open sans", Font.BOLD, 11));
                consumedSpace.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
                //consumedSpace.setBackground(new Color(49,49,49));
                consumedSpace.setAlignmentY(TOP_ALIGNMENT);
                consumedSpace.setOpaque(false);

                JLabel addSubscription = new JLabel(new ImageIcon(getClass().getResource("/icon/add-icon.png")));
                addSubscription.setName("addSubscription");
                addSubscription.addMouseListener(this.getHomeForm().getHomeController());
                // Commented for no subscriptions going now. TODO
                // p2.add(addSubscription);
                // usagePanel.setBackground(new Color(0,0,0,1));
                p2.add(progressBar);
                p2.setBorder(BorderFactory.createEmptyBorder());
                gbc.insets = new Insets(0, 0, 0, 0);
                usagePanel.add(p2, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(0, 0, 0, 0);
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                usagePanel.add(consumedSpace, gbc);
                JPanel consumedSpaceContainer = new JPanel();

                consumedSpaceContainer.add(consumedSpace, BorderLayout.NORTH);
                consumedSpaceContainer.setBackground(new Color(49, 49, 49));
                usagePanel.add(consumedSpaceContainer, gbc);

                add(usagePanel, BorderLayout.SOUTH);
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
    }

    private boolean isAurthorized(){
        boolean isAurhorized = true;
        try{
            AccessGrant accessGrant = getHomeForm().getFacebookAccessGrant();
            FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(
                    SSNConstants.SSN_FACEBOOK_API_KEY, SSNConstants.SSN_FACEBOOK_SECRET_KEY);
            Connection<Facebook> facebookConnection = connectionFactory.createConnection(accessGrant);
        }catch(Exception ee){
            ee.printStackTrace();
            isAurhorized = false;
        }
        return isAurhorized;
    }
    public SSNSubscriptionDetails getSubscriptionDetails(String accessToken) {
        try {
            SSNSubscriptionDetails subscriptionDetails = null;

            String urlString = SSNConstants.SSN_WEB_HOST + "api/users/subscription_details";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String input = "access_token=%s";
            input = String.format(input, URLEncoder.encode(accessToken, "UTF-8"));

            OutputStream os = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(input);
            writer.close();
            os.close();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                StringBuilder response = new StringBuilder();

                while ((output = br.readLine()) != null) {
                    response.append(output);
                }
                //System.out.println("response " + response);
                org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
                Map<String, Object> outputJSON = mapper.readValue(response.toString(), Map.class);

                boolean success = (Boolean) outputJSON.get("success");
                if (success) {
                    Map<String, Object> dataJSON = (Map<String, Object>) outputJSON.get("data");
                    subscriptionDetails = mapper.readValue(mapper.writeValueAsString(dataJSON), SSNSubscriptionDetails.class);
                }
            }
            return subscriptionDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return the m_display
     */
    public JTextField getM_display() {
        return m_display;
    }

    /**
     * @param m_display the m_display to set
     */
    public void setM_display(JTextField m_display) {
        this.m_display = m_display;
    }

    /**
     * @return the homeForm
     */
    public SSNHomeForm getHomeForm() {
        return homeForm;
    }

    /**
     * @param homeForm the homeForm to set
     */
    public void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }

    /**
     * @return the selectedFolder
     */
    public String getSelectedFolder() {
        return selectedFolder;
    }

    /**
     * @param selectedFolder the selectedFolder to set
     */
    public void setSelectedFolder(String selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private UIDefaults getUidefaults() {
        UIDefaults overrides = new UIDefaults();
        overrides.put("ProgressBar.background", Color.YELLOW);
        overrides.put("ProgressBar.foreground", Color.GREEN);
        return overrides;
    }
}
