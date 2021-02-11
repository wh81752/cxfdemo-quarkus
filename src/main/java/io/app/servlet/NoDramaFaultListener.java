package io.app.servlet;

import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.message.Message;
import org.jboss.logging.Logger;

/**
 * Class Documentation
 *
 * <p>
 * What is the point of this class?
 *
 * @author geronimo1
 */
public class NoDramaFaultListener implements FaultListener {
    static final Logger logger = Logger.getLogger(NoDramaFaultListener.class);

    @Override
    public boolean faultOccurred(Exception e, String s, Message message) {
        logger.info(e.getMessage());
        return false;
    }
}
