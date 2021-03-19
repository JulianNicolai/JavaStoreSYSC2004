package com.company;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting store...");
	    StoreManager storeManager = new StoreManager();
        System.out.println("Created StoreManager!");

        StoreView user1 = new StoreView(storeManager);

        user1.displayGUI();
    }
}
