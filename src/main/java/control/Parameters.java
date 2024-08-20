package control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class Parameters {
    private final Properties prop;
    private static final Logger logger = LogManager.getLogger(Parameters.class);

    public Parameters(String nomFichier) {
        prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(nomFichier);

        try {
            prop.load(input);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public String getVersion() {return prop.getProperty("version");}
    public String getProperty(String nomProperty) {return prop.getProperty(nomProperty);}
}