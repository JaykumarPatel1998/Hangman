import java.util.Arrays;
import java.util.Objects;

/*
All the utility methods can be written inside this class and can me made accessible to the entire project to use
when needed
 */
public class Utils {

    //utility method to add string value in to an array
    public static void addElementToAnArray(String [] strArray, String str) {
        for (int i = 0; i < strArray.length; i++) {
            if (Objects.equals(strArray[i], null)) {
                strArray[i] = str;
                break;
            }
        }
    }

    //utility method to get number of elements in an array
    public static int getNumberOfElements(String [] strArray) {
        for (int i = 0; i < strArray.length; i++) {
            if (Objects.equals(strArray[i], null)) {
                return i;
            }
        }
        return strArray.length;
    }

    //utility to check if passed string is actually an alphabet
    public static boolean isAnAlphabet(String str) {
        // Regular expression pattern to match alphabetic characters
        String pattern = "^[a-zA-Z]+$";

        return str.matches(pattern);
    }

}
