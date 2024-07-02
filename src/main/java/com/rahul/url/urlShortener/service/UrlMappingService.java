package com.rahul.url.urlShortener.service;

import java.util.Optional;

public interface UrlMappingService {

    public String shortenUrl(String destinationUrl);

    public Optional<String> getDestinationUrl(String shortUrl);

    public boolean updateShortenUrl(String shortUrl, String newDestinationUrl);

    public boolean updateExpiry(String shortUrl, int daysToAdd);

}
