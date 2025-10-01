package neko.kuro.projectGorgonCrafting.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import neko.kuro.projectGorgonCrafting.Exceptions.WrappedException;
import neko.kuro.projectGorgonCrafting.entities.ItemAmount;
import neko.kuro.projectGorgonCrafting.entities.Recipe;
import neko.kuro.projectGorgonCrafting.repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller("/scraper")
public class ScraperController {
    private static final Logger logger = LoggerFactory.getLogger(ScraperController.class);

    private RecipeRepository recipeRepository;

    public ScraperController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Get("/alchemy")
    public HttpResponse<String> scrapeAlchemy(HttpRequest<String> request) throws WrappedException {
        final String url = "https://wiki.projectgorgon.com/wiki/Alchemy/Recipes";
        Duration delay = Duration.of(1, ChronoUnit.SECONDS);

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            List<URI> worklist = new ArrayList<>();
            worklist.add(new URI(url));

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            while(!worklist.isEmpty()) {
                Instant tStart = Instant.now();

                URI uri = worklist.get(0);

                java.net.http.HttpRequest pageLoadRequest = java.net.http.HttpRequest.newBuilder(uri).GET().build();
                java.net.http.HttpResponse<String> pageLoadResponse = client.send(pageLoadRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

                String page = pageLoadResponse.body();
                // begin page parsing
                boolean parsing = false;
                int index = 0;
                Recipe recipe = null;
                ItemAmount itemAmount = null;

                XMLEventReader reader = xmlInputFactory.createXMLEventReader(new ByteArrayInputStream(page.getBytes()));
                while(reader.hasNext()) {
                    XMLEvent nextEvent = reader.nextEvent();
                    if (!parsing) {
                        if (nextEvent.isEndElement()) {
                            EndElement endElement = nextEvent.asEndElement();
                            if (endElement.getName().getLocalPart().equals("tr")) {
                                // we've reached the end of the header row, game on
                                parsing = true;
                            }
                        }
                    } else {
                        if (nextEvent.isStartElement()) {
                            StartElement startElement = nextEvent.asStartElement();
                            switch (startElement.getName().getLocalPart()) {
                                case "tr": {
                                    recipe = new Recipe();
                                    index = 0;
                                    break;
                                }
                                case "td": {
                                    if (index == 0) {
                                        nextEvent = reader.nextEvent();
                                        recipe.setLevel(Integer.parseInt(nextEvent.asCharacters().getData().trim()));
                                    } else if (index == 1) {
                                        nextEvent = reader.nextEvent();
                                        recipe.setName(nextEvent.asCharacters().getData().trim());
                                    } else if (index == 2) {
                                        nextEvent = reader.nextEvent();
                                        recipe.setXpForFirstCrafting(Integer.parseInt(nextEvent.asCharacters().getData().trim()));
                                    } else if (index == 3) {
                                        nextEvent = reader.nextEvent();
                                        recipe.setXpForSubsequentCrafting(Integer.parseInt(nextEvent.asCharacters().getData().trim()));
                                    } else if (index == 4 || index == 5) {
                                        // ingredients, complex
                                        // set a flag, as we need to parse a lot of stuff before the /td
                                        nextEvent = reader.nextEvent();
                                        itemAmount = new ItemAmount();
                                        itemAmount.setAmount(Integer.parseInt(nextEvent.asCharacters().getData().substring(1)));
                                    } else if (index == 6) {
                                        nextEvent = reader.nextEvent();
                                        recipe.setDescription(nextEvent.asCharacters().getData());
                                    } else if (index == 7) {
                                        // sources, complex
                                    }
                                    break;
                                }
                                case "a": {
                                    if (index == 4) {
                                        String line = nextEvent.asCharacters().getData();
                                        int hrefIndex = 0;
                                    }
                                    break;
                                }
                            }
                        } else if (nextEvent.isEndElement()) {
                            EndElement endElement = nextEvent.asEndElement();
                            switch (endElement.getName().getLocalPart()) {
                                case "td": {
                                    // end of element
                                    index++;
                                    break;
                                }
                                case "tr": {
                                    // end of row
                                    recipeRepository.save(recipe);
                                    break;
                                }
                                case "table": {
                                    parsing = false;
                                    break;
                                }
                            }
                        }
                    } // end if parsing
                } // end hasNext
            } // end while has work
        } catch (URISyntaxException ex) {
            String msg = "Unable to parse URI";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        } catch (InterruptedException ex) {
            String msg = "Page load interrupted";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        } catch (IOException ex) {
            String msg = "Page load exception";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        } catch (XMLStreamException ex) {
            String msg = "Unable to instantiate XML parsing";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        }
        return HttpResponse.accepted().body("Yes");
    }

}
