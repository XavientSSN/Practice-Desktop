/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ui.custom.component;

/**
 *
 * @author RDiwakar
 */


import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNHomeModel;
import com.ssn.model.TaggedFace;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.request.SSNFaceRecognitionJobType;
import com.ssn.ws.rest.request.SSNFaceRecognitionRequest;
import com.ssn.ws.rest.response.SSNFaceRecognitionResponse;
import com.ssn.ws.rest.service.SSNFaceRecognitionService;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author hvashistha
 */
public class SSNImageSlider extends JFrame implements Runnable,MouseListener, MouseMotionListener, KeyListener {

        private              SSNHomeForm         homeForm            = null;
        private              JLabel              slideshowTollbarLbl = null;
        private              ArrayList<ImageIcon> imageList          = null;
        private static       Integer             imagePosInArray     = null;
        private static       Integer             listSize            = null;
        private boolean                          loaded              = false;
        private boolean                          flag                = false;
        private              Thread              t1, t2;
        private final        JPanel              panel2, mainControlPanel,imgCountPanel, closeControlPanel,previousControlPanel, nextControlPanel, rotateLeftPanel, rotateRightPanel, zoomInPanel, zoomOutPanel, faceTagPanel, savePanel;
        private final        JLabel              images;
        private final        JLayeredPane        layeredPane;
        private             double                 screenHeight, screenWidth;
        private JButton previousButton, nextButton, playButton,closeButton;
        private JLabel rotateLeftButton,rotateRightButton,faceTagButton,saveButton,zoomInButton,zoomOutButton;
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNImageSlider.class);
        private int index =  0;
        private List<File> imageListForOperation = null;
        private int xPosition;
        private int yPosition;
        
        public SSNImageSlider(SSNHomeForm homeForm,JLabel slideshowTollbarLbl,ArrayList<ImageIcon> imageList, int index) {

           this.index  =   index;
            
            panel2 = new JPanel(new BorderLayout());
            images = new JLabel();

            imgCountPanel       =   new JPanel();
            mainControlPanel    =   new JPanel();
            closeControlPanel   =   new JPanel();
            previousControlPanel=   new JPanel();
            nextControlPanel    =   new JPanel();
            rotateLeftPanel     =   new JPanel();
            rotateRightPanel    =   new JPanel();
            faceTagPanel        =   new JPanel();
            savePanel           =   new JPanel();
            zoomInPanel         =   new JPanel();
            zoomOutPanel        =   new JPanel();
            layeredPane         =   new JLayeredPane();
            try {
                this.homeForm       = homeForm;
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                this.getContentPane().setBackground(new Color(0,0,0));
                screenHeight        = dimension.getHeight();
                screenWidth         = dimension.getWidth();
                SSNImageSlider.setImagePosInArray(index);
                setSlideshowTollbarLbl(slideshowTollbarLbl);
                initSlideShow(imageList);
                
            } 
            catch (Exception ex) {       
                log.error(ex);
             }

        }

        public SSNImageSlider(SSNHomeForm homeForm, JLabel jBtn1, ArrayList<ImageIcon> imageList, int index, List<File> imageListForOperation) {
       
            this.index  =   index;
            panel2 = new JPanel(new BorderLayout());
            images = new JLabel();

            imgCountPanel       =   new JPanel();
            mainControlPanel    =   new JPanel();
            closeControlPanel   =   new JPanel();
            previousControlPanel=   new JPanel();
            nextControlPanel    =   new JPanel();
            rotateLeftPanel     =   new JPanel();
            rotateRightPanel    =   new JPanel();
            faceTagPanel        =   new JPanel();
            savePanel           =   new JPanel();
            zoomInPanel         =   new JPanel();
            zoomOutPanel        =   new JPanel();
            
            
            layeredPane         =   new JLayeredPane();
            try {
                this.homeForm       = homeForm;
                this.getContentPane().setBackground(new Color(0,0,0));
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                screenHeight        = dimension.getHeight();
                screenWidth         = dimension.getWidth();
                setSlideshowTollbarLbl(slideshowTollbarLbl);
                initSlideShow(imageList);
                this.imageListForOperation = imageListForOperation;
            } 
            catch (Exception ex) {       
                log.error(ex);
             
            }

    }

        private void initSlideShow(ArrayList<ImageIcon> imageListMy) {
            imageList = imageListMy;
            addRotateRightPanel();
            addRotateLeftPanel();
            addPlayControlPanel();
            addCloseControlPanel();
            addPreviousControlPanel();

            addNextControlPanel();
            addZoomOutPanel();
            addZoomInPanel();
            addSavePanel();
            addFaceTagPanel();
            
            setLoaded(true);
            setLayout(new BorderLayout());
            setUndecorated(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);
            setVisible(true);
            setLocationRelativeTo(null);
            add(layeredPane,BorderLayout.CENTER);
          
        }
         @Override
        public void run() {

            if(homeForm != null && homeForm.isShowing()) {
                    this.setAlwaysOnTop(true);
            }

            t1 = new Thread() {
                 public void run() {                          
                     updatePanel();
                        while (flag) {
                            updatePanel();
                        }                                                
                     setAlwaysOnTop(true); 
                 }
            };
            t1.setName("ssn-slideshow");
            t1.setDaemon(true);
            t1.start();
        }
        private void addNewPanel(){
            JPanel panelNew =   new JPanel();
            JButton  btnPreviousImage  =   new JButton(new ImageIcon(getClass().getResource("/images/slideshow_arrow_left.png")));
            panelNew.add(btnPreviousImage);
            add(panelNew,BorderLayout.EAST);
       }
        private JPanel addNextControlPanel(){
            nextButton = new JButton(new ImageIcon(getClass().getResource("/icon/slideshow_arrow_right.png")));
            nextButton.setActionCommand("Next");
               nextButton.setToolTipText("Next Image");
               nextButton.setBorder(null);
               nextButton.setEnabled(true);
               nextButton.setFocusable(false);
               nextButton.setBorderPainted(false);
               nextButton.setFocusPainted(false);
               nextButton.setContentAreaFilled(false);
               nextButton.addKeyListener(this);
               nextButton.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       if (e != null && e.getActionCommand().equalsIgnoreCase("Next") && getImagePosInArray() != null) {
                           updatePanelWithNextImg(getImagePosInArray());
                       }
                   }
               });
              Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
              nextControlPanel.add(nextButton);
              nextControlPanel.setOpaque(false);
              nextControlPanel.setBounds(dim.width-120, dim.height-420, 120, 50);
            //  nextControlPanel.setBounds(900, 350, 120, 40);
              nextControlPanel.setVisible(true);
              nextControlPanel.setBackground(new Color(0,0,0,1));
              layeredPane.add(nextControlPanel,new Integer(1), 0);
            return nextControlPanel;
        }
        private JPanel addPreviousControlPanel(){
               previousButton = new JButton(new ImageIcon(getClass().getResource("/icon/slideshow_arrow_left.png")));
               previousButton.setActionCommand("Previous");
               previousButton.setToolTipText("Previous Image");
               previousButton.setBorder(null);
               previousButton.setFocusable(false);
               previousButton.setBorderPainted(false);
               previousButton.setFocusPainted(false);
               previousButton.setContentAreaFilled(false);
               previousButton.addKeyListener(this);
               previousButton.setEnabled(true);
               previousButton.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       if (e != null && e.getActionCommand().equalsIgnoreCase("Previous") && getImagePosInArray() != null) {
                           updatePanelWithPreviousImg(getImagePosInArray());
                       }
                   }
               });
              Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
              previousControlPanel.add(previousButton);
              previousControlPanel.setOpaque(false);
              previousControlPanel.setBounds(0, dim.height-420, 120, 50);
             // previousControlPanel.setBounds(0, 350, 120, 40);
              previousControlPanel.setVisible(true);
              previousControlPanel.setBackground(new Color(0,0,0,1));
              layeredPane.add(previousControlPanel,new Integer(1), 0);
            return previousControlPanel;
        }
        private JPanel addCloseControlPanel(){

            closeButton = new JButton(new ImageIcon(getClass().getResource("/images/popup-close.png")));
               closeButton.setBorder(null);
               closeButton.setActionCommand("Exit");
               closeButton.setToolTipText("Exit Slider");
               closeButton.setFocusable(false);
               closeButton.setBorderPainted(false);
               closeButton.setFocusPainted(false);
               closeButton.setContentAreaFilled(false);
               closeButton.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       if (e != null && e.getActionCommand().equalsIgnoreCase("Exit")) {
                           //getSlideshowTollbarLbl().setIcon(new ImageIcon(getClass().getResource("/icon/slideshow-normal.png"))); 
                           homeForm.repaint();
                           homeForm.setCurrentImage(null);
                           dispose();
                           
                           try {
                               String srcAlbumPath = homeForm.ssnFileExplorer.m_display.getText();
                               if (SSNHelper.lastAlbum != null) {
                                   if (SSNHelper.lastAlbum.equals("View All Albums")) {
                                       srcAlbumPath = SSNHelper.getSsnHiveDirPath();
                                       homeForm.ssnFileExplorer.m_display.setText(srcAlbumPath);
                                   } else if (SSNHelper.lastAlbum.equals("Instagram Media")) {
                                       srcAlbumPath = SSNHelper.getInstagramPhotosDirPath();
                                       homeForm.ssnFileExplorer.m_display.setText("instagramMedia");
                                   } else if (SSNHelper.lastAlbum.equals("Tag UnTagged Media")) {
                                       srcAlbumPath = SSNHelper.getSsnHiveDirPath();
                                       homeForm.ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
                                   } else if (SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) {
                                       srcAlbumPath = SSNHelper.lastAlbum;
                                       homeForm.ssnFileExplorer.m_display.setText(srcAlbumPath);
                                   }
                               }
                               
                               SSNGalleryHelper  contentPane = new SSNGalleryHelper(srcAlbumPath, homeForm, "ALL");
                               contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                               homeForm.getSsnHomeCenterPanel().removeAll();
                               homeForm.getSsnHomeCenterMainPanel().removeAll();
                               homeForm.getSsnHomeCenterPanel().add(homeForm.getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())));
                               homeForm.getSsnHomeCenterMainPanel().add(homeForm.getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                               homeForm.getSsnHomeCenterMainPanel().add(homeForm.getSsnHomeCenterPanel(), BorderLayout.CENTER);
                               homeForm.revalidate();
                           } catch (IOException ex) {
                               Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           
                       }    
                   }
               });

           Dimension dim =Toolkit.getDefaultToolkit().getScreenSize();
           closeControlPanel.add(closeButton);
           closeControlPanel.setOpaque(false);
           
           closeControlPanel.setBounds(dim.width-200, 135, 120, 40);
           closeControlPanel.setVisible(true);
           closeControlPanel.setBackground(new Color(0,0,0,1));
           layeredPane.add(closeControlPanel,new Integer(1), 0);
           return closeControlPanel;
        }
        private JPanel addPlayControlPanel() {
            try {


                JLabel playLabel = new JLabel("PLAY SLIDESHOW");
                playLabel.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
                playLabel.setFont(new Font("open sans", Font.BOLD, 12));
                playLabel.setBackground(new Color(0,0,0,1));
                
                playLabel.setOpaque(true);


                playButton = new JButton(new ImageIcon(getClass().getResource("/icon/play_slideshow_btn.png")));
                playButton.setActionCommand("Play");
                playButton.setToolTipText("Play SlideShow");
                playButton.setEnabled(true);
                playButton.setBorder(null);
                playButton.setFocusable(false);
                playButton.setBorderPainted(false);
                playButton.setFocusPainted(false);
                playButton.setContentAreaFilled(false);
                playButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e != null && e.getActionCommand().equalsIgnoreCase("Play")) {
                            t1.stop();
                            //mainControlPanel.setVisible(false);
                              homeForm.getHomeModel().openSlideshow(slideshowTollbarLbl);
                        }
                    }
                });
                mainControlPanel.add(playLabel);
                mainControlPanel.add(Box.createHorizontalStrut(15));
                mainControlPanel.add(playButton);
                mainControlPanel.setOpaque(false);
                int x   = (int)(screenWidth/100)*70;
                int y   = (int)(screenHeight/100)*85;  
            
                mainControlPanel.setBounds(x, y, 150, 85);
                mainControlPanel.setVisible(true);
                layeredPane.add(mainControlPanel,new Integer(1), 0);

            } catch (Exception iOException) {
                log.error(iOException);
                iOException.printStackTrace();
            }

            return mainControlPanel;
        }
        private JPanel addRotateLeftPanel() {
            try {
                rotateLeftButton = new JLabel(new ImageIcon(getClass().getResource("/icon/rotate-left.png")));
                rotateLeftButton.setName("rotateLeft");
                rotateLeftButton.setToolTipText("Rotate Left");
                rotateLeftButton.setEnabled(true);
                rotateLeftButton.setBorder(null);
                rotateLeftButton.setFocusable(false);
                rotateLeftButton.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        

                        BufferedImage image1 = homeForm.getCurrentImage();
                        Image background = null;
                        if (homeForm.getRotateAngleMultiple() % 2 == 0) {
                            background = SSNHelper.getScaledImage(image1, homeForm, false, 1);
                        } else {
                            background = SSNHelper.getScaledImage(image1, homeForm, true, 1);
                        }
                        BufferedImage bufferedImage = createResizedCopy(background, getImageWidth(index), getImageHeight(index), true);
                        homeForm.setRotateAngleMultiple(homeForm.getRotateAngleMultiple()-1);
                        BufferedImage rotatedImage = homeForm.getHomeModel().rotate(bufferedImage, homeForm.getRotateAngleMultiple());
                        homeForm.setCurrentRotatedImage(rotatedImage);
                        homeForm.setCurrentImage(rotatedImage);
                        images.setIcon(new ImageIcon(rotatedImage));
                        images.setHorizontalAlignment(SwingConstants.CENTER); 
                        panel2.add(images, BorderLayout.CENTER);
                        panel2.revalidate();
                        panel2.setBackground(new Color(0,0,0,1));
                        Dimension dimension     =  Toolkit.getDefaultToolkit().getScreenSize();
                        panel2.setBounds((int)screenWidth/4, (int)(dimension.getHeight()/2)-200, 600,400);
                        panel2.setVisible(true);          

                        layeredPane.add(panel2,new Integer(0), 0);
                        layeredPane.add(addImgCountLabel(), 1, 0);
                        layeredPane.setVisible(true);
                        layeredPane.setBackground(new Color(0,0,0,180));
                        add(layeredPane, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                        setVisible(true);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                       
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                     
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                   
                    }
                });
                rotateLeftPanel.add(Box.createHorizontalStrut(15));
                rotateLeftPanel.add(rotateLeftButton);
                rotateLeftPanel.setOpaque(false);
                int x   = (int)(screenWidth/100)*30;
                int y   = (int)(screenHeight/100)*90;  
               
                rotateLeftPanel.setBounds(x, y, 200, 70);
                rotateLeftPanel.setVisible(true);
                layeredPane.add(rotateLeftPanel,new Integer(1), 1);

            } catch (Exception iOException) {
                log.error(iOException);
                iOException.printStackTrace();
            }

            return rotateLeftPanel;
        }
        private JPanel addRotateRightPanel() {
            try {
                
                rotateRightButton = new JLabel(new ImageIcon(getClass().getResource("/icon/rotate-right.png")));
                rotateRightButton.setName("rotateRight");
                rotateRightButton.setToolTipText("Rotate Right");
                rotateRightButton.setEnabled(true);
                rotateRightButton.setBorder(null);
                rotateRightButton.setFocusable(false);
                rotateRightButton.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        
                        BufferedImage image1 = homeForm.getCurrentImage();
                        double zoomIn = 1;
                        homeForm.setZoomIn(zoomIn);
                        DecimalFormat df = new DecimalFormat("#.##");

                        homeForm.setRotateAngleMultiple(homeForm.getRotateAngleMultiple()+1);
                        Image background = null;
                        if (homeForm.getRotateAngleMultiple() % 2 == 0) {
                            background = SSNHelper.getScaledImage(image1, homeForm, false, 1);
                        } else {
                            background = SSNHelper.getScaledImage(image1, homeForm, true, 1);
                        }
                        BufferedImage bufferedImage = createResizedCopy(background, getImageWidth(index), getImageHeight(index), true);
                        BufferedImage rotatedImage = homeForm.getHomeModel().rotate(bufferedImage, homeForm.getRotateAngleMultiple());
                        homeForm.setCurrentRotatedImage(rotatedImage);
                        homeForm.setCurrentImage(rotatedImage);
                        images.setIcon(new ImageIcon(rotatedImage));
                        images.setHorizontalAlignment(SwingConstants.CENTER); 
                        panel2.add(images, BorderLayout.CENTER);
                        panel2.revalidate();
                        panel2.setBackground(new Color(0,0,0,1));
                        Dimension dimension     =  Toolkit.getDefaultToolkit().getScreenSize();
                        panel2.setBounds((int)screenWidth/4, (int)(dimension.getHeight()/2)-200, 600,400);
                        panel2.setVisible(true);          

                        layeredPane.add(panel2,new Integer(0), 0);
                        layeredPane.add(addImgCountLabel(), 1, 0);
                        layeredPane.setVisible(true);
                        layeredPane.setBackground(new Color(0,0,0,1));
                        add(layeredPane, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                        setVisible(true);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                       
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                     
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                   
                    }
                });
                rotateRightPanel.add(Box.createHorizontalStrut(15));
                rotateRightPanel.add(rotateRightButton);
                rotateRightPanel.setOpaque(false);
                int x   = (int)(screenWidth/100)*35;
                int y   = (int)(screenHeight/100)*90;  
                
                rotateRightPanel.setBounds(x, y, 200, 50);
                rotateRightPanel.setVisible(true);
                layeredPane.add(rotateRightPanel,new Integer(1), 2);

            } catch (Exception iOException) {
                log.error(iOException);
                iOException.printStackTrace();
            }

            return rotateRightPanel;
        }
        
        BufferedImage createResizedCopy(Image originalImage, 
    		int scaledWidth, int scaledHeight, 
    		boolean preserveAlpha)
    {
    	
    	int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    	BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
    	   Graphics2D g = scaledBI.createGraphics();
    	if (preserveAlpha) {
    		g.setComposite(AlphaComposite.Src);
    	}
    	g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    	g.dispose();
    	return scaledBI;
    }
        private JPanel addZoomOutPanel() {
            try {
                
                zoomOutButton = new JLabel(new ImageIcon(getClass().getResource("/icon/zoom-out.png")));
                zoomOutButton.setName("zoomOut");
                zoomOutButton.setToolTipText("Zoom Out");
                zoomOutButton.setEnabled(true);
                zoomOutButton.setBorder(null);
                zoomOutButton.setFocusable(false);
                zoomOutButton.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        
                        BufferedImage image1 = homeForm.getCurrentImage();
                        double zoomIn = homeForm.getZoomIn() + .2;
                        DecimalFormat df = new DecimalFormat("#.##");
                        zoomIn = Double.parseDouble(df.format(zoomIn));
                        if (zoomIn != 0) {
                            homeForm.setZoomIn(zoomIn);
                        } else {

                            zoomIn = zoomIn + .2;
                        }
                        int multiplier = homeForm.getRotateAngleMultiple();
                        Image background = null;
                        if (multiplier % 2 == 0) {
                            background = SSNHelper.getScaledImage(image1, homeForm, false, zoomIn);
                        } else {
                            background = SSNHelper.getScaledImage(image1, homeForm, true, zoomIn);
                        }
                        images.setIcon(new ImageIcon(background));
                        images.setHorizontalAlignment(SwingConstants.CENTER); 
                        panel2.add(images, BorderLayout.CENTER);
                        panel2.revalidate();
                        panel2.setBackground(new Color(0,0,0,1));
                        Dimension dimension     =  Toolkit.getDefaultToolkit().getScreenSize();
                        panel2.setBounds((int)screenWidth/4, (int)(dimension.getHeight()/2)-200, 600,400);
                        panel2.setVisible(true);          

                        layeredPane.add(panel2,new Integer(0), 0);
                        layeredPane.add(addImgCountLabel(), 1, 0);
                        layeredPane.setVisible(true);
                        layeredPane.setBackground(new Color(0,0,0,1));
                        add(layeredPane, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                        setVisible(true);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                       
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                     
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                   
                    }
                });
                zoomOutPanel.add(Box.createHorizontalStrut(15));
                zoomOutPanel.add(zoomOutButton);
                zoomOutPanel.setOpaque(false);
                int x   = (int)(screenWidth/100)*40;
                int y   = (int)(screenHeight/100)*90;  
                
                zoomOutPanel.setBounds(x, y, 200, 50);
                rotateLeftPanel.setVisible(true);
                layeredPane.add(zoomOutPanel,new Integer(1), 3);

            } catch (Exception iOException) {
                log.error(iOException);
                iOException.printStackTrace();
            }

            return zoomOutPanel;
        }
        private JPanel addZoomInPanel() {
        try {
            zoomInButton = new JLabel(new ImageIcon(getClass().getResource("/icon/zoom-in.png")));
            zoomInButton.setName("zoomIn");
            zoomInButton.setToolTipText("Zoom In");
            zoomInButton.setEnabled(true);
            zoomInButton.setBorder(null);
            zoomInButton.setFocusable(false);
            zoomInButton.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        
                        BufferedImage image1 = homeForm.getCurrentImage();
                        double zoomIn = homeForm.getZoomIn() - .2;
                        DecimalFormat df = new DecimalFormat("#.##");
                        zoomIn = Double.parseDouble(df.format(zoomIn));
                        if (zoomIn != 0) {
                            homeForm.setZoomIn(zoomIn);
                        } else {

                            zoomIn = zoomIn + .2;
                        }
                        int multiplier = homeForm.getRotateAngleMultiple();
                        Image background = null;
                        if (multiplier % 2 == 0) {
                            background = SSNHelper.getScaledImage(image1, homeForm, false, zoomIn);
                        } else {
                            background = SSNHelper.getScaledImage(image1, homeForm, true, zoomIn);
                        }
                        images.setIcon(new ImageIcon(background));
                        images.setHorizontalAlignment(SwingConstants.CENTER); 
                        panel2.add(images, BorderLayout.CENTER);
                        panel2.revalidate();
                        panel2.setBackground(new Color(0,0,0,1));
                        Dimension dimension     =  Toolkit.getDefaultToolkit().getScreenSize();
                        
                        int x   = (int)screenWidth/4;
                        int y   = (int)(screenHeight/100)*90; 
                        panel2.setBounds(x, (int)(dimension.getHeight()/2)-200, 600,400);
                        panel2.setVisible(true);          

                        layeredPane.add(panel2,new Integer(0), 0);
                        layeredPane.add(addImgCountLabel(), 1, 0);
                        layeredPane.setVisible(true);
                        layeredPane.setBackground(new Color(0,0,0,1));
                        add(layeredPane, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                        setVisible(true);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                       
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                     
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                   
                    }
                });
            zoomInPanel.add(Box.createHorizontalStrut(15));
            zoomInPanel.add(zoomInButton);
            zoomInPanel.setOpaque(false);
            int x   = (int)(screenWidth/100)*45;
            int y   = (int)(screenHeight/100)*90;  
           
            zoomInPanel.setBounds(x, y, 200, 50);
            zoomInPanel.setVisible(true);
            layeredPane.add(zoomInPanel,new Integer(1), 4);

        } catch (Exception iOException) {
            log.error(iOException);
            iOException.printStackTrace();
        }

        return zoomInPanel;
    }
        private JPanel addFaceTagPanel() {
            try {
                faceTagButton = new JLabel(new ImageIcon(getClass().getResource("/icon/taged.png")));
                faceTagButton.setName("taged");
                faceTagButton.setToolTipText("Face Tag");
                faceTagButton.setEnabled(true);
                faceTagButton.setBorder(null);
                faceTagButton.setFocusable(false);
                faceTagButton.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        tagImage();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });


                faceTagPanel.add(Box.createHorizontalStrut(15));
                faceTagPanel.add(faceTagButton);
                faceTagPanel.setOpaque(false);
                int x   = (int)(screenWidth/100)*50;
                int y   = (int)(screenHeight/100)*90;  
               
                faceTagPanel.setBounds(x, y, 200, 50);
                faceTagPanel.setVisible(true);
                layeredPane.add(faceTagPanel,new Integer(1), 3);

            } catch (Exception iOException) {
                log.error(iOException);
                iOException.printStackTrace();
            }

            return faceTagPanel;
        }
        private JPanel addSavePanel() {
            try {
                saveButton = new JLabel(new ImageIcon(getClass().getResource("/icon/save_small_icon.png")));
                saveButton.setName("save");
                saveButton.setToolTipText("Save");
                saveButton.setEnabled(true);
                saveButton.setBorder(null);
                saveButton.setFocusable(false);
                saveButton.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        BufferedImage image = homeForm.getCurrentRotatedImage();
                        int multiplier = homeForm.getRotateAngleMultiple();
                        if (image != null && multiplier % 4 != 0) {
                            
                                try {
                                    File currentFile = homeForm.getCurrentFile();
                                    int index = currentFile.getAbsolutePath().lastIndexOf(File.separator);
                                    String currentPath = currentFile.getAbsolutePath().substring(0, index);
                                    String newFileName = "";
                                    newFileName = "rotated_" + currentFile.getName();
                                   
                                    ImageIO.write(image, "jpg", new File(currentPath + File.separator + newFileName));
                                } catch (IOException ex) {
                                    //logger.error(ex.getMessage());
                                    ex.printStackTrace();
                                }
                            }

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });


                savePanel.add(Box.createHorizontalStrut(15));
                savePanel.add(saveButton);
                savePanel.setOpaque(false);
                int x   = (int)(screenWidth/100)*55;
                int y   = (int)(screenHeight/100)*90;  
               
                savePanel.setBounds(x, y, 200, 50);
                savePanel.setVisible(true);
                layeredPane.add(savePanel,new Integer(1), 3);

            } catch (Exception iOException) {
                log.error(iOException);
                iOException.printStackTrace();
            }

            return savePanel;
        }
        private JPanel addImgCountLabel() {

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            JLabel imgCount = new JLabel( ( (getImagePosInArray() + 1)+ "/" + imageList.size()) );
            imgCount.setFont(new Font("open sans",Font.BOLD,16));   
           // imgCount.setForeground(new Color(156,156,156));
            imgCount.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
            int x   = (int)(dim.width/100)*25;
            int y   = (int)(dim.height/100)*90;
            imgCountPanel.setBounds(x, y, 200, 50);
            //imgCountPanel.setBounds(dim.width / 2 - 350, dim.height - 60, 50, 50);
            imgCountPanel.setBackground(new Color(0,0,0,1));
            imgCountPanel.setOpaque(true);
            imgCountPanel.setVisible(true);        
            imgCountPanel.removeAll();
            imgCountPanel.add(imgCount);

            return imgCountPanel;
        }
        private JPanel updatePanel() {
          
            enablePreviousButton(this.index);
            int i=this.index;
           
                if(this.index>=imageList.size())
                    this.index=1;
                synchronized (this) {
                    setImagePosInArray(i);
                }           
                /*
                *  The below line of code displays the smaller images in full-screen mode with stretching & skewing
                */


                homeForm.setCurrentFile(imageListForOperation.get(i));
                
                images.setIcon(new ImageIcon(imageList.get(i).getImage().getScaledInstance(getImageWidth(i), getImageHeight(i), java.awt.Image.SCALE_SMOOTH)));
                images.setHorizontalAlignment(SwingConstants.CENTER); 
                
                panel2.add(images, BorderLayout.CENTER);

                panel2.setBorder(BorderFactory.createLineBorder(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR));
                panel2.revalidate();
               // panel2.setBackground(new Color(27, 27, 27));
                
                panel2.addMouseMotionListener(this); // mouse-motion events
                addKeyListener(this);
                this.setxPosition((int)screenWidth/4);
                this.setyPosition(184);
                
                panel2.setBounds(this.getxPosition(), this.getyPosition(), 600,400);
                panel2.setVisible(true);            
                panel2.setBackground(new Color(0,0,0,1));
                layeredPane.setBackground(new Color(0,0,0,1));
                layeredPane.add(panel2,0, 0);
                layeredPane.add(addImgCountLabel(), 1, 0);

                try {
                    BufferedImage image1 = ImageIO.read( homeForm.getCurrentFile());
                    homeForm.setCurrentImage(image1);
                    layeredPane.setVisible(true);
                    add(layeredPane, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                    Thread.sleep(1200);

                    if (i == (imageList.size() - 1)) {
                        flag = true;
                    }

                } catch (InterruptedException ex) {
                    log.error(ex);
                    ex.printStackTrace();
                }catch (IOException ex) {
                    log.error(ex);
                    ex.printStackTrace();
                }
           // }
            return panel2;
        }
        private void updatePanelWithNextImg(int currentIndex1) {
            /*
            *  The below line of code displays the smaller images in full-screen mode with stretching & skewing
            */
             if (currentIndex1 <= (imageList.size()-1)) {

            
                if (currentIndex1 <= imageList.size() - 1) {
                       currentIndex1 = currentIndex1 + 1;
                       synchronized (this) {
                           setImagePosInArray(currentIndex1);
                       }
                }
                
                homeForm.setCurrentFile(imageListForOperation.get(currentIndex1));
                BufferedImage image1 = null;
                try {
                    image1 = ImageIO.read( homeForm.getCurrentFile());
                } catch (IOException ex) {
                    Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(image1 != null)
                    homeForm.setCurrentImage(image1);
            
                images.setIcon(new ImageIcon(imageList.get(currentIndex1).getImage().getScaledInstance(getImageWidth(currentIndex1), getImageHeight(currentIndex1), java.awt.Image.SCALE_SMOOTH)));
                images.setHorizontalAlignment(SwingConstants.CENTER);           
                panel2.add(images, BorderLayout.CENTER);
                panel2.revalidate();
                panel2.setBackground(new Color(0,0,0,1));
               // panel2.setBounds((this.getWidth()/2)-300, (this.getHeight()/2)-200, 600,400);
                 panel2.setBounds((int)screenWidth/4, (int)(this.getHeight()/2)-200, 600,400);
                panel2.setVisible(true);          

                layeredPane.add(panel2,new Integer(0), 0);
                layeredPane.add(addImgCountLabel(), 1, 0);
                layeredPane.setVisible(true);
                layeredPane.setBackground(new Color(0,0,0,1));
                add(layeredPane, BorderLayout.CENTER);
                revalidate();
                repaint();
                setVisible(true);
            }

            enablePreviousButton(currentIndex1);
            enableNextButton(currentIndex1);
            

        }
        private void enablePreviousButton(int currentIndex1){
            if(currentIndex1 != 0   ){
                previousButton.setEnabled(true);
            }else{
                previousButton.setEnabled(false);
            }
        }
        private void enableNextButton(int currentIndex1){
            if(currentIndex1 >= imageList.size()-1 ){
                nextButton.setEnabled(false);
            }else{
                nextButton.setEnabled(true);
            }
        }
        private void updatePanelWithPreviousImg(int currentIndex1) {
             
            if (currentIndex1 > 0 && (currentIndex1 <= imageList.size() - 1)) {
                currentIndex1 = currentIndex1 - 1;
                homeForm.setCurrentFile(imageListForOperation.get(currentIndex1));
                BufferedImage image1 = null;
                try {
                    image1 = ImageIO.read( homeForm.getCurrentFile());
                } catch (IOException ex) {
                    Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(image1 != null)
                    homeForm.setCurrentImage(image1);
                synchronized (this) {
                    setImagePosInArray(currentIndex1);
                }
            } 
            enablePreviousButton(currentIndex1);
            enableNextButton(currentIndex1);
            /*
            *  The below line of code displays the smaller images in full-screen mode with stretching & skewing
            */
    
            images.setIcon(new ImageIcon(imageList.get(currentIndex1).getImage().getScaledInstance(getImageWidth(currentIndex1), getImageHeight(currentIndex1), java.awt.Image.SCALE_SMOOTH)));
            images.setHorizontalAlignment(SwingConstants.CENTER);           
            panel2.add(images, BorderLayout.CENTER);
            panel2.revalidate();
            
            panel2.setBackground(new Color(0,0,0,1));
           // panel2.setBounds((this.getWidth()/2)-300, (this.getHeight()/2)-200, 600,400);
              panel2.setBounds((int)screenWidth/4, (int)(this.getHeight()/2)-200, 600,400);
            panel2.setVisible(true);            
            layeredPane.add(panel2,new Integer(0), 0);
            layeredPane.add(addImgCountLabel(), 1, 0);
            layeredPane.setBackground(new Color(0,0,0,1));
            layeredPane.setVisible(true);
            add(layeredPane, BorderLayout.CENTER);
            revalidate();
            repaint();
            setVisible(true);
        }
        
        private int getImageHeight(int currentIndex1){
            int maxHeight = 400;
             int newHeight=0;
            if(currentIndex1 <= imageList.size())
            {
             newHeight = imageList.get(currentIndex1).getImage().getWidth(rootPane);
            }
            
            return newHeight>maxHeight?maxHeight:newHeight;
        }
        private int getImageWidth(int currentIndex1){
            int newWidth =0;
             int maxWidth =600;
            if(currentIndex1 <= imageList.size())
            {
             newWidth = imageList.get(currentIndex1).getImage().getWidth(rootPane);
            }
            return newWidth>maxWidth?maxWidth:newWidth;
        }
        /**
         * @return the loaded
         */
        public boolean isLoaded() {
            return loaded;
        }

        /**
         * @param loaded the loaded to set
         */
        public void setLoaded(boolean loaded) {
            this.loaded = loaded;
        }

        /**
         * @return the imagePosInArray
         */
        public static Integer getImagePosInArray() {
            return imagePosInArray;
        }

        /**
         * @param aImagePosInArray the imagePosInArray to set
         */
        public static void setImagePosInArray(Integer aImagePosInArray) {
            imagePosInArray = aImagePosInArray;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (isLoaded()) {
                try {
                    t1.suspend();
                    imgCountPanel.setVisible(true);
                    mainControlPanel.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            t1.resume();
            imgCountPanel.setVisible(false);
            mainControlPanel.setVisible(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            t1.resume();
            mainControlPanel.setVisible(false);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isLoaded()) {
                try {
                    t1.suspend();
                    imgCountPanel.setVisible(true);
                    mainControlPanel.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        /**
         * @return the listSize
         */
        public static Integer getListSize() {
            return listSize;
        }

        /**
         * @param aListSize the listSize to set
         */
        public static void setListSize(Integer aListSize) {
            listSize = aListSize;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            JFrame keyEventObj = (JFrame) e.getSource();
            char c = e.getKeyChar();

             if (isLoaded()) {
                try {
                    t1.suspend();
                    mainControlPanel.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (keyEventObj != null) {
                if (c == KeyEvent.VK_ESCAPE) {
                    if(getSlideshowTollbarLbl()!= null)
                    getSlideshowTollbarLbl().setIcon(new ImageIcon(getClass().getResource("/icon/slideshow-normal.png"))); 
                    homeForm.repaint();
                    keyEventObj.dispose();
                    this.dispose();
                }

                if (e.getKeyText(e.getKeyCode()).equalsIgnoreCase("Left") || e.getKeyText(e.getKeyCode()).equalsIgnoreCase("Up")) {
                    t2 = new Thread(new Thread() {
                        @Override
                        public void run() {
                            updatePanelWithPreviousImg(getImagePosInArray());
                        }
                        });

                        t2.start();

                }

                if (e.getKeyText(e.getKeyCode()).equalsIgnoreCase("Right") || e.getKeyText(e.getKeyCode()).equalsIgnoreCase("Down")) {
                    t2 = new Thread(new Thread() {
                        @Override
                        public void run() {
                            updatePanelWithNextImg(getImagePosInArray());
                        }
                    });

                    t2.start();
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        /**
         * @return the slideshowTollbarLbl
         */
        public JLabel getSlideshowTollbarLbl() {
            return slideshowTollbarLbl;
        }

        /**
         * @param slideshowTollbarLbl the slideshowTollbarLbl to set
         */
        public void setSlideshowTollbarLbl(JLabel slideshowTollbarLbl) {
            this.slideshowTollbarLbl = slideshowTollbarLbl;
        }
        public JPanel getPanel2(){
            return panel2;
        }

        public int getxPosition() {
            return xPosition;
        }

        public void setxPosition(int xPosition) {
            this.xPosition = xPosition;
        }

        public int getyPosition() {
            return yPosition;
        }

        public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
        public void tagImage() {
        File currentFile = homeForm.getCurrentFile();
        //SSNFaceRecognitionRequest request = new SSNFaceRecognitionRequest(currentFile.getAbsolutePath());
         //imageList.get(homeForm.getCurrentImage().getScaledInstance(getImageWidth(currentIndex1), getImageHeight(currentIndex1), java.awt.Image.SCALE_SMOOTH));
            byte[] imageInByte = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write( homeForm.getCurrentImage(), "jpg", baos );
                baos.flush();
                imageInByte = baos.toByteArray();
            } catch (IOException ex) {
                log.error(ex);
            }
	
	
        SSNFaceRecognitionRequest request = new SSNFaceRecognitionRequest(imageInByte);
        request.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_ADD.toString());
        request.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
        final SSNFaceRecognitionService service = new SSNFaceRecognitionService();
        SSNFaceRecognitionResponse response = service.getResponse(request);
        request.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_RECOGNIZE.toString());
        request.getParameters().remove(SSNFaceRecognitionRequest.TAG);
        SSNFaceRecognitionResponse response2 = service.getResponse(request);

        List<SSNFaceRecognitionResponse.FaceDetection> listForImgIndex = response.getFaceDetection();
        Map<Float, String> imgIndexMap = new HashMap<>();

        List<SSNFaceRecognitionResponse.FaceDetection> list = response2.getFaceDetection();
        JPanel panel    =   panel2;

            
        int panelX = panel.getX();
        int panelY = panel.getY();
        
        List<JPanel> tagPanelList = new ArrayList<JPanel>();
        boolean exist  = SSNDao.checkFaceExist(currentFile.getAbsolutePath());
        if (listForImgIndex != null && !listForImgIndex.isEmpty()) {
            for (SSNFaceRecognitionResponse.FaceDetection face : listForImgIndex) {
                float x = face.getBoundingbox().getTl().getX();
                String imgIndex = face.getImgIndex();
                imgIndexMap.put(x, imgIndex);
            }
        }
        if (list != null && !list.isEmpty()) {
            //JLayeredPane lpane = new JLayeredPane();
            for (SSNFaceRecognitionResponse.FaceDetection face : list) {
                float x = face.getBoundingbox().getTl().getX();
                float y = face.getBoundingbox().getTl().getY();
                float width = face.getBoundingbox().getSize().getWidth();
                float height = face.getBoundingbox().getSize().getHeight();
                final String imgIndex = imgIndexMap.get(x);
                TaggedFace taggedFace = new TaggedFace();
                taggedFace.setxCoordinate(x);
                taggedFace.setyCoordinate(y);
                taggedFace.setWidth(width);
                taggedFace.setHeight(height);
                taggedFace.setImageIndex(imgIndex);
                float[] coordinates = SSNHelper.getScaledCoordinatesofTaggedFace(x, y, width, height, homeForm.getCurrentImage(), homeForm);
                if (coordinates != null) {
                    x = coordinates[0];
                    y = coordinates[1];
                    width = coordinates[2];
                    height = coordinates[3];
                }
                JPanel tag = new JPanel();
                tag.setBackground(new Color(0, 0, 0, 1));
                tag.setBounds(panelX + (int) x, panelY + (int) y, (int) width, (int) height);
                tag.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                tagPanelList.add(tag);
                List<SSNFaceRecognitionResponse.FaceDetection.Matches> matches = face.getMatches();
                String matchString = "";
                final Vector<String> comboList = new Vector<String>();
                for (SSNFaceRecognitionResponse.FaceDetection.Matches match : matches) {
                    String matched = match.getTag();
                    float score = match.getScore();
                    if (score > .75) {
                        comboList.add(matched);
                        matchString+= matched + ",";
                    }
                }
                if(matchString.contains(",")){
                    matchString = matchString.substring(0,matchString.length()-1);
                }
                taggedFace.setTags(matchString);
                if(!exist){
                    try {
                        SSNDao.saveTaggedFaces(taggedFace, currentFile.getAbsolutePath());
                    } catch (SQLException ex) {
                        Logger.getLogger(SSNImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                final JComboBox field = new JComboBox(comboList);
                field.setEditable(true);
                field.setVisible(false);
                int fieldWidth = (int) ((int) width * 1.5);
                int fieldX = panelX + (int) x - (int) ((int) width * 0.25);
                field.setBounds(fieldX, panelY + (int) y + (int) height, fieldWidth, 25);
                field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                layeredPane.add(field, new Integer(1), 0);
                
                tag.addMouseListener(new MouseAdapter() {
                    boolean show = true;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (show) {
                            field.setVisible(true);
                            show = false;
                        } else {
                            field.setVisible(false);
                            show = true;
                        }
                    }
                });
                field.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            String selectedValue = field.getEditor().getItem().toString();
                            if (selectedValue != "" && !selectedValue.equalsIgnoreCase(SSNFaceRecognitionRequest.TAG_VALUE)) {
                                SSNFaceRecognitionRequest faceRenameRequest = new SSNFaceRecognitionRequest();
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.NEW_TAG, selectedValue);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.IMG_INDEX, String.valueOf(imgIndex));
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_RENAME.toString());
                                SSNFaceRecognitionResponse faceRenameResponse = service.getResponse(faceRenameRequest);

                                SSNFaceRecognitionRequest faceTrainRequest = new SSNFaceRecognitionRequest();
                                faceTrainRequest.getParameters().put(SSNFaceRecognitionRequest.TAGS, selectedValue);
                                faceTrainRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_TRAIN.toString());
                                SSNFaceRecognitionResponse faceTrainResponse = service.getResponse(faceTrainRequest);

                                SSNFaceRecognitionRequest faceDeleteRequest = new SSNFaceRecognitionRequest();
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_DELETE.toString());
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.IMG_INDEX, String.valueOf(imgIndex));
                                SSNFaceRecognitionResponse faceDeleteResponse = service.getResponse(faceDeleteRequest);

                                boolean added = comboList.add(selectedValue);
                                if (added) {
                                    try {
                                        SSNGalleryMetaData mdata = SSNDao.getSSNMetaData(homeForm.getCurrentFile().getAbsolutePath());
                                        String newKeywords = "";
                                        if (mdata.getSsnKeywords() == null || mdata.getSsnKeywords().isEmpty()) {
                                            newKeywords = selectedValue;
                                        } else {
                                            boolean exists = false;
                                            if (mdata.getSsnKeywords().contains(",")) {
                                                String[] keywords = mdata.getSsnKeywords().split(",");
                                                for (String s : keywords) {
                                                    if (s.equalsIgnoreCase(selectedValue)) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                            } else {
                                                if (mdata.getSsnKeywords().equalsIgnoreCase(selectedValue)) {
                                                    exists = true;
                                                }
                                            }
                                            if (!exists) {
                                                newKeywords = mdata.getSsnKeywords() + "," + selectedValue;
                                            } else {
                                                newKeywords = mdata.getSsnKeywords();
                                            }
                                        }

                                        String tags = SSNDao.getTaggedFace(homeForm.getCurrentFile().getAbsolutePath(), String.valueOf(imgIndex));
                                        String newTag = "";
                                        if (tags == null || tags.isEmpty()) {
                                            newTag = selectedValue;
                                            SSNDao.updateTaggedFaces(homeForm.getCurrentFile().getAbsolutePath(), String.valueOf(imgIndex), newTag);
                                        } else {
                                               newTag = selectedValue;
                                               SSNDao.updateTaggedFaces(homeForm.getCurrentFile().getAbsolutePath(), String.valueOf(imgIndex), newTag);

                                        }
                                        
                                        homeForm.getSsnHomeRightPanel().removeAll();
                                        homeForm.getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(homeForm.getCurrentFile(), homeForm), BorderLayout.NORTH);
                                        homeForm.getSsnHomeRightPanel().revalidate();
                                    } catch (SQLException ex) {
                                        java.util.logging.Logger.getLogger(SSNHomeModel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            }
                        }
                    }

                });
            }
        
            if (tagPanelList.size() > 0) {
                for (JPanel tag : tagPanelList) {
                    layeredPane.add(tag, new Integer(1), 0);
                }
            }
            
            layeredPane.add(panel2,new Integer(0), 0);
            layeredPane.add(addImgCountLabel(), 1, 0);
            layeredPane.setVisible(true);
            layeredPane.setBackground(new Color(0,0,0,1));
            add(layeredPane, BorderLayout.CENTER);
            revalidate();
            repaint();
            setVisible(true);

        }

    }
}