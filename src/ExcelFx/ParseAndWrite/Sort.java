/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.ParseAndWrite;

import ExcelFx.FXMLDocumentController;
import ExcelFx.row.IndexMap;
import ExcelFx.row.ExcelRow;
import ExcelFx.row.RowList;
import Json.JsonWrite;
import Json.JsonXLSXData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author seryo
 *
 */
public class Sort {

    private final RowList list;
    private final List<IndexMap> indMap;
    private JsonXLSXData jsonXLSXData;
    private JsonWrite universityJson;
    private JsonWrite collegeJson;
//    private JsonCodes codesJson;
    private RowList collegeProf;
    private RowList univerProf;
    private HashMap<String, HashMap<String, String>> bigGroupe;
    private final HashMap<String, String> universityParam = new HashMap<>();
    private final HashMap<String, String> collegeParam = new HashMap<>();
    private Integer YStart;
    private Integer YEnd;

    public JsonXLSXData getJsonXLSXData() {
        return jsonXLSXData;
    }

    public void setJsonXLSXData(JsonXLSXData jsonXLSXData) throws NullPointerException, IOException {
        this.jsonXLSXData = jsonXLSXData;
        parseJson(jsonXLSXData);
    }

    private void parseJson(JsonXLSXData jsonXLSXData) throws NullPointerException, IOException {
        System.out.println("----------------------" + "UniversityParam" + "----------------------");
        System.out.println(jsonXLSXData.getUniversityJson().toString());
        this.universityJson = jsonXLSXData.getUniversityJson();
        this.universityJson.setPage(String.valueOf((Integer.valueOf(this.universityJson.getPage()) - 1)));

        System.out.println("----------------------" + "CollegeParam" + "-------------------------");
        System.out.println(jsonXLSXData.getCollegeJson().toString());
        System.out.println("--------------------------------------------------------------");
        this.collegeJson = jsonXLSXData.getCollegeJson();
        this.collegeJson.setPage(String.valueOf((Integer.valueOf(this.collegeJson.getPage()) - 1)));

        this.collegeProf = new RowList(CollegeProfession(this.collegeJson.getPatch(), Integer.parseInt(this.collegeJson.getPage())));
        this.univerProf = new RowList(UniversityProfession(this.universityJson.getPatch(), Integer.parseInt(this.universityJson.getPage())));

        setParam(universityJson, univerProf, universityParam);

        setParam(collegeJson, collegeProf, collegeParam);

    }



    private void setParam(JsonWrite json, RowList rowList, HashMap<String, String> param) {

        for (int i = 0; i < rowList.get(0).size(); i++) {

            if (json.getProffName().equals(rowList.get(0).get(i))) {
                param.put("proffName", String.valueOf(i));
                break;
            }
        }
        for (int i = 0; i < rowList.get(0).size(); i++) {
            if (json.getEddWay().equals(rowList.get(0).get(i))) {
                param.put("eddWay", String.valueOf(i));
                break;

            }
        }

    }

    /**
     *
     * @param list список сортировку которого нужно произвести
     * @param jsonXLSXData пути и настройки xlsx файлов
     * @param YStart год начала парсинга
     * @param YEnd год конца парсинга
     * @throws java.io.IOException
     */
    public Sort(RowList list, JsonXLSXData jsonXLSXData, String YStart, String YEnd) throws NullPointerException, IOException {

        this.indMap = new ArrayList<>();
        this.list = new RowList(list);
        this.YStart = list.get(0).columnNumberEducationLevel(YStart);
        this.YEnd = list.get(0).columnNumberEducationLevel(YEnd);
        parseJson(jsonXLSXData);

    }

    public Sort(RowList list) {
        this.list = list;
        this.indMap = new ArrayList<>();
    }

    public Sort() {
        this.list = new RowList();
        this.indMap = new ArrayList<>();
    }

