import java.util.LinkedList;

public class MergeSort {


    public LinkedList<String> sort(LinkedList<String> queue) {
        return compare(queue);
    }

    protected LinkedList<String> compare(LinkedList<String> list) {
        if (list.size() == 1) {
            return list;
        }
        int listSize = list.size();
        int mid = listSize / 2;
        LinkedList<String> left = new LinkedList<>();
        LinkedList<String> right = new LinkedList<>();

        left.addAll(list.subList(0, mid));
        right.addAll(list.subList(mid, listSize));

        left = compare(left);
        right = compare(right);
        return mergeArray(left, right, list);
    }

    public boolean compareSetLeft(String left, String right) {
        int leftIndex = 0;
        int rightIndex = 0;

        boolean isLeftPrecedent = true;
        while (leftIndex < left.length() && rightIndex < right.length()) {
            char leftChar = Character.toLowerCase(left.charAt(leftIndex));
            char rightChar = Character.toLowerCase(right.charAt(rightIndex));
            if (leftChar < rightChar) {
                break;
            } else if (leftChar > rightChar) {
                isLeftPrecedent = false;
                break;
            }
            leftIndex++;
            rightIndex++;
        }
        return isLeftPrecedent;
    }

    protected LinkedList<String> mergeArray(LinkedList<String> left, LinkedList<String> right, LinkedList<String> res) {

        res.clear();
        int sizeTotal = left.size() + right.size();

        for (int i = 0; i < sizeTotal; i++) {
            if ( right.isEmpty() || (!left.isEmpty()
                    && compareSetLeft(left.peekFirst(), right.peekFirst()))) {
                res.offerLast(left.pollFirst());
            } else {
                res.offerLast(right.pollFirst());
            }
        }
        return res;
    }

}
