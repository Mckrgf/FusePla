package com.supcon.testlibrary;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author : yaobing
 * @date : 2020/9/22 14:04
 * @desc :
 */
public class Aa {
    public static void main(String[] args) {

//        //功率1
//        String a = "aaaa//aaaa//111.com";
//        int index = a.lastIndexOf("//");
//        String name = "aaaa//aaaa//111.com".substring(index+2,a.length());
//        System.out.println(name);
        ArrayList<String> aa = new ArrayList<>();
        aa.add("1");
        aa.add(null);
        aa.add(2,"3");

        String[] aaa = new String[3];
        aaa[0] = "1";
        aaa[2] = "3";

//        String[] aaaa = (String[]) aa.toArray();
        System.out.println(aa.toString());
        System.out.println(aaa.toString());
//        System.out.println(Arrays.toString(aaaa));
    }
}
