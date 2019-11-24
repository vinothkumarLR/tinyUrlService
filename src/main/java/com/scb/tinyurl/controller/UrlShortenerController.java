package com.scb.tinyurl.controller;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.hash.Hashing;
import com.scb.tinyurl.models.Error;
import com.scb.tinyurl.models.Url;

@RestController
@CrossOrigin
@RequestMapping(value = "/rest/url")
public class UrlShortenerController {

  @Autowired
  private RedisTemplate<String, Url> redisTemplate;

  @Value("${redis.ttl}")
  private long ttl;
  
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public RedirectView getUrl(@PathVariable String id) {
	  
	RedirectView redirectView = new RedirectView();
    // get from redis
    Url url = redisTemplate.opsForValue().get(id);

    if (url == null) {
      //Error error = new Error("id", id, "No such key exists");
      redirectView.setUrl("http://localhost:4200/");
      return redirectView;
    }
    
    
    redirectView.setUrl(url.getUrl());
    
    return redirectView;
  }

  /**
   * Returns a short URL.
   */
  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity postUrl(@RequestBody @NotNull Url url) {

    UrlValidator validator = new UrlValidator(
        new String[]{"http", "https"}
    );

    // if invalid url, return error
    if (!validator.isValid(url.getUrl())) {
      Error error = new Error("url", url.getUrl(), "Invalid URL");
      return ResponseEntity.badRequest().body(error);
    }

    String id = Hashing.murmur3_32().hashString(url.getUrl(), Charset.defaultCharset()).toString();
    url.setId(id);
    url.setCreated(LocalDateTime.now());

    //store in redis
    redisTemplate.opsForValue().set(url.getId(), url, ttl, TimeUnit.SECONDS);

    return ResponseEntity.ok(url);
  }

}