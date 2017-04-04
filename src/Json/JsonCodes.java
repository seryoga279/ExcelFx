/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author seryo
 */
public class JsonCodes {

    private String patch;
    private ArrayList<String> pageList = new ArrayList<>();

    public JsonCodes() {
    }

    public JsonCodes(String patch, ArrayList<String> pegeList) {
        this.patch = patch;
        this.pageList = pegeList;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public ArrayList<String> getPageList() {
        return pageList;
    }

    public void setPageList(ArrayList<String> pageList) {
        this.pageList = pageList;
    }

    public void jsonCreate(String patchName) {
        JSONObject obj = new JSONObject();
        JSONArray ar = new JSONArray();

        for (int i = 0; i < this.pageList.size(); i++) {

            ar.add(this.pageList.get(i));
        }
        obj.put("patch", this.patch);
        obj.put("page", ar);

        File file = new File(patchName);

        try {
            //проверяем, что если файл не существует то создаем его
            if (!file.exists()) {
                file.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст у файл
                out.print(obj);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void jsonRead(String patchName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject object = (JSONObject) parser.parse(new FileReader(patchName));
        JSONArray arr = new JSONArray();
        this.patch = (String) object.get("patch");
        arr = (JSONArray) object.get("page");
        for(int i=0; i<arr.size();i++){
            this.pageList.add((String) arr.get(i));
        }

        System.out.println("Json.JsonWrite.jsonRead()");

    }

}
