package com.example.summer.foodpedia4;

/**
 * Created by summer on 16/11/2017.
 */

public class Restaurant {

    private String mobjectID;
    private String mname;
    private String mimage_path;
    private String mprice;
    private String mscore;
    private Integer mdate;
    private String mlocation;
    private String mtype;

    public Restaurant (String objectID, String name, String type, String image_path, String price, String score, Integer date, String location){
        mobjectID = objectID;
        mname = name;
        mtype = type;
        mimage_path = image_path;
        mprice = price;
        mscore = score;
        mdate =date;
        mlocation = location;

    }


    public String getobjectID(){
        return mobjectID;
    }

    public String getname(){
        return mname;
    }

    public String gettype(){return mtype;}

    public String getimage_path(){
        return mimage_path;
    }

    public String getprice() {return mprice;}

    public String getscore() { return mscore;}

    public Integer getdate() { return mdate;}

    public String getlocation() { return  mlocation;};

}
