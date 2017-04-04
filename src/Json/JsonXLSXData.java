/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Json;

import java.util.ArrayList;

/**
 *
 * @author seryo
 */
public class JsonXLSXData {

    private JsonWrite universityJson;
    private JsonWrite collegeJson;
    //private JsonCodes codes;

    public JsonXLSXData() {
    }

    public JsonXLSXData(JsonWrite universityJson, JsonWrite collegeJsonWrite, JsonCodes codes) {
        this.universityJson = universityJson;
        this.collegeJson = collegeJsonWrite;
       // this.codes = codes;
    }

    public JsonWrite getUniversityJson() {
        return universityJson;
    }

    public void setUniversityJson(JsonWrite universityJson) {
        this.universityJson = universityJson;
    }

    public JsonWrite getCollegeJson() {
        return collegeJson;
    }

    public void setCollegeJsonWrite(JsonWrite collegeJsonWrite) {
        this.collegeJson = collegeJsonWrite;
    }

//    public JsonCodes getCodes() {
//        return codes;
//    }

//    public void setCodes(JsonCodes codes) {
//        this.codes = codes;
//    }

    public void setAll(JsonWrite universityJson, JsonWrite collegeJsonWrite /*JsonCodes codes*/) {
        this.universityJson = universityJson;
        this.collegeJson = collegeJsonWrite;
        //this.codes = codes;
    }

}
