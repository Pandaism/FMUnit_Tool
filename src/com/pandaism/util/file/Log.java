package com.pandaism.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Log {
    private File folder = new File("./logs/");
    private File logFile;

    public Log() {
        if(!this.folder.exists()) {
            this.folder.mkdirs();
        }

        createLog();
    }

    private void createLog() {
        this.logFile = new File(this.folder.getPath() + "/" + getDateFormat("YYYYMMdd") + ".log");
        try {
            if(!this.logFile.exists()) {
                if(this.logFile.createNewFile()) {
                    writeToFile("[" + getDateFormat("HH:mm:ss") + "] Created log file", false);
                }
            } else {
                writeToFile("[" + getDateFormat("HH:mm:ss") + "] Application Restarted", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String string, boolean append) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFile, append));
        if(append) {
            writer.write("\n" + string);
        } else {
            writer.write(string);
        }
        writer.close();
    }

    private String getDateFormat(String format) {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat sf = new SimpleDateFormat(format);
        return sf.format(calendar.getTime());
    }

    public void log(String log) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFile, true));
            writer.write("\n[" + getDateFormat("HH:mm:ss") + "]" + log);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
