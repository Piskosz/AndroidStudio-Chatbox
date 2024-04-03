package com.example.shoot;

public class Model {

 private String mText1;
 private String mText2;
 private String mText3;
 private String id;

    public Model(){}

    public Model(String mtex1,String mtex2,String mtex3,String id){


       this.mText1=mtex1;
       this.mText2=mtex2;
       this.mText3=mtex3;
       this.id = id;


   }


    public String getTex1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }


    public String getTex2() {
        return mText2;
    }
    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public String getTex3() {
        return mText3;
    }

    public void setmText3(String mText3) {
        this.mText3 = mText3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
