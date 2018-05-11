package com.example.thiranja.smartnotes;

import java.util.ArrayList;
import java.util.Collections;

public class NoteSorter {

    private int i,j;


    public void sortByModifiedDate(ArrayList<Note> notes){
        // here needs to sort according to the data modified order
        int size = notes.size();
        for (i = 0; i < size-1; i++){
            for (j = 0; j < (size-1)-i;j++){
                String firstDate = notes.get(j).getDate();
                String secondDate = notes.get(j+1).getDate();

                if(dateCompare(firstDate, secondDate) < 0){
                    Collections.swap(notes,j,j+1);
                }
            }
        }

    }

    public void sortByAlphabatically(ArrayList<Note> notes){
        // Here comes the code for sorting in the alphabet order
        int size = notes.size();
        for (i = 0; i < size-1; i++){
            for (j = 0; j < (size-1)-i;j++){
                String firstName = notes.get(j).getName();
                String secondName = notes.get(j+1).getName();
                if(firstName.compareTo(secondName) > 0){
                    Collections.swap(notes,j,j+1);
                }
            }
        }
    }

    private int dateCompare(String first, String second){
        String[] firstArray = first.split(" ");
        String[] secondArray = second.split(" ");
        int year = firstArray[2].compareTo(secondArray[2]);
        if(year > 0){
            return 1;
        }else if(year < 0){
            return -1;
        }else{
            int month = (getMonthNumber(firstArray[1]) - getMonthNumber(secondArray[1]));
            if(month > 0){
                return 1;
            }else if(month < 0){
                return -1;
            }else{
                int date = firstArray[0].compareTo(secondArray[0]);
                if(date > 0){
                    return 1;
                }else if(date < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        }

    }

    private int getMonthNumber(String month){
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            default:
                return 12;
        }
    }


}
