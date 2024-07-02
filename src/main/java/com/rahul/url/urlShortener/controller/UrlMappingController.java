package com.rahul.url.urlShortener.controller;

import com.rahul.url.urlShortener.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlMappingController {

    @Autowired
    private UrlMappingService service;

    @PostMapping("/shorten")
    public Map<String, String> shortenUrl(@RequestParam String destinationUrl){
        String shortenUrl = service.shortenUrl(destinationUrl);
        return Map.of("shortUrl", shortenUrl);
    }

    @GetMapping("/{shortenString}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString){
        service.getDestinationUrl(shortenString).ifPresentOrElse(
                fullUrl -> {
                    try{
                        response.sendRedirect(fullUrl);
                    }catch (IOException e){
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
                    }
                },
                () ->{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found");
                }
        );
    }

    @PostMapping("/update")
    public boolean updateShortUrl(@RequestParam String shortUrl, @RequestParam String destinationUrl){
        return service.updateShortenUrl(shortUrl, destinationUrl);
    }

    @PostMapping("/updateExpiry")
    public boolean updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd){
        return service.updateExpiry(shortUrl, daysToAdd);
    }

}
