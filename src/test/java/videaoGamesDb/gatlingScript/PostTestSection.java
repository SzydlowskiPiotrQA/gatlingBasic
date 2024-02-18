package videaoGamesDb.gatlingScript;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import videaoGamesDb.feeders.CustomFeeders;
import videaoGamesDb.feeders.RandomPostData;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class PostTestSection extends Simulation {

    private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.csv").circular();

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk:443/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");


    private static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(ElFileBody(
                            "data/authenticate.json"))
                    .check(jmesPath("token").saveAs("jwtToken")));


    private static ChainBuilder createNewGame =
            feed(RandomPostData.createRandomPostData)
                    .exec(http("Create new game")
                            .post("/videogame")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(ElFileBody("bodies/newGameTemplate.json"))
                            .check(bodyString().saveAs("responseBody")))
                    .exec(session -> {
                        System.out.println(session.getString("responseBody"));
                        return session;
                    });

    private final ScenarioBuilder sc = scenario("Video Game Db - POST METHOD")
            .exec(authenticate)
            .repeat(20).on(
                    exec(createNewGame));


    {
        setUp(sc.injectOpen(atOnceUsers(1))
                .protocols(httpProtocol));
    }
}
