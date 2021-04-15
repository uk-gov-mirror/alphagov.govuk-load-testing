

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SearchAutocompleteApi extends Simulation {
    val dataDir = sys.props.getOrElse("dataDir", "src/test/resources/test-data")
    val searches = csv(dataDir + java.io.File.separatorChar + "autocomplete-searches.csv").readRecords

    val duration = sys.props.getOrElse("duration", "3600").toInt

    val scn = scenario("Load test")
        .foreach(searches, "search") {
            exec(http("Search")
                .get("${search_term}"))
            .pause(50.milliseconds, 150.milliseconds)
        }
    val workers = sys.props.getOrElse("workers", "1").toInt
    val ramp = sys.props.getOrElse("ramp", "1").toInt
    val maxTime = sys.props.getOrElse("maxTime", "3600").toInt
    setUp(
      scn.inject(constantUsersPerSec(workers) during (ramp seconds) randomized)
    ).maxDuration(maxTime seconds)


}

