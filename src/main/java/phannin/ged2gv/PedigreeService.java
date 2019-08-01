package phannin.ged2gv;

import phannin.ged2gv.domain.Person;

import java.io.PrintWriter;
import java.util.stream.Stream;

public class PedigreeService {
    public void createPedigree(String filename, String person, Style style, String[] targets) {
        try {
            //          Pedigree sukupuu = new MongoPedigree();
            Pedigree sukupuu = new InMemoryPedigree();


            sukupuu.load(filename);
            //sukupuu.dump();

            Filter filter;
            if (targets != null && targets.length > 0)
                filter = Filter.factory().forAncestors(person, targets, sukupuu);
            else
                filter = Filter.factory().allAncestors(person, sukupuu);

            switch (style) {
                case ANCESTORS:
                    PedigreeWriter writer = new PedigreeWriter("results/pedigree.dot");
                    writer.writePedigree(sukupuu, filter, person);
                    break;
                case DESENDANTS:
                    DecendantsWriter dwriter = new DecendantsWriter("results/pedigree.dot");
                    dwriter.writeDecendants(sukupuu, filter, person);
                    break;
                case BOTH:
                    PrintWriter printwriter = new PrintWriter("results/pedigree.dot", "UTF-8");
                    printwriter.println("strict digraph G {rankdir=LR;");

                    PedigreeWriter pwriter = new PedigreeWriter(printwriter);
                    pwriter.writeTree(sukupuu, filter, person);

                    for (String spouse : sukupuu.getSpouse(person)) {
                        pwriter.writeTree(sukupuu, Filter.factory().allAncestors(spouse, sukupuu), spouse);
                    }

                    DecendantsWriter dwriter2 = new DecendantsWriter(printwriter);
                    dwriter2.writeTree(sukupuu, filter, person);
                    printwriter.println("}");
                    printwriter.close();
                    break;

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public Stream<Person> getPersons(String filename) {
        Pedigree sukupuu = new InMemoryPedigree();


        try {
            sukupuu.load(filename);
            return sukupuu.getPersons().values().stream()
                    .sorted();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return Stream.empty();
        }
    }

    public enum Style {ANCESTORS, DESENDANTS, BOTH}
}
