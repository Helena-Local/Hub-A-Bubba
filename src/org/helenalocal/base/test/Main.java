package org.helenalocal.base.test;

import org.helenalocal.base.Item;

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
    }
}