    /**
     * <p>
     * сортировка по вэд(Внешнеэкономической деятельности)</p>
     *
     * @deprecated изменить выбор колонки на то как это было сделанно для вузов
     * и сузов
     *
     */
    public HashMap<String, RowList> sortVacancyByEducationLevel(RowList list) {
        HashMap<String, RowList> vac = new HashMap<>();
        RowList university = new RowList();
        RowList college = new RowList();
        RowList other = new RowList();
        int indexcolumn = list.get(0).columnNumberEducationLevel("требования к уровню образования");

        for (int i = 0; i < list.size(); i++) {
            try {
                if (list.get(i).isCollege(indexcolumn)) {
                    college.add(list.get(i));
                } else if (list.get(i).isUniversity(indexcolumn)) {
                    university.add(list.get(i));
                } else {
                    other.add(list.get(i));

                }

            } catch (IndexOutOfBoundsException e) {

            }
        }

        vac.put("высшее", university);
        vac.put("среднее", college);
        vac.put("особое", other);
        return vac;
    }

    public RowList UniversityProfession(String patchNameUniversityProffession, Integer page) throws NullPointerException, IOException {
        Parser parser = new Parser();
        return parser.parseInitalData(patchNameUniversityProffession, page);

    }

    public RowList CollegeProfession(String patchNameCollegeProffession, Integer page) throws NullPointerException, IOException {
        Parser parser = new Parser();
        return parser.parseInitalData(patchNameCollegeProffession, page);

    }

    public HashMap<String, HashMap<String, String>> bigGroupeByIndex(String bigGroupePatch, ArrayList<Integer> pageList) throws IOException {
        Parser parser = new Parser();
        HashMap<String, HashMap<String, String>> bigGroupeList = new HashMap<>();
        for (int i = 0; i < pageList.size(); i++) {
            bigGroupeList.put("*.0" + (i + 1) + ".*", parser.parseBigGroupe(bigGroupePatch, i));
        }
        return bigGroupeList;
    }

    /**
     * <p>
     * назван в честь листа с которго был считан :)
     * <p>
     * <p>
     * Создает два массива:</p>
     * <ul>
     * <li>Массив профессий для которгго были найдены коды напрвлений
     * подготовки</li>
     * <li>Массив профессий для которгго были не были найдены коды напрвлений
     * подготовки</li>
     * </ul>
     *
     * @throws java.io.IOException
     */
    private void getConsolidatedList() throws IOException {

        HashMap<String, RowList> vacSort = sortVacancyByEducationLevel(list);

        List<ExcelRow> consolidatedDatas = new ArrayList<>();
        for (int i = 2; i < list.size(); i++) {
            consolidatedDatas.add(checkGroupe(list.get(i), collegeProf, univerProf, bigGroupe));
            System.out.println(i);
        }

        System.out.println("");

    }

