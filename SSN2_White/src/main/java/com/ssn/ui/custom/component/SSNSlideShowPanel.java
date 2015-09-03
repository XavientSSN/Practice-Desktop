package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 *
 * @author hvashistha
 */
public class SSNSlideShowPanel extends JFrame implements Runnable,MouseListener, MouseMotionListener, KeyListener {

    private              SSNHomeForm         homeForm            = null;
    private              JLabel              slideshowTollbarLbl = null;
    private              List<ImageIcon> imageList          = null;
    private static       Integer             imagePosInArray     = null;
    private static       Integer             listSize            = null;
    private boolean                          loaded              = false;
    //private boolean                          flag                = false;
    private              Thread              t1, t2;
    private final        JPanel              panel2, mainControlPanel,imgCountPanel;
    private final        JLabel              images;
    private final        JLayeredPane        layeredPane;

    private JButton previousButton, nextButton, closeButton;
    private JToggleButton playButton;

   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNSlideShowPanel.class);
 
    

    public static boolean isSlideShowVisible = true;
    

    public SSNSlideShowPanel(SSNHomeForm homeForm,JLabel slideshowTollbarLbl,List<ImageIcon> imageListMy) {
       // Collections.reverse(imageListMy);

        panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(Color.red);
        this.setBackground(Color.yellow);
        images = new JLabel();
        imgCountPanel = new JPanel();
        mainControlPanel = new JPanel();
        layeredPane = new JLayeredPane();
        try {
            this.homeForm = homeForm;
            setSlideshowTollbarLbl(slideshowTollbarLbl);
            initSlideShow(imageListMy);
        } 
        catch (Exception ex) {            
        }
        
    }

    private void initSlideShow(List<ImageIcon> imageListMy) {
        imageList = imageListMy;
        addImgControl();
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
                 //System.out.println("update///////////////////////"+imageList.size());
                 updatePanel(imageList);
//                    while (flag) {
//                     try {
//                         updatePanel(imageList);
//                         Thread.sleep(2000);
//                     } catch (InterruptedException ex) {
//                         Logger.getLogger(SSNSlideShowPanel.class.getName()).log(Level.SEVERE, null, ex);
//                     }
//                    }                                                
                 setAlwaysOnTop(true); 
             }
        };
        t1.setName("ssn-slideshow");
        t1.setDaemon(true);
        t1.start();
    }
    
    
    private JPanel addImgControl() {
        try {
            
            /*
             Adding button Panel to content Pane
             */
            previousButton = new JButton(new ImageIcon(getClass().getResource("/images/previous-normal.png")));
            playButton = new JToggleButton(new ImageIcon(getClass().getResource("/images/pause-normal-new.png")));
            nextButton = new JButton(new ImageIcon(getClass().getResource("/images/next-normal.png")));
            closeButton = new JButton(new ImageIcon(getClass().getResource("/images/close-normal.png")));
            //playButton.setSelected(true);
            closeButton.setBorder(null);
            closeButton.setActionCommand("Exit");
            closeButton.setToolTipText("Exit Slideshow");
            closeButton.setFocusable(false);
            closeButton.setBorderPainted(false);
            closeButton.setFocusPainted(false);
            closeButton.setContentAreaFilled(false);
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e != null && e.getActionCommand().equalsIgnoreCase("Exit")) {
                        //getSlideshowTollbarLbl().setIcon(new ImageIcon(getClass().getResource("/icon/slideshow-normal.png"))); 
                       // homeForm.repaint();
                        isSlideShowVisible = false;
                        dispose();
                       
                    }    
                }
            });
            
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
                       if(playButton.isSelected()){
                           t1.suspend();
                           playButton.setIcon(new ImageIcon(getClass().getResource("/images/play-normal.png")));
                       }else{
                            t1.resume();   
                            playButton.setIcon(new ImageIcon(getClass().getResource("/images/pause-normal-new.png")));
                       }
                        mainControlPanel.setVisible(true);
                    }
                }
            });
            
            Dimension dim = SSNConstants.SSN_SCREEN_SIZE;
            
            mainControlPanel.add(previousButton);
            mainControlPanel.add(Box.createHorizontalStrut(20));
            mainControlPanel.add(playButton);
            mainControlPanel.add(Box.createHorizontalStrut(20));
            mainControlPanel.add(nextButton);
            mainControlPanel.add(Box.createHorizontalStrut(180));
            mainControlPanel.add(closeButton);            
            
            mainControlPanel.setOpaque(false);
            //mainControlPanel.setBounds(dim.width / 2 - 250, dim.height - 60, 600, 50);
            int x   = (int)(dim.width/100)*30;
            int y   = (int)(dim.height/100)*90;  
            mainControlPanel.setBounds(x, y, dim.width / 2 , dim.height);
            mainControlPanel.setVisible(false);
            
            layeredPane.add(mainControlPanel,new Integer(1), 0);

        } catch (Exception iOException) {
        }

        return mainControlPanel;
    }


    private JPanel addImgCountLabel() {
        
        Dimension dim = SSNConstants.SSN_SCREEN_SIZE;
        
        JLabel imgCount = new JLabel( ( (getImagePosInArray() + 1)+ "/" + imageList.size()) );
        imgCount.setFont(new Font("Century Gothic",Font.BOLD,20));
        imgCount.setForeground(new Color(156,156,156));

        int x   = (int)(dim.width/100)*20;
        int y   = (int)(dim.height/100)*90;  
        imgCountPanel.setBounds(x, y, 250, 50);
        imgCountPanel.setOpaque(false);
        imgCountPanel.setVisible(true);        
        imgCountPanel.removeAll();
        imgCountPanel.add(imgCount);
        
        return imgCountPanel;
    }
    
    
    public JPanel updatePanel(List<ImageIcon> imageListMy) {
        if(!isSlideShowVisible)
        {
            return null;
        }
        imageList = imageListMy;
        ImageIcon imageIcone = null ;   
        for (int i = 0; i < imageList.size(); i++) {
            synchronized (this) {
                setImagePosInArray(i);
            }         
            
            /*
            *  The below line of code displays the smaller images in full-screen mode with stretching & skewing
            */
            
            int newWidth = imageList.get(i).getImage().getWidth(rootPane);
            int newHeight = imageList.get(i).getImage().getHeight(rootPane);
           // System.out.println("newWidth::"+newWidth+"newHeight::"+newHeight+"flag:::"+isSlideShowVisible);
            
             if(newWidth>this.getWidth())
                   newWidth = this.getWidth()-100;
                
             if(newHeight>this.getHeight())
                newHeight = this.getHeight();
                
            
            imageIcone = new ImageIcon(imageList.get(i).getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH));
            images.setIcon(imageIcone);
            images.setHorizontalAlignment(SwingConstants.CENTER);           
            panel2.add(images, BorderLayout.CENTER);
            panel2.revalidate();
            panel2.setBackground(new Color(27, 27, 27));
           // panel2.addMouseListener(this); // listens for own mouse and 
            panel2.addMouseMotionListener(this); // mouse-motion events
            addKeyListener(this);
            panel2.setBounds(0, 0, this.getWidth(), this.getHeight());
            panel2.setVisible(true);            
            layeredPane.add(panel2,0, 0);
           // layeredPane.add(addImgCountLabel(), 1, 0);
            try {
                layeredPane.setVisible(true);
                add(layeredPane, BorderLayout.CENTER);
                revalidate();
                repaint();
               // System.out.println(System.currentTimeMillis());
                Thread.sleep(2000);

//                if (i == (imageList.size() - 1)) {
//                    flag = true;
//                }
                if (i + 1 == imageList.size()) {
                   // t1.stop();
                    homeForm.getHomeModel().startSlideShow();
                }
                if(!isSlideShowVisible)
                {
                    break;
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return panel2;
    }
    
    private void updatePanelWithNextImg(int currentIndex1) {
        /*
        *  The below line of code displays the smaller images in full-screen mode with stretching & skewing
        */
        int newWidth = imageList.get(currentIndex1).getImage().getWidth(rootPane);
        int newHeight = imageList.get(currentIndex1).getImage().getHeight(rootPane);
        
        if(newWidth>this.getWidth())
            newWidth = this.getWidth()-100;
        
        if(newHeight>this.getHeight())
            newHeight = this.getHeight();
        
        images.setIcon(new ImageIcon(imageList.get(currentIndex1).getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH)));
        images.setHorizontalAlignment(SwingConstants.CENTER);           
        panel2.add(images, BorderLayout.CENTER);
        panel2.revalidate();
        panel2.setBackground(new Color(27, 27, 27));
        panel2.setBounds(0, 0, this.getWidth(), this.getHeight());
        panel2.setVisible(true);            
        layeredPane.add(panel2,new Integer(0), 0);
       // layeredPane.add(addImgCountLabel(), 1, 0);
        layeredPane.setVisible(true);
        add(layeredPane, BorderLayout.CENTER);
        revalidate();
        repaint();
        setVisible(true);

        if (currentIndex1 < imageList.size() - 1) {
            currentIndex1 = currentIndex1 + 1;
            synchronized (this) {
                setImagePosInArray(currentIndex1);
            }
        } else {
            return;
        }
        
    }
    
    private void updatePanelWithPreviousImg(int currentIndex1) {
        if (currentIndex1 > 0) {
            currentIndex1 = currentIndex1 - 1;
            synchronized (this) {
                setImagePosInArray(currentIndex1);
            }
        } else {
            return;
        }
        /*
        *  The below line of code displays the smaller images in full-screen mode with stretching & skewing
        */
        int newWidth = imageList.get(currentIndex1).getImage().getWidth(rootPane);
        int newHeight = imageList.get(currentIndex1).getImage().getHeight(rootPane);
        images.setIcon(new ImageIcon(imageList.get(currentIndex1).getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH)));
        images.setHorizontalAlignment(SwingConstants.CENTER);           
        panel2.add(images, BorderLayout.CENTER);
        panel2.revalidate();
        panel2.setBackground(new Color(27, 27, 27));
        panel2.setBounds(0, 0, this.getWidth(), this.getHeight());
        panel2.setVisible(true);            
        layeredPane.add(panel2,new Integer(0), 0);
        //layeredPane.add(addImgCountLabel(), 1, 0);
        layeredPane.setVisible(true);
        add(layeredPane, BorderLayout.CENTER);
        revalidate();
        repaint();
        setVisible(true);
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
               // t1.suspend();
                imgCountPanel.setVisible(true);
                //mainControlPanel.setVisible(true);
             //   playButton = new JButton(new ImageIcon(getClass().getResource("/icons/pause-slideshow.png")));
                mainControlPanel.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(SSNSlideShowPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //t1.resume();
        imgCountPanel.setVisible(false);
        mainControlPanel.setVisible(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //t1.resume();
        mainControlPanel.setVisible(false);
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
                //t1.suspend();
                mainControlPanel.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(SSNSlideShowPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (keyEventObj != null) {
            if (c == KeyEvent.VK_ESCAPE) {
                //getSlideshowTollbarLbl().setIcon(new ImageIcon(getClass().getResource("/icon/slideshow-normal.png"))); 
                //homeForm.repaint();
                keyEventObj.dispose();
                isSlideShowVisible = false;
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
    
}