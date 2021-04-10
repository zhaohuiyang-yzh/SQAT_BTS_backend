package bts.demo.bts.utils;

import org.json.JSONArray;

import java.io.File;
import java.util.Scanner;


public class DataInFileUtil {
    public static JSONArray readFile(String fileName) throws Exception {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext())
            sb.append(scanner.next());
        return new JSONArray(sb.toString());
    }
}
