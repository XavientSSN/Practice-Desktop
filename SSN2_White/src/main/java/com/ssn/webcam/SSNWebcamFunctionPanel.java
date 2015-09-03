package com.ssn.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.ui.custom.component.SSNHorizontalScrollbarUI;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author vkvarma
 */
public class SSNWebcamFunctionPanel extends JPanel {

    private static Logger log = Logger.getLogger(SSNWebcamFunctionPanel.class);
    private static final long serialVersionUID = 1L;
  
    private JLabel capturePhotoBtn = null;
    private JLabel captureVideoBtn = null;
    private JLabel stopVideoBtn = null;
    private JPanel camButtonPanel = null;
    
    private JLabel leftButton = null;
    private JLabel rightButton = null;
    
    private JScrollPane scrollableArea = null;
    private volatile Boolean captureVideo = true;
    public TargetDataLine line = null;
    private static int offsets = 0;
    private static int toIndex = 1;
    private static int fromIndex = 0;

    public Boolean isCaptureVideo() {
        return captureVideo;
    }

    public static int getOffsets() {
        return offsets;
    }

    public static void setOffsets(int offsets) {
        SSNWebcamFunctionPanel.offsets = offsets;
    }

    public static int getToIndex() {
        return toIndex;
    }

    public static void setToIndex(int toIndex) {
        SSNWebcamFunctionPanel.toIndex = toIndex;
    }

    public static int getFromIndex() {
        return fromIndex;
    }

    public static void setFromIndex(int fromIndex) {
        SSNWebcamFunctionPanel.fromIndex = fromIndex;
    }

    public void setCaptureVideo(Boolean captureVideo) {
        this.captureVideo = captureVideo;
    }

    public JLabel getStopVideoBtn() {
        return stopVideoBtn;
    }

    public void setStopVideoBtn(JLabel stopVideoBtn) {
        this.stopVideoBtn = stopVideoBtn;
    }
    private Webcam ssnWebcam = null;
    private SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel = null;
    private long webcamOpenTime = 0;
    public SSNWebcamPanel panel = null;

    public SSNWebcamFunctionPanel(final Webcam ssnWebcam, long webcamOpenTime, SSNWebcamPanel panel) {
        initGUIComponent(ssnWebcam, webcamOpenTime, panel);
    }

    /**
     * @return the captureVideoBtn
     */
    public JLabel getCaptureVideoBtn() {
        return captureVideoBtn;
    }

    /**
     * @param captureVideoBtn the captureVideoBtn to set
     */
    public void setCaptureVideoBtn(JLabel captureVideoBtn) {
        this.captureVideoBtn = captureVideoBtn;
    }

    private void initGUIComponent(final Webcam ssnWebcam, long webcamOpenTime, final SSNWebcamPanel panel) {
        this.panel = panel;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350, 300));
        this.webcamOpenTime = webcamOpenTime;
        setSsnWebcam(ssnWebcam);
        camButtonPanel = new JPanel();
        camButtonPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        camButtonPanel.setBorder(BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BORDER_COLOR));

//        setCapturePhotoBtn(new JButton("Capture", new ImageIcon(getClass().getResource("/images/ssn-camera-click.png"))));
//        setCaptureVideoBtn(new JButton("Record", new ImageIcon(getClass().getResource("/images/ssn-video-capture.png"))));
//        setStopVideoBtn(new JButton("Stop", new ImageIcon(getClass().getResource("/images/ssn-video-capture.png"))));

        setCapturePhotoBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/camera_icon.png"))));
        setCaptureVideoBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/video_ico.png"))));
        setStopVideoBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/video_ico.png"))));

        getCaptureVideoBtn().setEnabled(true);
        //getCaptureVideoBtn().setToolTipText("This feature will be available Soon");
        // getStopVideoBtn().setVisible(false);
     
        
        camButtonPanel.add(getCapturePhotoBtn());
        camButtonPanel.add(Box.createHorizontalStrut(20));
        camButtonPanel.add(getCaptureVideoBtn());
       // JLabel ssnRecentPhotosLabel = new JLabel("Recent Photos :");
//      by ritesh  ssnRecentPhotosLabel.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
//        ssnRecentPhotosLabel.setBounds(5,250,20,10);
//        camButtonPanel.add(ssnRecentPhotosLabel);
//                
        //camButtonPanel.add(getStopVideoBtn());
        setSsnCapturedImageScrollPanel(new SSNCapturedImageScrollPanel(webcamOpenTime, panel.getTmpImageDir()));
        getSsnCapturedImageScrollPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        scrollableArea = new JScrollPane(getSsnCapturedImageScrollPanel());
        //scrollableArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scrollableArea.getHorizontalScrollBar().setUI(new SSNHorizontalScrollbarUI());
        //scrollableArea.setBorder(new LineBorder(new Color(68,68,68),2, true));
        scrollableArea.setBorder(BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BORDER_COLOR));
        add(camButtonPanel, BorderLayout.PAGE_START);
        
        
         Border paddingBorder = BorderFactory.createEmptyBorder(-5, 10, 10, 10);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        JScrollPane s = new JScrollPane();
