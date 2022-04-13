import com.sun.org.apache.xpath.internal.operations.Bool;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        //region Variables
        VersionFile versionFile = VersionFile.getInstance();
        String url = args[0];
        String currentPath = System.getProperty("user.dir");
        String pathRepo = currentPath+"/Repo";

        String[] version;
        String javaVersion = "";
        int total = 0;
        //endregion

        //region Config.properties
        try {
            InputStream config = new FileInputStream("config.properties");
            Properties properties = new Properties();
            properties.load(config);

            javaVersion = properties.getProperty("javaPath");
            total = Integer.parseInt(properties.getProperty("sampleSize"));
        } catch(Exception e) {
            System.out.println(e);
        }
        //endregion

        try {

            deleteGit(pathRepo);
            Git git;
            System.out.println("+-------------------------------------------------------------+");
            System.out.println("+ Git clone en cours ...");

            git = Git.cloneRepository().setURI(url).setDirectory(new File(pathRepo)).call();

            System.out.println("+ Git clone fini !");
            System.out.println("+-------------------------------------------------------------+\n");

            version = gitVersion(pathRepo, git);
            int nbVersion = version.length;

            //region Choix des versions
            total = Math.min(total, nbVersion);
            Random r = new Random();
            int[] index = new int[total];

            //Random si moins de 50% du nb de version, sinon distances égales
            if((float) total < 0.5*((float) nbVersion)) {
                for (int i = 0; i < total; i++) {

                    int currentRandom = r.nextInt(nbVersion);
                    while (arraysContains(index, currentRandom, i)) {
                        currentRandom = r.nextInt(nbVersion);
                    }

                    index[i] = currentRandom;

                }
                Arrays.sort(index);
            } else {
                float distance = ((float) nbVersion)/((float) total);
                for(int i = 0; i < total; i++) {
                    index[i] = Math.round(distance*i);
                }
            }
            //endregion

            //region Calcul des métriques et écriture
            int currentNbClass;

            for(int i = 0; i < total; i++) {
                String currentVersion = version[index[i]];
                gitReset(currentVersion, i, total-1, index[i], git);

                currentNbClass = nbClass(pathRepo);

                runMetric(currentPath, pathRepo, javaVersion);
                float[] sumMetric = sumMetric();
                sumMetric[0] /= (float) currentNbClass;
                sumMetric[1] /= (float) currentNbClass;

                versionFile.add(currentVersion+","+currentNbClass+','+sumMetric[0]+","+sumMetric[1]);

            }
            versionFile.closeWriter();
            git.close();
            deleteGit(pathRepo);
            (new File(currentPath + "/paquets.csv")).delete();
            (new File(currentPath + "/classes.csv")).delete();

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static String[] gitVersion(String path, Git git) {
        String[] version = {};

        try{

            ArrayList<String> versionList = new ArrayList<>();

            System.out.println("+-------------------------------------------------------------+");
            System.out.println("+ Git rev-list en cours ...");
            Repository repository = new FileRepository(path+"/.git");
            String treeName = "refs/heads/master";
            for (RevCommit commit : git.log().add(repository.resolve(treeName)).call()) {
                versionList.add(commit.getName());
            }
            System.out.println("+ Git rev-list fini !");
            System.out.println("+-------------------------------------------------------------+\n");
            version = new String[versionList.size()];
            for(int i = 0; i < versionList.size(); i++) {
                version[i] = versionList.get(i);
            }

        } catch(Exception e) {
            System.out.println(e);
        }

        return version;
    }

    public static void gitReset(String version, int current, int total, int index, Git git) {

        try{

            System.out.println("+-------------------------------------------------------------+");
            System.out.println("+ Git reset en cours " + current + " de " + total +"\n+ Numéro de l'index : "+index+" ...");

            git.reset().setMode(ResetCommand.ResetType.HARD).setRef(version).call();

            System.out.println("+ Git reset terminé !");

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static void deleteGit(String path) {

        try{

            System.out.println("+-------------------------------------------------------------+");
            System.out.println("+ Suppression en cours ...");

            deleteRec(new File(path));

            System.out.println("+ Suppression fini !");
            System.out.println("+-------------------------------------------------------------+\n");

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    private static void deleteRec(File file) {
        if (file.isDirectory()) {
            for (File c : file.listFiles()) {
                deleteRec(c);
            }
        }
        file.delete();
    }

    public static Boolean arraysContains(int[] array, int element, int maxIndex) {

        for(int i = 0; i < maxIndex; i++)
            if(array[i] == element)
                return true;

        return false;

    }

    public static int nbClass(String path) {

        int nbClass = 0;
        File currentFile = new File(path);

        String[] subFiles = currentFile.list();

        for(int i = 0; i < subFiles.length; i++) {

            File newFile = new File(path + "/" +subFiles[i]);


            if(newFile.isDirectory()) {

                nbClass += nbClass(path + "/" + subFiles[i]);

            } else {

                String[] pathSplit = subFiles[i].split("\\.");
                if(pathSplit.length > 1 && pathSplit[pathSplit.length-1].equals("java")) {
                    nbClass++;
                }

            }

        }

        return nbClass;

    }

    public static void runMetric(String jarPath, String repoPath, String javaVersion) {

        try {

            System.out.println("+ \tCalcule des métrique ...");

            Process proc;
            if(javaVersion.equals("")) {
                ProcessBuilder pb = new ProcessBuilder("java", "-jar", "IFT3913_TP1.jar",repoPath);
                pb.directory(new File(jarPath));
                proc = pb.start();
            } else {
                ProcessBuilder pb = new ProcessBuilder(javaVersion, "-jar", "IFT3913_TP1.jar",repoPath);
                pb.directory(new File(jarPath));
                proc = pb.start();
            }
            proc.waitFor();
            System.out.println("+ \tCalcule des métriques fini !");
            System.out.println("+-------------------------------------------------------------+\n");

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static float[] sumMetric() {

        float[] sums = {0,0};

        try{

            FileReader fr = new FileReader("classes.csv");
            BufferedReader reader = new BufferedReader(fr);

            String s = reader.readLine();

            while((s = reader.readLine()) != null) {

                String[] split = s.split(",");
                sums[0] += Float.parseFloat(split[5]);
                sums[1] += Float.parseFloat(split[6]);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return sums;

    }

}
