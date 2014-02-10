/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base.test;

import org.helenalocal.base.Certification;
import org.helenalocal.base.Item;
import org.helenalocal.base.get.CertificationHub;

import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("**** Starting fetch test for MOCK....");
        try {
            List<Item> myItemArr = new MockHub().getItems(null);
            for (int i = 0; i < myItemArr.size(); i++) {
                Item item = myItemArr.get(i);
                System.out.println(item.toString());
            }
        } catch (Exception e) {
            System.out.println("No MOCK products found....");
        }
        System.out.println("**** Ending fetch test for MOCK....");

        try {
            List<Certification> myCertArr = CertificationHub.buildCertificationList("C-1~99;C-0~55;C-2");
            for (int i = 0; i < myCertArr.size(); i++) {
                Certification certification = myCertArr.get(i);
                System.out.println(certification.toString());
            }
        } catch (Exception e) {
            System.out.println("No MOCK products found....");
        }

    }
}
