package cursorMod;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

public class CursorMod {
    private boolean backupSuccessful = false;
    private ArrayList<String[]> backup;
    private String regSource = "HKEY_CURRENT_USER\\Control Panel\\Cursors";
    private String dummy = "I:\\Computer Stuff\\cursor\\Material Design\\Main Cursor.cur";

    public CursorMod() {
        backup = new ArrayList<String[]>();
        backupSuccessful = backupValues();

    }
    //reg add "HKEY_CURRENT_USER\Control Panel\Cursors" /v "Test" /t REG_EXPAND_SZ /d test
    //reg delete "HKEY_CURRENT_USER\Control Panel\Cursors" /v "Test" /f
    public boolean hideCursor() {
        if (!backupSuccessful) return false;
        try {
            for (String[] strings : backup) {
               String regMod = "reg add \"" + regSource + "\" /v \"" + strings[0] + "\"  /t REG_EXPAND_SZ /d \"" + dummy + "\" /f";
               Process process = Runtime.getRuntime().exec(regMod);
               process.destroy();
               System.out.println(regMod);
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        
        System.out.println("Modified");
        return true;
    }

    public boolean unHideCursor() {
        if (!backupSuccessful) return false;
        try {
            for (String[] strings : backup) {
                String regMod = "reg add \"" + regSource + "\" /v \"" + strings[0] + "\"  /t REG_EXPAND_SZ /d \"" + strings[1] + "\" /f";
                Process process = Runtime.getRuntime().exec(regMod);
                process.destroy();
            
                System.out.println(regMod);
            }
            
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        System.out.println("Restored");
        return true;
    }


    private boolean backupValues() {
        String[] cursorNames = {
            //"Arrow",
            "AppStarting",
            "Crosshair",
            "Hand",
            "Help",
            "IBeam",
            "No",
            "NWPen",
            "SizeAll",
            "SizeNESW",
            "SizeNS",
            "SizeNWSE",
            "SizeWE",
            "UpArrow",
            "Wait",
        };

        try {
            for(String name : cursorNames) {
                String value = regRead(regSource, name);
                if (value.equals("")) {
                    throw new Exception("Unable to backup value " + regSource + "\\" + name + " Cannot be read");
                } else {
                    String[] backupReg = { name, value };
                    backup.add(backupReg);
                }
            }
            return true;
        } catch (Exception e) {
            backup.clear();
            System.out.println(e);
        }
        return false;
    }

    private String regRead(String location, String key) {
        try {
            Process process = Runtime.getRuntime().exec("reg query " +  '"'+ location + "\" /v " + key);
    
            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();
            String[] split = output.split("    ");

            return split[split.length - 1].replaceAll("\n", "").trim();
            
        } catch (Exception e) {
            return null;
        }
    }
  
    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw= new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) { 
        }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}
