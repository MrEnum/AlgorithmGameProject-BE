package com.seventeam.algoritmgameproject.web.service.crawling_service;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TestCaseUtil {
    private final Random rnd;
    public TestCaseUtil() {
        this.rnd = new Random();
    }

    public String randomStr(int len) {
        int num = rnd.nextInt(len) + 3;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < num; i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) ((rnd.nextInt(26)) + 97));
            } else {
                buf.append((char) ((rnd.nextInt(26)) + 65));
            }

        }
        return buf.toString();
    }
//사용 안함
//    public String randomNumAndStr(int len) {
//        StringBuilder buf = new StringBuilder();
//        for (int i = 0; i < len; i++) {
//            if (rnd.nextBoolean()) {
//                buf.append((char) ((int) (rnd.nextInt(26)) + 97));
//            } else {
//                buf.append((rnd.nextInt(10)));
//            }
//        }
//        return buf.toString();
//    }

    public String randomPhoneNumber() {

        StringBuilder common;
        int num = rnd.nextInt(10);
        if (num < 5) {
            common = new StringBuilder("010");
        } else {
            common = new StringBuilder("02");
        }
        for (int i = 0; i < 8; i++) {
            common.append(rnd.nextInt(10));
        }
        return common.toString();
    }

    public int[] randomArr() {
        int[] ans = new int[rnd.nextInt(20) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = rnd.nextInt(9) + 1;
        }
        return ans;
    }

    public int[][] randomParamsSol10() {
        int[] arr1 = new int[6];
        int[] arr2 = new int[6];
        List<Integer> arr1List = new ArrayList<>();
        List<Integer> arr2List = new ArrayList<>();
        for (int i = 0; i < arr1.length; i++) {
            int atomicArr1 = rnd.nextInt(46);
            int atomicArr2 = rnd.nextInt(45) + 1;

            if(atomicArr1 != 0) {
                while (arr1List.contains(atomicArr1)) {
                    atomicArr1 = rnd.nextInt(46);
                }
            }
            while (arr2List.contains(atomicArr2)) {
                atomicArr2 = rnd.nextInt(45) + 1;
            }


            arr1List.add(atomicArr1);
            arr2List.add(atomicArr2);
            arr1[i] = atomicArr1;
            arr2[i] = atomicArr2;
        }
        return new int[][]{arr1, arr2};
    }

}


