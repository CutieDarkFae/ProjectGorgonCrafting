package neko.kuro.projectGorgonCrafting.controllers;

import com.google.gson.Gson;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import neko.kuro.projectGorgonCrafting.Exceptions.WrappedException;
import neko.kuro.projectGorgonCrafting.repositories.ItemRepository;
import neko.kuro.projectGorgonCrafting.repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

@Controller("/scraper")
public class ScraperController {
    private static final Logger logger = LoggerFactory.getLogger(ScraperController.class);

    private RecipeRepository recipeRepository;
    private ItemRepository itemRepository;

    public ScraperController(
            RecipeRepository recipeRepository,
            ItemRepository itemRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.itemRepository = itemRepository;
    }

    // actually, I can do all this with the JSON files from the CDN, much nicer.
    @Get("/json/items")
    public HttpResponse<String> scrapeJson() throws WrappedException {
        String base = "https://cdn.projectgorgon.com/v435/data/";

        try (HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        ) {
            Gson gson = new Gson();
            String cdnIconBase = "http://cdn.projectgorgon.com/v435/icons/icon_";
            String cdnIconSuffix = ".png";

            URI uri = new URI("https://cdn.projectgorgon.com/v435/data/items.json");

            java.net.http.HttpRequest pageLoadRequest = java.net.http.HttpRequest.newBuilder(uri)
                .GET().build();
            java.net.http.HttpResponse<String> pageLoadResponse =
                client.send(pageLoadRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

            String page = pageLoadResponse.body();
            Map map;
            try {
                map = gson.fromJson(page, Map.class);
            } catch (IllegalStateException ex) {
                String msg = "During parsing:\n" + page;
                logger.error(msg, ex);
                throw new WrappedException(msg, ex);
            }
            for (Object key : map.keySet()) {
                neko.kuro.projectGorgonCrafting.entities.Item itemEntity = new neko.kuro.projectGorgonCrafting.entities.Item();
                Object obj = map.get(key);
                itemEntity.setInternalId((String)key);
                Map values = (Map)obj;
                String description = (String) values.get("Description");
                String name = (String) values.get("InternalName");
                int iconId = ((Double) values.get("IconId")).intValue();
                int value = ((Double) values.get("Value")).intValue();

                itemEntity.setDescription(description);
                itemEntity.setName(name);
                itemEntity.setSellsFor(value);
                itemEntity.setImage(new URI(cdnIconBase + iconId + cdnIconSuffix));

                itemRepository.save(itemEntity);
            }
        } catch (URISyntaxException ex) {
            String msg = "Unable to parse url";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        } catch (InterruptedException ex) {
            String msg = "Unable to connect to site";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        } catch (IOException ex) {
            String msg = "Unable to read from site";
            logger.error(msg, ex);
            throw new WrappedException(msg, ex);
        }
        return HttpResponse.ok();
    }
}
