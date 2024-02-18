package videaoGamesDb.gatlingScript;

import videaoGamesDb.feeders.CustomFeeders;
import videaoGamesDb.feeders.VideoGameDbFeeders;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetTestSection extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk:443/api")
            .acceptHeader("application/json");


    private static ChainBuilder getAllViedoGames =
            exec(http("get all video games")
                    .get("/videogame")
                    .check(status().not(400), status().not(500))
                    .check(status().is(200))
            );

    private static ChainBuilder getSingleSpecificViedoGameByCsvFeeder =
            repeat(5).on(
                    feed(VideoGameDbFeeders.getCSVFeeder())
                            .exec(http("Game id: #{gameId},  name: #{gameName} - get specific video game CSV")
                                    .get("/videogame/#{gameId}")
                                    .check(status().is(200))
                                    .check(jmesPath("name").isEL("#{gameName}"))));

    private static ChainBuilder getSingleSpecificViedoGameByJsonFeeder =
            repeat(10).on(
                    feed(VideoGameDbFeeders.getJsonFeeder())
                            .exec(http("Game id: #{id},  name: #{name} - get specific video game JSON")
                                    .get("/videogame/#{id}")
                                    .check(status().is(200))
                                    .check(jmesPath("name").isEL("#{name}"))));


    private static ChainBuilder getSingleSpecificViedoGameByCustomFeeder =
            repeat(10).on(
                    feed(CustomFeeders.iterateTo10)
                            .exec(http("get specific video game with id: #{gameId} - customFeeder")
                                    .get("/videogame/#{gameId}")
                                    .check(status().is(200))));

    private final ScenarioBuilder sc = scenario("Video Game Db - GET METHOD")
            .exec(getAllViedoGames)
            .pause(5)
            .exec(getSingleSpecificViedoGameByCsvFeeder)
            .pause(5)
            .exec(getSingleSpecificViedoGameByJsonFeeder)
            .pause(5)
            .exec(getSingleSpecificViedoGameByCustomFeeder)
            .exec(getAllViedoGames);


    {
        setUp(sc.injectOpen(atOnceUsers(3))
                .protocols(httpProtocol));
    }
}

