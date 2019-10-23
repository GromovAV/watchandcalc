import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatchAndCalc {
    public static String INPUT_PATH = ".\\IN";
    public static String OUTPUT_PATH = ".\\OUT";
    public static String DEFAULT_FILE = "input.txt";

    public static void main(String[] args) {
        try {
            String fullNameDefaultFile = INPUT_PATH + "\\" + DEFAULT_FILE;
            if(Files.exists(Paths.get(fullNameDefaultFile)))
            {
                System.out.println("Запускаем вычисления на файле -Input.txt");
                CalcResult calcResult = new CalcResult(DEFAULT_FILE);
                calcResult.ReadText();
                calcResult.Calculate();
                calcResult.WriteText();
                System.out.println("Ok!");
            }

            System.out.println("Отслеживаем появление новых файлов и вычисляем их...");

            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path logDir = Paths.get(INPUT_PATH);
            logDir.register(watcher, ENTRY_CREATE);

            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (ENTRY_CREATE.equals(kind)) {
                        String nameFile=event.context().toString();
                        CalcResult calcResult = new CalcResult(nameFile);
                        System.out.println("Вычисляем файл " + nameFile);
                        calcResult.ReadText();
                        calcResult.Calculate();
                        calcResult.WriteText();
                        System.out.println("Ok!");
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
