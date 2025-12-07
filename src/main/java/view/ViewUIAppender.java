
package view;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.LogEvent;

import java.io.Serializable;

public class ViewUIAppender extends AbstractAppender {
    protected ViewUIAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout, false, null);
    }

    @Override
    public void append(LogEvent event) {
        String message = new String(getLayout().toByteArray(event));
        ViewUI.appendLogStatic(message);
    }

    public static ViewUIAppender createAppender() {
        return new ViewUIAppender("ViewUIAppender", null, PatternLayout.createDefaultLayout());
    }
}
