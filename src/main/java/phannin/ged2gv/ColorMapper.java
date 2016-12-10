package phannin.ged2gv;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by pasi on 11.9.2016.
 */
public class ColorMapper {
    Properties colors = new Properties();

    public ColorMapper() {
        try {
            colors.load(new FileInputStream("colors.properties"));
            colors.load(new InputStreamReader(new FileInputStream(new File("colors.properties")), Charset.forName("UTF-8")));
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public String getColorFor(String name) {
        for (Object key : colors.keySet()) {
            if (name.startsWith((String) key))
                return colors.getProperty((String) key);
        }
        return null;
    }

    public String getColor(Person person) {
        // http://www.graphviz.org/content/color-names (colorscheme=paired12)


        String colorcheme = colors.getProperty("colorscheme");


        String color = getColorFor(person.getSurname());
        if (color != null)
            return "colorscheme=" + colorcheme + " fillcolor=" + color;
        else
            return " color=black fillcolor=white ";

    }


    public String getLineColor(Person person) {

        String colorcheme = colors.getProperty("colorscheme");


        String color = getColorFor(person.getSurname());
        if (color != null)
            return "colorscheme=" + colorcheme + " color=" + color + " penwidth=4.0 ";
        else
            return " color=black ";


    }


}
