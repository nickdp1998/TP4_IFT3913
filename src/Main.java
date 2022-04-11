import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        String url = "https://github.com/jfree/jfreechart.git";
        String currentPath = System.getProperty("user.dir");
        String pathRepo = currentPath+"\\Repo";

        Runtime rt = Runtime.getRuntime();

        ArrayList<String> versionList = new ArrayList<>();
        String[] version;

        try {
            //region Create Git Repo
            System.out.println("Git clone en cours ...\n");
            Process proc1 = rt.exec("git clone " + url + " Repo");
            proc1.waitFor();
            System.out.println("Git clone fini !\n");
            //endregion

            //region Getting version
            System.out.println("Git rev-list en cours ...\n");
            String[] cmd1 = {"cmd.exe", "/c", "cd "+pathRepo+" && git rev-list master"};
            Process proc2 = rt.exec(cmd1);
            BufferedReader r = new BufferedReader(new InputStreamReader(proc2.getInputStream()));

            String line;
            while (true) {
                line = r.readLine();
                if (line == null)
                    break;
                versionList.add(line);
            }
            proc2.waitFor();
            System.out.println("Git rev-list fini !\n");
            version = new String[versionList.size()];
            for(int i = 0; i < versionList.size(); i++) {
                version[i] = versionList.get(i);
            }
            //endregion

            
            String currentVersion = version[0];
            System.out.println("Git reset en cours ...\n");
            String[] cmd3 = {"cmd.exe", "/c", "cd "+pathRepo+" && git reset --hard " + currentVersion};
            Process proc3 = rt.exec(cmd3);
            proc3.waitFor();
            System.out.println("Git reset terminé !\n");

            int currentNbClass = nbClass(pathRepo);


            //region Delete Git Repo
            System.out.println("rmdir en cours ...\n");
            String[] cmd2 = {"cmd.exe", "/c", "rmdir /s /q "+pathRepo};
            rt.exec(cmd2);
            System.out.println("rmdir en terminé !\n");
            //endregion


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
