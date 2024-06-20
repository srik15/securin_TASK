package com.securin.converters;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class ConversionJSONToXML {
    public static void main(String[] args) throws JSONException {
        // create variable loc that store location of the Sample.json file
        String loc = "D:\\securin1\\movieproject\\converters\\src\\main\\java\\com\\securin\\converters\\jsondata.json";

        String result;
        try {
            // read byte data from the Sample.json file and convert it into String
            result = new String(Files.readAllBytes(Paths.get(loc)));
            //Convert JSON to XML
            String xml = convertToXML(result, "object"); // This method converts json object to xml string
            FileWriter file = new FileWriter("D:\\securin1\\movieproject\\converters\\src\\main\\java\\com\\securin\\converters\\Xmldata");
            file.write(xml);
            file.flush();
            System.out.println("Your XML data is successfully written into XMLData.txt");
            file.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    //method for converting JSOn data into XML

    public static String jsArray(JSONArray value,String keyy){
        String jsxml=" ";
        jsxml += "\t<array name=\"" + keyy + "\">\n";
        for (int l = 0; l < ((JSONArray) value).length(); l++) {
            if (((JSONArray) value).get(l) instanceof Integer) {
                jsxml += "\t\t<number>\n\t\t\t" + ((JSONArray) value).get(l) + "\n\t\t</number>\n";
            }
            if (((JSONArray) value).get(l) instanceof String) {
                jsxml += "\t\t<string>\n\t\t\t" + ((JSONArray) value).get(l) + "\n\t\t</string>\n";
            }
            if (((JSONArray) value).get(l) instanceof Boolean) {
                jsxml += "\t\t<boolean>\n\t\t\t" + ((JSONArray) value).get(l) + "\n\t\t</boolean>\n";
            }
            if (((JSONArray) value).get(l) instanceof JSONObject) {
                System.out.println(((JSONArray) value).get(l));
                jsxml += convertToXML(value.toString(), "object name=\"" + ((JSONArray) value).get(l) + "\"");
                jsxml += "\n";
            }
            if (((JSONArray) value).get(l) instanceof JSONArray) {
                System.out.println(((JSONArray) value).get(l));
//                for(int k=0;k<((JSONArray) ((JSONArray) value).get(l)).length();k++) {
//                    System.out.println(((JSONArray) ((JSONArray) value).get(l)).get(k));
                    jsxml += jsArray((JSONArray) ((JSONArray) ((JSONArray) value).get(l)), "array");
//                }
            }
            if(((JSONArray) value).get(l) instanceof JSONObject){
                System.out.println(((JSONArray) value).get(l));
                jsxml+=convertToXML(jsxml, (String) ((JSONArray) value).get(l));
                jsxml+="\n";
            }
        }

        jsxml += "\t</array>\n";
        return jsxml;
    }

    public static String convertToXML(String jsonString, String root) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray key_names = jsonObject.names();
        String xml = "<"+root+">\n";
        String tag="";
        System.out.println(jsonObject+"-- ");
        for(int i=0;i<jsonObject.length();i++){
            String keyy = (String) key_names.get(i);
            System.out.println();
            Object value = jsonObject.get((String) key_names.get(i));
            System.out.println(keyy+" "+value);
            if (value instanceof Integer) {
                System.out.println("integer");
                tag="number";
            }
            if(value.toString() == "null"){
                System.out.println("yes");
                tag = "null";
            }
            else if(value instanceof String){
                System.out.println("string");
                tag = "string";
            }
            if(value instanceof Boolean){
                tag="boolean";
            }
            if(value instanceof JSONObject){
                xml+=convertToXML(value.toString(),"object name=\""+keyy+"\"");
                xml+="\n";
            }
            else if(value instanceof JSONArray){
                xml+= jsArray((JSONArray) value,"array");
            }
            else if (tag.equals("null")){
                xml+="\t<"+tag+" name=\""+ keyy+"\"/>\n";
            }
            else {
                xml += "\t<" + tag + " name=\"" + keyy + "\">\n\t\t" + value + "\n\t</" + tag + ">\n";
            }
        }
        xml+="</"+root+">";
        System.out.println(xml);
        return xml;
    }
}

