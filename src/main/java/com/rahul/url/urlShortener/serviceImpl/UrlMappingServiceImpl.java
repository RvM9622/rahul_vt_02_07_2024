package com.rahul.url.urlShortener.serviceImpl;

import com.rahul.url.urlShortener.entity.UrlMapping;
import com.rahul.url.urlShortener.repository.UrlMappingRepository;
import com.rahul.url.urlShortener.service.UrlMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlMappingServiceImpl implements UrlMappingService {

    @Autowired
    private UrlMappingRepository repository;

    @Override
    public String shortenUrl(String destinationUrl) {

        final String BASE_URL = "http://localhost:8080/";

        String shortUrl = generateShortUrl();
        LocalDateTime expirationDate = LocalDateTime.now().plusMonths(10);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setDestinationUrl(destinationUrl);
        urlMapping.setExpirationDate(expirationDate);

        repository.save(urlMapping);

        return BASE_URL + shortUrl;
    }

    @Override
    public Optional<String> getDestinationUrl(String shortUrl) {
        return repository.findByShortUrl(shortUrl)
                .filter(urlMapping -> urlMapping.getExpirationDate().isAfter(LocalDateTime.now()))
                .map(UrlMapping::getDestinationUrl);
    }

    @Override
    public boolean updateShortenUrl(String shortUrl, String newDestinationUrl) {

        Optional<UrlMapping> optionalUrlMapping = repository.findByShortUrl(shortUrl);
        if (optionalUrlMapping.isPresent()){
            UrlMapping urlMapping = optionalUrlMapping.get();
            urlMapping.setDestinationUrl(newDestinationUrl);
            repository.save(urlMapping);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateExpiry(String shortUrl, int daysToAdd) {

        Optional<UrlMapping> optionalUrlMapping = repository.findByShortUrl(shortUrl);
        if (optionalUrlMapping.isPresent()){
            UrlMapping urlMapping = optionalUrlMapping.get();
            urlMapping.setExpirationDate(urlMapping.getExpirationDate().plusDays(daysToAdd));
            repository.save(urlMapping);
            return true;
        }
        return false;
    }

    private String generateShortUrl() {

        final int SHORT_URL_LENGTH = 6;

        Random random = new Random();
        StringBuilder shortUrl  = new StringBuilder();

        for (int i = 0; i < SHORT_URL_LENGTH; i++){
            int charType = random.nextInt(3);
            if (charType == 0) {
                shortUrl.append((char) ('a' + random.nextInt(26)));
            } else if (charType == 1) {
                shortUrl.append((char) ('A' + random.nextInt(26)));
            } else {
                shortUrl.append((char) ('0' + random.nextInt(10)));
            }
        }
        return shortUrl.toString();
    }
}
