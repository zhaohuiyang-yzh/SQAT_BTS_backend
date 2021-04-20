package bts.demo.bts.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class DataInFileUtil {
    private DataInFileUtil() {
        throw new IllegalStateException("Utility class");
    }

    ;
    private static final Logger logger = LoggerFactory.getLogger(DataInFileUtil.class);

    public static JSONArray readFile(String fileName) throws JSONException {
        File file = new File(fileName);
        String encoding = "UTF-8";
        long fileLength = file.length();
        byte[] fileContent = new byte[(int) fileLength];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
            logger.error("An error happened in utils/DataInFileUtil.readFile(): " + e.getMessage());
        }
        try {
            return new JSONArray(new String(fileContent, encoding));
        } catch (UnsupportedEncodingException e) {
            logger.error("An error happened in utils/DataInFileUtil.readFile(): " + e.getMessage());
            logger.error("The OS does not support " + encoding);
            return null;
        }
    }
}
