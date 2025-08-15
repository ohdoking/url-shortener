# URL Shortener Planning

Plan for implementing basic functionality URL shortener.

## Requirements

1. build MVP  
    1.1 modern 
    1.2 nicely written
    1.3 maintainable
    1.4 production ready API
2. no user registeration, security
3. rest api
    3.1 url encoder
        - request: URL
        - response: identifier/hash(corresponds with requested URL)
    3.2 url decoder
        - request: identifier/hash
        - response: URL
4. OK to use external libraries/components

## Design Architecture

url shortener service - postgresql db(read heavy)

## Design API

- Shorten URL
`POST /api/url`
  - Request Body
    ```json
    {
      "original_url": "https://dkb.com"
    }
    ```
  - Response Body(201)
    ```json
    {
      "alias": "abc",
      "shorten_url": "http://localhost:8080/abc",
      "original_url": "https://dkb.com"
    }
    ```

- Retrieve the original URL
`GET /api/url/{identifier}`

  - Request Body - x
  - Response Body(200)
    ```json
    {
      "original_url": "https://dkb.com"
    }
    ```


- Redirect URL
`GET /{identifier}`

  - Request Body - x
  - Response Body - x -> just redirect with `301 Moved Permanently`


