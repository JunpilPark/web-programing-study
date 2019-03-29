import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalcurator {

    public int calculate(String str) throws RuntimeException {
        if(isNullOrEmpty(str)) return 0;
        String[] splitedNumbers = spritBySeperator(str);
        int[] numbers = changeToIntArray(splitedNumbers);
        checkMinusNumber(numbers);
        return  sum(numbers);
    }

    private String[] spritByBasicSeperator(String target)  {
        String[] spritedStrings = target.split(",|:");
        return spritedStrings;
    }

    private String[] spritByCutomSeperator(String target, String customSeperator)  {
        if(".$|()[{^?*+".indexOf(customSeperator) >= 0) {
            customSeperator = String.format("\\%s", customSeperator);
        }
        return target.split(customSeperator);
    }

    private String[] spritString(String target, String seperator) {
        if(seperator == null) {
            return spritByBasicSeperator(target);
        }
        return spritByCutomSeperator(target, seperator);
    }

    private boolean isNullOrEmpty(String str) {
        if(str == null || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    private String convertToZeroFromNullOrEmpty(String str) {
        if(str == null || str.trim().isEmpty()) {
            return "0";
        }
        return str;
    }

    private int[] changeToIntArray(String[] stringArr) {
        int[] covertedNumber = new int[stringArr.length];
        for(int i = 0 ; i < stringArr.length ; i++) {
            covertedNumber[i] = Integer.parseInt(convertToZeroFromNullOrEmpty(stringArr[i]));
        }
        return covertedNumber;
    }

    private void checkMinusNumber(int[] target) throws RuntimeException {
        for(int i = 0 ; i<target.length ; i++) {
            if(target[i] < 0) throw new RuntimeException();
        }
    }

    private int sum(int[] targets) {
        int sum = 0;
        for(int i : targets) {
            sum += i;
        }
        return sum;
    }

    private String[] spritBySeperator(String str) {
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(str);
        if(m.find()) {
            String customSeperator = m.group(1);
            return spritByCutomSeperator(m.group(2), customSeperator);
        }
        return spritByBasicSeperator(str);
    }
}
