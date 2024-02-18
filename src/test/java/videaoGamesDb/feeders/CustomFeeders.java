package videaoGamesDb.feeders;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CustomFeeders {

        public static Iterator<Map<String, Object>> iterateTo10 =
                Stream.generate((Supplier<Map<String, Object>>) () -> {
                    Random rand = new Random();
                    int gameId = rand.nextInt(10 - 1 + 1) + 1;
                    return Collections.singletonMap("gameId", gameId);
                }).iterator();
}