    public ExcelRow checkGroupe(ExcelRow row, RowList collegeProf, RowList univerProf, HashMap<String, HashMap<String, String>> bigGroupe) throws IOException, NullPointerException {

        Parser parser = new Parser();
        int proffRow = list.get(0).columnNumberEducationLevel("Наименование профессии, должности");
        int educRow = list.get(0).columnNumberEducationLevel("Требования к уровню образования");
        String proffName;

        ExcelRow eRow = new ExcelRow();

        if ((educRow != -1) && (row.isCollege(educRow))) {

            proffName = row.get(proffRow); //имя проффессии
            String tmp;
            for (int j = 1; j < collegeProf.size(); j++) {
                String indexBigGr = collegeProf.get(j).get(Integer.parseInt(this.collegeParam.get("eddWay"))).substring(3, 5);
                HashMap<String, String> datas = bigGroupe.get("*." + indexBigGr + ".*");
                if (collegeProf.get(j).get(Integer.parseInt(this.collegeParam.get("proffName"))).equals(proffName)) { //сравниваем имя профессии из списка вакансий и списка учебных заведений
                    if ((datas != null) && (proffName != "null") && (proffName != null)) {
                        tmp = datas.get(collegeProf.get(j).get(Integer.parseInt(this.collegeParam.get("eddWay"))));
                        eRow.add(tmp);
                        eRow.add(proffName);
                        for (int n = this.YStart; n <= this.YEnd; n++) {
                            eRow.add(row.get(n));
                        }
                        eRow.add("вп");
                        System.err.println("");
                    }
                }
            }

            if (eRow.size() < 3) {
                eRow = row;
            }

        } else if ((educRow != -1) && (row.isUniversity(educRow))) {

            proffName = row.get(proffRow); //имя проффессии
            String indexBigGr = univerProf.get(1).get(Integer.parseInt(this.universityParam.get("eddWay"))).substring(3, 5);
            HashMap<String, String> datas = bigGroupe.get("*." + indexBigGr + ".*");
            for (int j = 1; j < univerProf.size(); j++) {

                if (univerProf.get(j).get(Integer.parseInt(this.universityParam.get("proffName"))).equals(proffName)) { //сравниваем имя профессии из списка вакансий и списка учебных заведений
                    if ((datas != null) && (proffName != "null") && (proffName != null)) {
                        String tmp = datas.get(univerProf.get(j).get(Integer.parseInt(this.universityParam.get("eddWay"))));

                        eRow.add(tmp);
                        eRow.add(proffName);
                        for (int n = this.YStart; n <= this.YEnd; n++) {
                            eRow.add(row.get(n));
                        }
                        eRow.add("вп");
                        System.err.println("");
                        break;
                    }
                }

            }
            if (eRow.size() < 3) {
                eRow = row;
            }
        }

        return eRow;
    }

    public HashMap<String, HashMap<String, ArrayList<String>>> professionsGrouping(String YStart, String YEnd) {
        System.out.println("profession group sorting is start");

        int startY = list.get(0).columnNumberEducationLevel(YStart);
        int finishY = list.get(0).columnNumberEducationLevel(YEnd);

        HashMap<String, ArrayList<String>> professionGroupeUniversity = new HashMap<>();
        HashMap<String, ArrayList<String>> professionGroupeCollege = new HashMap<>();
        HashMap<String, HashMap<String, ArrayList<String>>> countedMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            String edLevel = list.get(i).get(2).toLowerCase().trim(); //абслютная адресыция на имя профессии 
            switch (edLevel) {
                case "вп": {
                    String profName = list.get(i).get(1); //абслютная адресыция на тип образования исправить 
                    if (!professionGroupeUniversity.containsKey(profName)) {
                        ArrayList<String> tempYear = new ArrayList<>();
                        for (int j = startY; j <= finishY; j++) {
                            try {
                                tempYear.add(String.valueOf(Double.parseDouble(list.get(i).get(j))));
                            } catch (NumberFormatException e) {
                                tempYear.add(String.valueOf(0));
                            }
                        }
                        professionGroupeUniversity.put(profName, tempYear);
                    } else {
                        int tmpY = 0;
                        //ArrayList<String> tempYear = professionGroupeUniversity.get(profName);

                        for (int j = startY; j <= finishY; j++) {
                            if (tmpY <= professionGroupeUniversity.get(profName).size()) {
                                try {
                                    if (Double.parseDouble(list.get(i).get(j)) > 0) {
                                        professionGroupeUniversity.get(profName).set(tmpY, String.valueOf(Double.parseDouble(professionGroupeUniversity.get(profName).get(tmpY)) + Double.parseDouble(list.get(i).get(j))));
                                    }
                                } catch (NumberFormatException e) {
                                    professionGroupeUniversity.get(profName).set(tmpY, String.valueOf(Double.parseDouble(professionGroupeUniversity.get(profName).get(tmpY)) + 0));
                                }
                                tmpY++;
                            }

                        }

                    }

                    break;
                }
                case "сп": {
                    String profName = list.get(i).get(1); //абслютная адресыция на тип образования исправить 
                    if (!professionGroupeCollege.containsKey(profName)) {
                        ArrayList<String> tempYear = new ArrayList<>();
                        for (int j = startY; j <= finishY; j++) {
                            try {

                                tempYear.add(String.valueOf(Double.parseDouble(list.get(i).get(j))));
                            } catch (NumberFormatException e) {
                                tempYear.add(String.valueOf(0));
                            }
                        }
                        professionGroupeCollege.put(profName, tempYear);
                    } else {
                        int tmpY = 0;
                        //ArrayList<String> tempYear = professionGroupeUniversity.get(profName);

                        for (int j = startY; j <= finishY; j++) {
                            if (tmpY <= professionGroupeCollege.get(profName).size()) {
                                try {
                                    if (Double.parseDouble(list.get(i).get(j)) > 0) {
                                        professionGroupeCollege.get(profName).set(tmpY, String.valueOf(Double.parseDouble(professionGroupeCollege.get(profName).get(tmpY)) + Double.parseDouble(list.get(i).get(j))));
                                    }
                                } catch (NumberFormatException e) {
                                    professionGroupeCollege.get(profName).set(tmpY, String.valueOf(Double.parseDouble(professionGroupeCollege.get(profName).get(tmpY)) + 0));
                                }
                                tmpY++;
                            }

                        }

                    }

                    break;
                }
                default: {

                }
            }

        }

