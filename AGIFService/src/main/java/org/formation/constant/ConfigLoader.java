package org.formation.constant;

import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.formation.service.impl.AgifServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConfigLoader {
    private static final Properties props = new Properties();
    private static final String CONFIG_PATH = System.getenv("CONFIG_PATH"); // Chemin passé en variable d'environnement dans setenv.sh

    static {
        Logger.getLogger(ConfigLoader.class.getName()).log(Level.INFO, "CONFIG_PATH : " + CONFIG_PATH);

        try {
            // Charge d'abord les propriétés par défaut (dans le WAR)
            props.load(ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties"));
            // Surcharge avec le fichier externe (si présent)
            if (CONFIG_PATH != null) {
                try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                    props.load(fis);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger la configuration", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
