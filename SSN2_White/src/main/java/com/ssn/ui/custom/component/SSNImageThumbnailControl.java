/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNHomeModel;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import java.util.Arrays;

import java.util.Iterator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;

/**
 *
 * @author RDiwakar
 */
// class SSNImageThumbnailControl1 extends JFrame{
//    
//    public static void main(String[] args){
//        SSNImageThumbnailControl sSNImageThumbnailControl = new SSNImageThumbnailControl();
//        SSNImageThumbnailControl1 panel = new SSNImageThumbnailControl1();
//        
////        sSNImageThumbnailControl.add(panel.getSsnImageThumbnailControl("C:\\Users\\rdiwakar\\Documents\\My Received Files\\Sliced_images\\Loading_screen.png"));
////        
////        sSNImageThumbnailControl.setAlwaysOnTop(true);
////        sSNImageThumbnailControl.setVisible(true);
////        sSNImageThumbnailControl.setSize(600, 400);
//    }
//}
public class SSNImageThumbnailControl extends JPanel implements MouseListener{

   public int index ;
    static int iT = 0;
    static int iF = 0;
    private BufferedImage videoFrame = null;
    public SSNImageThumbnailControl() {
    }

    public SSNImageThumbnailControl getSsnImageThumbnailControl(String imagePath, int index){
        //this.setLayout();
        iF = 0;
        BufferedImage thumbImg1 =   null;
        this.index = index;
        BufferedImage image;
        String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
        final List<String> videoSupportedList = Arrays.asList(videoSupported);
        try {

              // add code to check file is video or image  if video then write code to create thumbnail 
            String fileExtention    =   imagePath.substring(imagePath.lastIndexOf(".")+1,imagePath.length());
                if(videoSupportedList.contains(fileExtention.toUpperCase())){
                    
                    
                    IMediaReader reader = null;
                    try{

                        if(true){
                            reader = ToolFactory.makeReader(imagePath);
                            reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
                            reader.addListener(new MediaListenerAdapter() {
                                @Override
                                public void onVideoPicture(IVideoPictureEvent event) {
                                    setVideoFrame(event.getImage());
                                    iF++;
                                }

                            });
                            while (reader.readPacket() == null && iF == 0);
                                thumbImg1 = SSNHelper.resizeImage(getVideoFrame(),50, 50);
                        }
                    }catch(Throwable e){
                        e.printStackTrace();
                    }finally{
                        if(reader != null)
                            reader.close();
                    }
                }else{
                    image       = ImageIO.read(new File(imagePath));
                    thumbImg1   = SSNHelper.resizeImage(image, 50, 50);
                }

        } catch (IOException ex) {
            Logger.getLogger(SSNImageThumbnailControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIcon imageIcon     =   new ImageIcon(thumbImg1);
        JLabel thumbnailLabel   =   new JLabel(imageIcon, SwingConstants.HORIZONTAL);
        JLabel closeLabel   =   new JLabel(new ImageIcon(getClass().getResource("/icon/remove-icon.png")), SwingConstants.HORIZONTAL);
        closeLabel.setFocusable(true);
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(thumbnailLabel);
        this.add(closeLabel);
        closeLabel.addMouseListener(this);
    
        this.setFocusable(true);
        this.setSize(new Dimension(50,50));
        this.setBackground(new Color(0,0,0,1));
        
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      this.setVisible(false);
        SSNHomeModel.removeItemList.add(this.index);
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
    
    public BufferedImage getVideoFrame() {
        return videoFrame;
    }

    /**
     *
     * @param videoFrame
     */
    public void setVideoFrame(BufferedImage videoFrame) {
        this.videoFrame = videoFrame;
    }
    
}
