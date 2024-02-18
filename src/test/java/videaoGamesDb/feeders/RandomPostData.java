package videaoGamesDb.feeders;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RandomPostData {

    private static LocalDate randomDate() {
        int hundredYears = 100 * 365;
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));
    }

    public static Iterator<Map<String, Object>> createRandomPostData =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rand = new Random();
                int gameId = rand.nextInt(10 - 1 + 1) + 1;
                int reviewScore = rand.nextInt(100);
                String releaseDate = randomDate().toString();
                String gameName = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
                String category = RandomStringUtils.randomAlphanumeric(5) + "-category";
                String rating = RandomStringUtils.randomAlphanumeric(5) + "-rating";

                HashMap<String, Object> hmap = new HashMap<String, Object>();
                hmap.put("gameId", gameId);
                hmap.put("gameName", gameName);
                hmap.put("releaseDate", releaseDate);
                hmap.put("reviewScore", reviewScore);
                hmap.put("category", category);
                hmap.put("rating", rating);
                return hmap;
            }).iterator();
}
