package com.ssn.webcam;

import com.ssn.app.loader.SSNConstants;
import com.ssn.ui.custom.component.SSNVideoMetadata;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author vkvarma
 */

public class SSNCapturedImageScrollPanel extends JPanel {
    
    private static final long              serialVersionUID   = 1L;
    File                                   folder             = null;
    File[]                                 listOfFiles        = null;    
    private              BufferedImage[]   b                  = null;
    private              long              webcamOpenTime     = 0;  
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNCapturedImageScrollPanel.class);
    public SSNCapturedImageScrollPanel(long webcamOpenTime, File tmpImgFolder) {  
        this.webcamOpenTime = webcamOpenTime;        
        this.folder = tmpImgFolder;
        reloadPanel(this.webcamOpenTime,this.folder);   
        
    }
    
    public void reloadPanel(long webcamOpenTime, File tmpImgFolder) {
        this.folder = tmpImgFolder;
        this.webcamOpenTime = webcamOpenTime;
        listOfFiles = folder.listFiles();

        Arrays.sort(listOfFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                return compare((File) o1, (File) o2);
            }

            private int compare(File f1, File f2) {                        
                    long result = f2.lastModified() - f1.lastModified();
                    if (result > 0) {
                        return 1;
                    } else if (result < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
            }            
        });
        String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
        List<String> videoSupportedList = Arrays.asList(videoSupported);
        
        if(listOfFiles.length > 0){
            b = new BufferedImage[listOfFiles.length>5?5:listOfFiles.length];
            
           
            int count = listOfFiles.length <5 ? listOfFiles.length : 5;
            int index = 0;
             
            for (int i = 0; i< count; i++) {
                
                    try { 
                        String fileExtension = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1, listOfFiles[i].getName().length());
                    if (videoSupportedList.contains(fileExtension.toUpperCase())) {
                        b[index] = ImageIO.read(getClass().getResource("/icon/play.png"));
                    }else{
                        File tempfile = new File(this.folder+ File.separator + listOfFiles[i].getName());
                            b[index] = ImageIO.read(tempfile);

                    }
                      b[index] = resizeImage(b[index],70,70);
                    }catch (Exception ex) {
                       // ex.printStackTrace();
                    }
             index++;
            }
            
            if(b != null && b.length > 0)
               addImageToScrollView(b);        
        }
    }
    
    public void reloadPanel(long webcamOpenTime, File tmpImgFolder,int offset, int toIndex, int fromIndex) {
        this.folder = tmpImgFolder;
        this.webcamOpenTime = webcamOpenTime;
        listOfFiles = folder.listFiles();
        
        Arrays.sort(listOfFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                return compare((File) o1, (File) o2);
            }

            private int compare(File f1, File f2) {                        
                    long result = f2.lastModified() - f1.lastModified();
                    if (result > 0) {
                        return 1;
                    } else if (result < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
            }            
        });
        String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
        List<String> videoSupportedList = Arrays.asList(videoSupported);
        
        if(listOfFiles.length > 5)
            b = new BufferedImage[5];
        else
            b = new BufferedImage[listOfFiles.length>5?5:listOfFiles.length];
        
        int count = 0;
        for (int i = fromIndex; i<toIndex&&count<(toIndex-fromIndex); i++) {
            
                try { 
                    String fileExtension = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1, listOfFiles[i].getName().length());
                if (videoSupportedList.contains(fileExtension.toUpperCase())) {
                    b[count] = ImageIO.read(getClass().getResource("/icon/play.png"));
                }else{
                    File tempfile = new File(this.folder+ File.separator + listOfFiles[i].getName());
                        b[count] = ImageIO.read(tempfile);

                }
                  b[count] = resizeImage(b[count],70,70);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    
                }
                count++;
            }
        
      
        
        if(b != null && b.length > 0)
           addImageToScrollView(b);
        
    }
    
    public  BufferedImage resizeImage(BufferedImage image, int width, int height) {
        int type=0;
        type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width,height,type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
    

    
    public void addImageToScrollView() {
        removeAll();
        for (int j = 0; j < listOfFiles.length; j++) {
            if(b != null && b.length > 0) {
                JLabel imageLabel = new JLabel(new ImageIcon(b[j]));           
                imageLabel.setBorder(new LineBorder(new Color(68,68,68),1, true));
                imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));             
                add(imageLabel); 
            }
        }
        repaint();
    }
     public void addImageToScrollView(BufferedImage[] images) {
        removeAll();
        if(images != null){
            for (int j = 0; j < images.length; j++) {
               
                    try{
                    JLabel imageLabel = new JLabel(new ImageIcon(images[j]));           
                    imageLabel.setBorder(new LineBorder(new Color(68,68,68),1, true));
                    imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));             
                    add(imageLabel); 
                    }catch(Exception ee){
                        ee.printStackTrace();
                    }
               
            }
        }
        repaint();
    }
}