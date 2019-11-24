"# tinyUrlService" 

Requisite
_________

Redis server 


Built using
__________

Redis DB(key value pair of type (string, url))

Spring boot

Maven build tool


tinyUrlClient code available in repo access below endpoints

EndPoint API
____________

http://localhost:8085/rest/url - to map original url with short url

http://localhost:8085/rest/url/{id} - to retrieve original url from short url

