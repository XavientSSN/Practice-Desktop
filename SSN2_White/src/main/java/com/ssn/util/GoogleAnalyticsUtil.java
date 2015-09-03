/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.util;

import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
import com.ssn.app.loader.SSNConstants;
import org.apache.log4j.Logger;

/**
 *
 * @author ATripathi4
 */
public class GoogleAnalyticsUtil {
    final    private static Logger logger = Logger.getLogger(GoogleAnalyticsUtil.class);
    
    public static void track(String eventName){
        logger.debug("Tracking event : "+ eventName);
        JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(SSNConstants.SSN_APP_NAME_IN_GOOGLE_ANALYTICS,
                                                            SSNConstants.SSN_APP_VERSION_IN_GOOGLE_ANALYTICS,
                                                            SSNConstants.SSN_APP_TRACKING_ID_IN_GOOGLE_ANALYTICS);

        FocusPoint focusPoint = new FocusPoint(eventName);

        tracker.trackSynchronously(focusPoint);
    }
}
