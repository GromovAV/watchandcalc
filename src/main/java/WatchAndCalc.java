import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatchAndCalc {
    public static String INPUT_PATH = ".\\IN";
    public static String OUTPUT_PATH = ".\\OUT";
    public static String DEFAULT_FILE = "input.txt";

    public static void main(String[] args) {
        try {
            if(!DEFAULT_FILE.isEmpty()){
                CalcResult calcResult = new CalcResult(DEFAULT_FILE);
                calcResult.ReadText();
                calcResult.Calculate();
                calcResult.WriteText();
            }

            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path logDir = Paths.get(INPUT_PATH);
            logDir.register(watcher, ENTRY_CREATE);

            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (ENTRY_CREATE.equals(kind)) {
                        CalcResult calcResult = new CalcResult(event.context().toString());
                        calcResult.ReadText();
                        calcResult.Calculate();
                        calcResult.WriteText();
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
