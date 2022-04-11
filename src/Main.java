import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        String url = "https://github.com/jfree/jfreechart.git";
        String currentPath = System.getProperty("user.dir");
        String pathTest = currentPath+"\\Repo";

        Runtime rt = Runtime.getRuntime();

        ArrayList<String> versionList = new ArrayList<>();
        String[] version;

        try {
            //region Create Git Repo
            Process proc1 = rt.exec("git clone " + url + " Repo");
            proc1.waitFor();
            //endregion

            //region Getting version
            String[] cmd1 = {"cmd.exe", "/c", "cd "+pathTest+" && git rev-list --all"};
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
            version = new String[versionList.size()];
            for(int i = 0; i < versionList.size(); i++) {
                version[i] = versionList.get(i);
            }
            //endregion

            /**
            //region Delete Git Repo
            String[] cmd2 = {"cmd.exe", "/c", "rmdir /s /q "+pathTest};
            rt.exec(cmd2);
            //endregion
            **/

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static int nbClass(String path) {



        return 0;

    }

}
