/**
 * Singleton qui permet d'écrire les classes et leurs métriques dans un fichier .csv
 */
public class VersionFile extends Writer {

    //region Attributs
    //Instance unique de ClassFile
    private static VersionFile singleInstance = null;
    //endregion

    //region Constructeur
    /**
     * Constructeur de la classe (privé)
     * Initialise le fichier classes.csv et ajoute la ligne avec les noms de métrique
     */
    private VersionFile() {
        super("versions.csv");
        super.add("id_version,NC,mWMC,mcBC");
    }
    //endregion

    //region Méthodes
    /**
     * Méthode qui permet d'obtenir une instance du singleton
     * @return L'instance unique de la classe
     */
    public static VersionFile getInstance() {
        if(singleInstance == null)
            singleInstance = new VersionFile();

        return singleInstance;
    }
    //endregion

}