//        s.setBorder(border);
        s.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        //setBorder(border);
        
        
        s.getHorizontalScrollBar().setUI(new SSNHorizontalScrollbarUI());
        Border borderVer = BorderFactory.createEmptyBorder(0, 0, 0, 2);
        s.setBorder(borderVer);
        getSsnCapturedImageScrollPanel().setBorder(border);
        
        JPanel thumbnailPanel = new JPanel(new FlowLayout());
        thumbnailPanel.add(getSsnCapturedImageScrollPanel());
        thumbnailPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        
       setLeftButton(new JLabel(new ImageIcon(getClass().getResource("/icon/take_me_arrow.png"))));
        leftButton.setName("leftArrow");
        add(Box.createHorizontalStrut(1));
        add(thumbnailPanel, BorderLayout.CENTER);
        
        
        setRightButton( new JLabel(new ImageIcon(getClass().getResource("/icon/take_me_arrow2.png"))));
        
        
        rightButton.setName("rightArrow");
        
        leftButton.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        rightButton.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        leftButton.setOpaque(true);
        rightButton.setOpaque(true);
        
        add(leftButton, BorderLayout.WEST);
        add(rightButton, BorderLayout.EAST);
        setFromIndex(panel.getTmpImageDir().listFiles().length > 5 ? 0 : -1);
        setToIndex(panel.getTmpImageDir().listFiles().length > 5 ? 5 : -1);
        
        if(getToIndex() == -1 )
            getRightButton().setEnabled(false);
        
        if(getFromIndex() == -1 )
            getLeftButton().setEnabled(false);
        
            
        getLeftButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
               
                setFromIndex(getFromIndex()-5);
                setToIndex(getFromIndex()+5);
                
                if(leftButton.isEnabled())
                    new SSNWebcamFunctionPanel.PaintWorker(ssnCapturedImageScrollPanel,getOffsets(),getToIndex(),getFromIndex()).execute();
                
                if(getFromIndex()  >0){
                    leftButton.setEnabled(true);
                }else{
                    leftButton.setEnabled(false);
                }
                if(getToIndex() < panel.getTmpImageDir().listFiles().length){
                    rightButton.setEnabled(true);
                }else{
                    rightButton.setEnabled(false);
                }
                
                
               
            }

            @Override
            public void mousePressed(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        getRightButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                setFromIndex(panel.getTmpImageDir().listFiles().length >5 ? toIndex+1 : toIndex);
                setToIndex(getFromIndex()+5);
                
                if(rightButton.isEnabled())
                    new SSNWebcamFunctionPanel.PaintWorker(ssnCapturedImageScrollPanel,getOffsets(),getToIndex(),getFromIndex()).execute();
                
                if(getFromIndex()  >0){
                    leftButton.setEnabled(true);
                }else{
                    leftButton.setEnabled(false);
                }
                if(getToIndex() < panel.getTmpImageDir().listFiles().length){
                    rightButton.setEnabled(true);
                }else{
                    rightButton.setEnabled(false);
                }
                
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        getCapturePhotoBtn().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if(panel.getTmpImageDir().listFiles().length > 5){
                    rightButton.setEnabled(true);
                }
                 captureImage();
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
        getCaptureVideoBtn().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                  new SSNWebcamFunctionPanel.PaintWorker2(camButtonPanel, true).execute();
                  captureVideo();
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
        getStopVideoBtn().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                stopVideo();
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

    }

    public void stopVideo() {
        new SSNWebcamFunctionPanel.PaintWorker2(camButtonPanel, false).execute();
        setCaptureVideo(false);


        
        new SSNWebcamFunctionPanel.PaintWorker(ssnCapturedImageScrollPanel).execute();
    }
