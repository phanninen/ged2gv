package phannin.ged2gv;


import javax.swing.*;

public class Reader {


    public static void main(String[] args) throws Exception {
        Start dialog = new Start();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);


        // loadAndCreate("data/puujalka.ged",  "@I0047@", new String[] {"@I0424@"});
    }

    public static void loadAndCreate(String filename, String startPerson, String[] targetPersons) throws Exception {
        String person = startPerson;
        String[] targets = targetPersons;


//        Pedigree sukupuu = new InMemoryPedigree();
        Pedigree sukupuu = new MongoPedigree();

        Long start = System.currentTimeMillis();

        sukupuu.load(filename);

        System.out.println("loadtime=" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();


        Filter all = Filter.factory().allAncestors(person, sukupuu);
        Filter notall = Filter.factory().forAncestors(person, targets, sukupuu);

        System.out.println("filtertime=" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();

        PedigreeWriter writer = new PedigreeWriter("results/pedigree.dot");
        writer.writePedigree(sukupuu, notall, person);

        System.out.println("writetime=" + (System.currentTimeMillis() - start));

    }
}
