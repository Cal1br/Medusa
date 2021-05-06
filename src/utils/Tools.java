package utils;

public class Tools {
    private Tools() {

    }

    public static int HammingDistance(String str1, String str2) {
        int i = 0, count = 0;
        int shortLength = 0;
        if(str1.length()>str2.length()){
            shortLength = str2.length();
        }
        else {
            shortLength = str1.length();
        }
        while (i < shortLength) {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        return count;
    }

}
