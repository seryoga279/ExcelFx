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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author seryo
 */
public class JsonWrite {

    private String patch;
    private String page;
    private String eddWay;
    private String proffName;
    private String YStart;
    private String YEnd;
    private String EddName;

    public JsonWrite(String patch, String page, String eddWay, String proffName, String YStart, String YEnd) {
        this.patch = patch;
        this.eddWay = eddWay;
        this.proffName = proffName;
        this.YStart = YStart;
        this.YEnd = YEnd;
        
    }

    public JsonWrite() {
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getEddWay() {
        return eddWay;
    }

    public void setEddWay(String eddWay) {
        this.eddWay = eddWay;
    }

    public String getProffName() {
        return proffName;
    }

    public void setProffName(String proffName) {
        this.proffName = proffName;
    }

    public String getYStart() {
        return YStart;
    }

    public void setYStart(String YStart) {
        this.YStart = YStart;
    }

    public String getYEnd() {
        return YEnd;
    }

    public void setYEnd(String YEnd) {
        this.YEnd = YEnd;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void jsonCreate(String patchName) {
        JSONObject obj = new JSONObject();
        obj.put("patch", this.patch);
        obj.put("page", this.page);
        obj.put("eddWay", this.eddWay);
        obj.put("proffName", this.proffName);
        obj.put("yearStart", this.YStart);
        obj.put("yearEnd", this.YEnd);
        obj.put("eddName", this.EddName);

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
        this.patch = (String) object.get("patch");
        this.page = (String) object.get("page");
        this.eddWay = (String) object.get("eddWay");
        this.proffName = (String) object.get("proffName");
        this.YStart = (String) object.get("yearStart");
        this.YEnd = (String) object.get("yearEnd");
        this.EddName = (String) object.get("eddName");

        //System.out.println("Json.JsonWrite.jsonRead()");

    }

    public String getEddName() {
        return EddName;
    }

    public void setEddName(String EddName) {
        this.EddName = EddName;
    }
     public boolean isEmpty(){
         return this.patch.isEmpty();
     }

    @Override
    public String toString() {
        return ("Profession way: "+this.proffName +"\nEducation name: "+ this.EddName +"\nEducation Way: "+ this.eddWay +"\nFile patch: "+ this.patch +"\nFile Page: "+ this.page +"\nYear start: "+ this.YStart +"\nYear end: "+ this.YEnd);
    }
     

}
