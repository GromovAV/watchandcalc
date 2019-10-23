import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalcResult implements ICalcResult {
    private List<String> words;
    private String inputFile = "";
    Map<Character,Integer> charsFrequency;

    public  CalcResult(String inputFile)
    {
       this.inputFile = inputFile;
        charsFrequency = new HashMap<>();
        words= new ArrayList<>();
    }

    public List<String>  ReadText()
    {
        String delims = "[ ,.?)(![0-9]]+";
            try(BufferedReader br = new BufferedReader(new FileReader(WatchAndCalc.INPUT_PATH + "\\" + inputFile)))
            {
                while ((br.read()) != -1) {
                    for (String word : br.readLine().split(delims)) {
                        if (word.trim().length() > 0) {
                            words.add(word);
                        }
                    }
                }
        } catch (IOException e) {
            System.out.println("An I/O Error Occurred");
        }
        return words;
    }

    public void Calculate()
    {
        for(String word : words)
        {
            char[] chars = word.toCharArray();
            for(char ch : chars)
            {
                if(Character.isLetter(ch)) {
                    Integer j = charsFrequency.get(ch);
                    charsFrequency.put(ch, (j == null) ? 1 : j + 1);
                }
            }
        }
    }

    public void WriteText()
    {
        String fineName = WatchAndCalc.OUTPUT_PATH + "\\" + "Result_" + inputFile;
        try(BufferedWriter osw = new BufferedWriter(new FileWriter(fineName))) {
            osw.write("Результат вычислений" + "\n");
            osw.write("Количество слов = " + words.size() + "\n");
            osw.write("Частота появления букв " + "\n");
            charsFrequency.entrySet().stream()
                    .sorted(Map.Entry.<Character, Integer>comparingByKey().reversed()).forEach(entry->{
                try {
                    osw.write(entry.getKey() + " = " + entry.getValue() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
    }
}