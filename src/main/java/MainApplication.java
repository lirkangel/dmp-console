import javax.swing.*;
import java.io.File;
import java.util.*;

public class MainApplication {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("right");
        list.add("left");
        list.add("Abdc");
        list.add("zzzzz");
        list.add("abcd");
        MergeSort sorter = new MergeSort();
        LinkedList<String> result = sorter.sort(list);
        for (String item: result) {
            System.out.println(item);
        }
    }
}


