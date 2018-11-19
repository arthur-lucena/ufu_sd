package br.ufu.sd.work.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    Properties prop = new Properties();
    InputStream input = null;
    String fileName;

    public Configuration(String fileName) {
        this.fileName = fileName;
        configure(fileName);
    }

    public Properties getProp() {
        return prop;
    }

    private void configure(String fileName) {
        try {
            input = Configuration.class.getClassLoader().getResourceAsStream(fileName);
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return;
            }

            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
