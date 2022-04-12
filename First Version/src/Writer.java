import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe abstraite qui permet d'écrire un fichier et de le fermer uniquement avec l'appel d'une fonction
 */
abstract public class Writer {

    //region Attributs
    protected String fileName;
    protected FileWriter fr;
    protected BufferedWriter bw;
    //endregion

    //region Constructeur
    /**
     * Constructeur de la classe, ouvre le fichier si possible, sinon affiche une erreur.
     * @param fileName Le nom du fichier à écrire
     */
    public Writer(String fileName) {
        try {

            this.fileName = fileName;
            this.fr = new FileWriter(fileName);
            this.bw = new BufferedWriter(fr);

        } catch (IOException error) {

            System.out.println("Impossible de créer " + fileName);
            System.out.println("Erreur : " + error);
            System.out.println();

        }
    }
    //endregion

    //region Méthodes
    /**
     * Méthode qui permet d'écrire une ligne dans le fichier
     * @param s
     */
    public void add(String s) {
        try{

            bw.append(s);
            bw.newLine();

        } catch (IOException error) {

            System.out.println("Impossible d\'ecrire dans " + fileName);
            System.out.println("Erreur : " + error);
            System.out.println();

        }
    }

    /**
     * Méthode qui permet de fermer le fichier
     */
    public void closeWriter() {
        try {
            bw.close();
        } catch (IOException error) {
            System.out.println("Impossible de fermer le fichier" + fileName);
            System.out.println("Erreur : " + error);
            System.out.println();
        }
    }
    //endregion

}
