package phannin.ged2gv;

import java.io.IOException;

/**
 * Created by pasi on 26.2.2017.
 */
public class MongoLoader {
    public static void main(String[] args) throws IOException {

        String filename = args.length > 0 ? args[0] : "data/Hänninen.ged";
        Pedigree sukupuu = new MongoPedigree();

        Long start = System.currentTimeMillis();

        sukupuu.load(filename);

        System.out.println("loadtime=" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();


    }
}
