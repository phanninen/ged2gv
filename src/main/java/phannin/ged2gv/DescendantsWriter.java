package phannin.ged2gv;

import phannin.ged2gv.domain.Family;
import phannin.ged2gv.domain.Person;

import java.io.PrintWriter;

/**
 * Created by pasi on 22.1.2017.
 */
public class DescendantsWriter extends DotWriter {


    public DescendantsWriter(String filename) throws Exception {
        super(filename);


    }

    public DescendantsWriter(PrintWriter writer) {
        super(writer);

    }

    public void writeDescendants(Pedigree pedigree, Filter filter, String person) {
        initWriter();
        writeConnections(pedigree, filter, person, true);
        writePersons(pedigree, filter, person);
        writeFamilies(pedigree, filter, person);
        //   printGroups(pedigree, filter);
        closeWriter();
    }


    public void writeTree(Pedigree pedigree, Filter filter, String person) {
        writeConnections(pedigree, filter, person, false);
        writePersons(pedigree, filter, person);
        writeFamilies(pedigree, filter, person);
    }

    private void writeConnections(Pedigree pedigree, Filter filter, String personId, boolean leftToRight) {
        Person person = pedigree.getPerson(personId);
        System.out.println(person.toString());
        if (person != null) { //&& generation<10
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    //               outputFamily(fam, pedigree);
                    if (fam.getHusband().equals(personId) && fam.getWife() != null)
                        writeParentToFamilyConnector(pedigree.getPerson(fam.getWife()), fam, leftToRight);
                    if (personId.equals(fam.getWife()) && fam.getHusband() != null)
                        writeParentToFamilyConnector(pedigree.getPerson(fam.getHusband()), fam, leftToRight);
                    writeParentToFamilyConnector(person, fam, leftToRight);
                    for (String child : fam.getChildren()) {
                        writeFamilyToChildConnector(fam, pedigree.getPerson(child), leftToRight);
                        writeConnections(pedigree, filter, child, leftToRight);
                    }


                }
            }
        }
    }

    private void writePersons(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            writePersonNode(person);
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    //               outputFamily(fam, pedigree);
                    if (fam.getHusband().equals(personId) && fam.getWife() != null && !fam.getWife().isEmpty())
                        writePersonNode(pedigree.getPerson(fam.getWife()));
                    if (personId.equals(fam.getWife()) && fam.getHusband() != null && !fam.getHusband().isEmpty())
                        writePersonNode(pedigree.getPerson(fam.getHusband()));
                    for (String child : fam.getChildren()) {
                        writePersons(pedigree, filter, child);
                    }


                }
            }
        }
    }




    private void writeFamilies(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    writeFamilyNode(pedigree, fam);

                    for (String child : fam.getChildren()) {
                        writeFamilies(pedigree, filter, child);
                    }


                }
            }
        }
    }


}