        countedMap.put("высшее", professionGroupeUniversity);
        countedMap.put("среднее", professionGroupeCollege);

        System.out.println("coutedMap size is " + countedMap.size() + ":");
        System.out.println("universitySize: " + countedMap.get("высшее").size());
        System.out.println("collegeSize: " + countedMap.get("среднее").size());
        System.out.println("profession group sorting is end");
        System.out.println("-----------------------------------------------------------");

        return countedMap;

    }

    public HashMap<String, HashSet<CountedMap>> addWay(HashMap<String, HashMap<String, ArrayList<String>>> countedMap) throws NullPointerException, IOException {
        System.out.println("eddWay was started");
        System.out.println("coutedMap size is " + countedMap.size() + ":");
        System.out.println("universitySize: " + countedMap.get("высшее").size());
        System.out.println("collegeSize: " + countedMap.get("среднее").size());

        HashMap<String, HashSet<CountedMap>> result = new HashMap<>();

        for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : countedMap.entrySet()) {
            switch (entry.getKey().trim().toLowerCase()) {
                case "высшее": {
                    HashSet<CountedMap> hs = new HashSet<>();
                    HashSet<CountedMap> hsErr = new HashSet<>();
                    HashMap<String, ArrayList<String>> tempHm = entry.getValue();
                    for (Map.Entry<String, ArrayList<String>> newEntry : tempHm.entrySet()) {
                        HashSet<String> edWay = new HashSet();
                        for (int i = 0; i < this.univerProf.size(); i++) {
                            try {

                                if (univerProf.get(i).get(Integer.parseInt(universityParam.get("proffName"))).equals(newEntry.getKey())) {
                                    edWay.add(univerProf.get(i).get(Integer.parseInt(universityParam.get("eddWay"))));

                                }
                            } catch (IndexOutOfBoundsException e) {
                                addToTableList(e.toString());
                                System.err.println(e);
                                this.collegeProf.getRowList().remove(i);
                                i--;

                            }
                        }

                        if (edWay.size() > 0) {

                            hs.add(new CountedMap(newEntry.getKey(), newEntry.getValue(), edWay));
                            //System.out.println(edWay);
                            addToTableList(edWay.toString());
                        } else {
                            hsErr.add(new CountedMap(newEntry.getKey(), newEntry.getValue(), edWay));
                            addToTableList("Совпадение с профессией" + newEntry.getKey().toLowerCase().trim() + "не найденно");
                        }
                    }
                    result.put("universityTrue", hs);
                    result.put("universityFalse", hsErr);
                    break;
                }
                case "среднее": {
                    HashSet<CountedMap> hs = new HashSet<>(); // Hashset профессий для которых найдены совпадения
                    HashSet<CountedMap> hsErr = new HashSet<>(); // Hashset профессий для которых совпадения не найдены
                    HashMap<String, ArrayList<String>> tempHm = entry.getValue();

                    for (Map.Entry<String, ArrayList<String>> newEntry : tempHm.entrySet()) {
                        HashSet<String> edWay = new HashSet();
                        //System.out.println(newEntry.getKey());

                        for (int i = 0; i < this.collegeProf.size(); i++) {
                            //System.out.println(i);

                            try {

                                if (collegeProf.get(i).get(Integer.parseInt(collegeParam.get("proffName"))).equals(newEntry.getKey())) {
                                    edWay.add(collegeProf.get(i).get(Integer.parseInt(collegeParam.get("eddWay"))));
                                    //System.out.println(collegeProf.get(i).get(Integer.parseInt(collegeParam.get("proffName"))));

                                }
                            } catch (IndexOutOfBoundsException e) {
                                System.err.println(e);
                                addToTableList(e.toString());
                                this.collegeProf.getRowList().remove(i);
                                i--;

                            }

                        }
                        if (edWay.size() > 0) {

                            hs.add(new CountedMap(newEntry.getKey(), newEntry.getValue(), edWay));
                            //System.out.println(edWay);
                            addToTableList(edWay.toString());
                        } else {
                            hsErr.add(new CountedMap(newEntry.getKey(), newEntry.getValue(), edWay));
                            addToTableList("Совпадение с профессией" + newEntry.getKey().toLowerCase().trim() + "не найденно");
                        }
                    }

                    result.put("collegeTrue", hs);
                    result.put("collegeFalse", hsErr);
                    break;
                }
            }
        }

        System.out.println("result size: " + result.size());
        System.out.println("UniversityTrue size: " + result.get("universityTrue").size());
        System.out.println("UniversityFalse size: " + result.get("universityFalse").size());
        System.out.println("collegeTrue size: " + result.get("universityTrue").size());
        System.out.println("collegeFalse size: " + result.get("universityFalse").size());
        System.out.println("eddWay finished");
        System.out.println("-----------------------------------------------------------");

        return result;

    }

    public int editdist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            D2[i] = i;
        }

        for (int i = 1; i <= m; i++) {
            D1 = D2;
            D2 = new int[n + 1];
            for (int j = 0; j <= n; j++) {
                if (j == 0) {
                    D2[j] = i;
                } else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if (D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost) {
                        D2[j] = D2[j - 1] + 1;
                    } else if (D1[j] < D1[j - 1] + cost) {
                        D2[j] = D1[j] + 1;
                    } else {
                        D2[j] = D1[j - 1] + cost;
                    }
                }
            }
        }
        return D2[n];
    }

    private boolean checkWord(String s1, String s2) {
        ArrayList<String> splitString1 = new ArrayList<>(Arrays.asList(Arrays.toString(s1.toCharArray()).substring(1, s1.length() - 1).split(",")));
        ArrayList<String> splitString2 = new ArrayList<>(Arrays.asList(s2.split(" ")));

        Integer index = 0;
        for (int i = 0; i < splitString1.size(); i++) {
            for (int j = 0; j < splitString2.size(); j++) {
                if (splitString1.get(i).equals(splitString2.get(j))) {
                    index++;
                }
            }
        }
        return index >= (splitString1.size() + splitString2.size()) * 0.3;

    }

    public void addToTableList(String retString) {
        FXMLDocumentController fXMLDocumentController = new FXMLDocumentController();
        fXMLDocumentController.eddArr(retString);

    }

}
//
