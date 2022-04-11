import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        VersionFile versionFile = VersionFile.getInstance();
        String url = args[0];
        String currentPath = System.getProperty("user.dir");
        String pathRepo = currentPath+"\\Repo";

        String[] version;
        String javaVersion = "";
        int total = 0;

        try (InputStream config = new FileInputStream("config.properties")){
            Properties properties = new Properties();
            properties.load(config);

            javaVersion = properties.getProperty("javaVersion");
            total = Integer.parseInt(properties.getProperty("total"));
        } catch(Exception e) {
            System.out.println(e);
        }

        try {

            cloneGit(url, pathRepo);

            version = gitVersion(pathRepo);

            //Random index
            total = Math.min(total,version.length);
            int[] index = new int[total];
            float space = ((float) version.length)/((float) total);

            for(int i = 0; i < total; i++) {
                index[i] = Math.round(space * i);
            }
            Arrays.sort(index);

            //Calcul des métriques et écriture
            int currentNbClass;

            for(int i = 0; i < total; i++) {
                String currentVersion = version[index[i]];
                gitReset(currentVersion, pathRepo, i, total, index[i]);

                currentNbClass = nbClass(pathRepo);

                runMetric(currentPath, pathRepo, javaVersion);
                float[] sumMetric = sumMetric();
                sumMetric[0] /= (float) currentNbClass;
                sumMetric[1] /= (float) currentNbClass;

                versionFile.add(currentVersion+","+currentNbClass+','+sumMetric[0]+","+sumMetric[1]);

            }
            versionFile.closeWriter();

            deleteGit(pathRepo);

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static void cloneGit(String url, String path) {

        Runtime rt = Runtime.getRuntime();

        try{

            System.out.println("Git clone en cours ...");
            String[] cmd = {"cmd.exe", "/c", "git clone "+url + " " +path};
            Process proc = rt.exec(cmd);
            proc.waitFor();
            System.out.println("Git clone fini !\n");

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static String[] gitVersion(String path) {
        Runtime rt = Runtime.getRuntime();
        String[] version = {};

        try{

            ArrayList<String> versionList = new ArrayList<>();

            System.out.println("Git rev-list en cours ...");
            String[] cmd = {"cmd.exe", "/c", "cd "+path+" && git rev-list master"};
            Process proc = rt.exec(cmd);
            BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line;
            while (true) {
                line = r.readLine();
                if (line == null)
                    break;
                versionList.add(line);
            }
            proc.waitFor();
            System.out.println("Git rev-list fini !\n");
            version = new String[versionList.size()];
            for(int i = 0; i < versionList.size(); i++) {
                version[i] = versionList.get(i);
            }

        } catch(Exception e) {
            System.out.println(e);
        }

        return version;
    }

    public static void gitReset(String version, String path, int current, int total, int index) {

        Runtime rt = Runtime.getRuntime();

        try{

            System.out.println("Git reset en cours " + current + " de " + total +"\nNuméro de l'index : "+index+" ...");
            String[] cmd = {"cmd.exe", "/c", "cd "+path+" && git reset --hard " + version};
            Process proc = rt.exec(cmd);
            proc.waitFor();
            System.out.println("Git reset terminé !\n");

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static void deleteGit(String path) {

        Runtime rt = Runtime.getRuntime();

        try{

            System.out.println("rmdir en cours ...");
            String[] cmd = {"cmd.exe", "/c", "rmdir /s /q "+path};
            rt.exec(cmd);
            System.out.println("rmdir fini !\n");

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static void runMetric(String jarPath, String repoPath, String javaVersion) {


        try{

            System.out.println("Calcule des métrique ...");

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
            System.out.println("Calcule des métriques fini !\n");

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

    public static int nbClass(String path) {

        int nbClass = 0;
        File currentFile = new File(path);

        String[] subFiles = currentFile.list();

        for(int i = 0; i < subFiles.length; i++) {

            File newFile = new File(path + "\\" +subFiles[i]);


            if(newFile.isDirectory()) {

                nbClass += nbClass(path + "\\" + subFiles[i]);

            } else {

                String[] pathSplit = subFiles[i].split("\\.");
                if(pathSplit.length > 1 && pathSplit[pathSplit.length-1].equals("java")) {
                    nbClass++;
                }

            }

        }

        return nbClass;

    }

}
