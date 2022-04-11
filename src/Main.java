import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        VersionFile versionFile = VersionFile.getInstance();
        String url = "https://github.com/jfree/jfreechart.git";
        String currentPath = System.getProperty("user.dir");
        String pathRepo = currentPath+"\\Repo";

        String[] version;

        try {

            cloneGit(url, pathRepo);

            version = gitVersion(pathRepo);
            int currentNbClass;
            int total = 1;

            for(int i = 0; i < total; i++) {
                String currentVersion = version[i];
                gitReset(currentVersion, pathRepo, i, total);
                currentNbClass = nbClass(pathRepo);
                versionFile.add(currentVersion+","+currentNbClass);
                runMetric(currentPath, pathRepo);
            }
            versionFile.closeWriter();

            //deleteGit(pathRepo);

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

    public static void gitReset(String version, String path, int current, int total) {

        Runtime rt = Runtime.getRuntime();

        try{

            System.out.println("Git reset en cours " + current + " de " + total +"  ...");
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

    public static void runMetric(String jarPath, String repoPath) {

        Runtime rt = Runtime.getRuntime();

        try (InputStream config = new FileInputStream("config.properties")){

            Properties properties = new Properties();
            properties.load(config);

            String javaVersion = properties.getProperty("javaVersion");

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
