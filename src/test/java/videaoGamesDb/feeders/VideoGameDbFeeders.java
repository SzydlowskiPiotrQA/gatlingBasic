package videaoGamesDb.feeders;

import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.jsonFile;

public class VideoGameDbFeeders {

    public static FeederBuilder.FileBased<String> getCSVFeeder() {
        return csv("data/gameCSVFile.csv").circular();
    }

    public static FeederBuilder.FileBased<Object> getJsonFeeder() {
        return jsonFile("data/gameJsonFile.csv").circular();
    }
}
