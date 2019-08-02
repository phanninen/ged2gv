package phannin.ged2gv;

import phannin.ged2gv.domain.Person;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by pasi on 11.9.2016.
 */
public class ColorMapper {
    private final Properties colors = new Properties();

    public ColorMapper() {
        try {
            colors.load(new FileInputStream("colors.properties"));
            colors.load(new InputStreamReader(new FileInputStream(new File("colors.properties")), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private String getColorFor(String name) {
        for (Object key : colors.keySet()) {
            if (name.startsWith((String) key))
                return colors.getProperty((String) key);
        }
        return null;
    }

    public String getColor(Person person) {
        // http://www.graphviz.org/content/color-names (colorscheme=paired12)


        String colorscheme = colors.getProperty("colorscheme");


        String color = getColorFor(person.getSurname());
        if (color != null)
            return "colorscheme=" + colorscheme + " fillcolor=" + color;
        else
            return " color=black fillcolor=white ";

    }


    public String getLineColor(Person person) {

        String colorscheme = colors.getProperty("colorscheme");


        String color = getColorFor(person.getSurname());
        if (color != null && color.equals("6"))
            return " color=grey ";
        else if (color != null)
            return "colorscheme=" + colorscheme + " color=" + color + " penwidth=4.0 ";

        else
            return " color=black ";


    }


}