// by ritesh for captur image
    private class PaintWorker extends SwingWorker<SSNCapturedImageScrollPanel, SSNCapturedImageScrollPanel> {

        private SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel;

        public PaintWorker(SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel) {
            this.ssnCapturedImageScrollPanel = ssnCapturedImageScrollPanel;
        }
        //start by ritesh
        int offsets = -1;
        int toIndex = -1;
        int fromIndex=-1;
        public PaintWorker(SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel,int offsets,int toIndex, int fromIndex) {
            this.ssnCapturedImageScrollPanel = ssnCapturedImageScrollPanel;
            this.offsets = offsets;
            this.toIndex = toIndex;
            this.fromIndex = fromIndex;
        }
        //end by ritesh
        @Override
        protected SSNCapturedImageScrollPanel doInBackground() throws Exception {
            if(toIndex != -1 && fromIndex != -1 && offsets != -1)
                ssnCapturedImageScrollPanel.reloadPanel(webcamOpenTime, panel.getTmpImageDir(),offsets,toIndex,fromIndex);
            else
                ssnCapturedImageScrollPanel.reloadPanel(webcamOpenTime, panel.getTmpImageDir());
            
            ssnCapturedImageScrollPanel.revalidate();
            ssnCapturedImageScrollPanel.repaint();
            revalidate();
            repaint();
            return ssnCapturedImageScrollPanel;
        }
    }
    //  for captur image
 private class PaintWorker3 extends SwingWorker<SSNCapturedImageScrollPanel, SSNCapturedImageScrollPanel> {

        private SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel;

        public PaintWorker3(SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel) {
            this.ssnCapturedImageScrollPanel = ssnCapturedImageScrollPanel;
        }

        @Override
        protected SSNCapturedImageScrollPanel doInBackground() throws Exception {
            
            ssnCapturedImageScrollPanel.reloadPanel(webcamOpenTime, panel.getTmpImageDir());
            ssnCapturedImageScrollPanel.revalidate();
            ssnCapturedImageScrollPanel.repaint();
            revalidate();
            repaint();
            return ssnCapturedImageScrollPanel;
        }
    }
 // for capturing video
    private class PaintWorker2 extends SwingWorker<JPanel, JPanel> {

        private JPanel buttonPanel;
        private boolean buttonToShow;

        public PaintWorker2(JPanel buttonPanel, boolean buttonToShow) {
            this.buttonPanel = buttonPanel;
            this.buttonToShow = buttonToShow;
        }

        @Override
        protected JPanel doInBackground() throws Exception {
            
            // ssnCapturedImageScrollPanel.reloadPanel(webcamOpenTime);
            if (buttonToShow) {
                buttonPanel.remove(getCaptureVideoBtn());
                buttonPanel.add(getStopVideoBtn());
            } else {
                buttonPanel.remove(getStopVideoBtn());
                buttonPanel.add(getCaptureVideoBtn());
            }
            buttonPanel.revalidate();
            buttonPanel.repaint();
            revalidate();
            repaint();
            return buttonPanel;
        }
    }

    /**
     * @return the capturePhotoBtn
     */
    public JLabel getCapturePhotoBtn() {
        return capturePhotoBtn;
    }

    /**
     * @param capturePhotoBtn the capturePhotoBtn to set
     */
    public void setCapturePhotoBtn(JLabel capturePhotoBtn) {
        this.capturePhotoBtn = capturePhotoBtn;
    }

    /**
     * @return the ssnWebcam
     */
    public Webcam getSsnWebcam() {
        return ssnWebcam;
    }

    /**
     * @param ssnWebcam the ssnWebcam to set
     */
    public void setSsnWebcam(Webcam ssnWebcam) {
        this.ssnWebcam = ssnWebcam;
    }

    /**
     * @return the ssnCapturedImageScrollPanel
     */
    public SSNCapturedImageScrollPanel getSsnCapturedImageScrollPanel() {
        return ssnCapturedImageScrollPanel;
    }

    /**
     * @param ssnCapturedImageScrollPanel the ssnCapturedImageScrollPanel to set
     */
    public void setSsnCapturedImageScrollPanel(SSNCapturedImageScrollPanel ssnCapturedImageScrollPanel) {
        this.ssnCapturedImageScrollPanel = ssnCapturedImageScrollPanel;
    }

    public JLabel getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(JLabel leftButton) {
        this.leftButton = leftButton;
    }

    public JLabel getRightButton() {
        return rightButton;
    }

    public void setRightButton(JLabel rightButton) {
        this.rightButton = rightButton;
    }

    public void captureImage() {
        //File theDir = new File(panel.getTmpImageDir().getName());
        String prefix = getImagePrefixPreference();
        if(!StringUtils.equals("", prefix)){
            prefix = prefix+"_";
        }
        File theDir = this.panel.getTmpImageDir();
        if (theDir.exists()) {
            String fileNamePart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            fileNamePart = fileNamePart.replace(":", "_").replace("-", "_").replace(" ", "_");
            
            WebcamUtils.capture(getSsnWebcam(), panel.getTmpImageDir() + File.separator + prefix + fileNamePart, ImageUtils.FORMAT_JPG);

           
            new SSNWebcamFunctionPanel.PaintWorker(ssnCapturedImageScrollPanel).execute();
        }
    }

    public void captureVideo() {

        Thread thread;
        thread = new Thread() {

            @Override
            public void run() {
                setCaptureVideo(true);
                int channelCount = 1;
                float sampleRate = 44100.0F;
                String fileNamePart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                fileNamePart = fileNamePart.replace(":", "_").replace("-", "_").replace(" ", "_");
                String prefix = getVideoPrefixPreference();
                if(!StringUtils.equals("", prefix)){
                    prefix = prefix+"_";
                }
                
                File theDir = new File(panel.getTmpImageDir() + File.separator + prefix +fileNamePart + ".mp4");
                IMediaWriter writer = ToolFactory.makeWriter(theDir.getAbsolutePath());
                Dimension size = WebcamResolution.QVGA.getSize();

                writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, size.width, size.height);
                writer.addAudioStream(1, 0, ICodec.ID.CODEC_ID_AAC, channelCount, (int)sampleRate);
                Webcam webcam = Webcam.getDefault();
                long start = System.nanoTime();
                int i = 0;
                IContainer container = writer.getContainer();
                IStream stream = container.getStream(1);
                stream.getStreamCoder().getDefaultAudioFrameSize();

                AudioFormat audioFormat = new AudioFormat(sampleRate, (int) 16, channelCount, true, false);
                //AudioFormat audioFormat = new AudioFormat(44100.0F, 8, 2, true,false);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
                
                   try{ 
                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(audioFormat);
                } catch (LineUnavailableException ex) {
                    log.error(ex);
                }

                line.start();

                while (isCaptureVideo()) {
                    
                    BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
                    IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

                    IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
                    frame.setKeyFrame(i == 0);
                    frame.setQuality(0);
                    long time = System.nanoTime() - start;
                    // encode the image to stream #0 
                    SSNWebcamFunctionPanel.this.panel.getPanel().add(new WebcamPanel(getSsnWebcam()), true);
                    writer.encodeVideo(0, image, time, TimeUnit.NANOSECONDS);
                    short[] samples1 = customAudioStream();
                    writer.encodeAudio(1, samples1);
                    i++;
                }
                line.close();
                writer.close();

            }

        };
        thread.start();

    }

    public short[] customAudioStream() {
        byte[] data = new byte[line.getBufferSize()];
        int sz = line.read(data, 0, data.length);
        short[] audioSamples = new short[sz / 2];
        for (int i = 0; i < sz / 2; i++) {
            audioSamples[i] = (short) ((data[2 * i + 1] << 8) | data[2 * i]);

        }
        return (audioSamples);
    }
    
    public static String getImagePrefixPreference(){
        String prefix="";
        
        Map<String,String> preferences = null;
        try {
            preferences = SSNDao.getPreferences();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SSNWebcamFunctionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(preferences != null){
            prefix = preferences.get(SSNConstants.SSN_IMAGE_PREFIX);
            prefix = prefix==null?"":prefix;
        }
        return prefix.trim();
    }
    
    public static String getVideoPrefixPreference(){
        String prefix="";
        
        Map<String,String> preferences = null;
        try {
            preferences = SSNDao.getPreferences();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SSNWebcamFunctionPanel.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        if(preferences != null){
            prefix = preferences.get(SSNConstants.SSN_VIDEO_PREFIX);
            prefix = prefix == null ? "" : prefix;
        }
        return prefix.trim();
    }
    public Vector<AudioFormat> getSupportedFormats(Class<?> dataLineClass) {
    /*
     * These define our criteria when searching for formats supported
     * by Mixers on the system.
     */
    float sampleRates[] = { (float) 8000.0, (float) 16000.0, (float) 44100.0 };
    int channels[] = { 1, 2 };
    int bytesPerSample[] = { 2 };

    AudioFormat format;
    DataLine.Info lineInfo;

  //  SystemAudioProfile profile = new SystemAudioProfile(); // Used for allocating MixerDetails below.
    Vector<AudioFormat> formats = new Vector<AudioFormat>();

    for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
        for (int a = 0; a < sampleRates.length; a++) {
            for (int b = 0; b < channels.length; b++) {
                for (int c = 0; c < bytesPerSample.length; c++) {
                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                            sampleRates[a], 8 * bytesPerSample[c], channels[b], bytesPerSample[c],
                            sampleRates[a], false);
                    lineInfo = new DataLine.Info(dataLineClass, format);
                    if (AudioSystem.isLineSupported(lineInfo)) {
                        /*
                         * TODO: To perform an exhaustive search on supported lines, we should open
                         * TODO: each Mixer and get the supported lines. Do this if this approach
                         * TODO: doesn't give decent results. For the moment, we just work with whatever
                         * TODO: the unopened mixers tell us.
                         */
                        if (AudioSystem.getMixer(mixerInfo).isLineSupported(lineInfo)) {
                            formats.add(format);
                        }
                    }
                }
            }
        }
    }
    return formats;
}
}
