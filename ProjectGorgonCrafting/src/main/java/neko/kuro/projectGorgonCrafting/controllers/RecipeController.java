package neko.kuro.projectGorgonCrafting.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/recipe")
public class RecipeController {

    @Get()
    public String hello() {
        return "Hello";
    }
}