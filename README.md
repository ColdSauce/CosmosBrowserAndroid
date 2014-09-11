CosmosBrowserAndroid
====================

Cosmos Browser allows the user to connect to the internet through the use of SMS. No data or WiFi required.

### How it works

After a person inputs a url, our app texts our Twilio number which forwards the URL as a POST request to our Node.JS backend. The backend takes the url, gets the HTML source of the website, minifies it, gets rid of the css, javascript, and images, GZIP compresses it, encodes it in Base64, and sends the data as a series of SMS's. The phone recieves this stream at a rate of 3 messages per second, orders them, decompresses them, and displays the content.

“So we created a web browser app. the app sends a text to our twilio backend which is the url you want. then the backend gets the HTML of that url, it minifies it, compresses it with the GZIP compression algorithm, encodes it in Base64 and then sends the data stream as a series of text messages to the phone which then get read and the browser renders the HTML.“ -[ComZero](http://www.reddit.com/r/Android/comments/2g3rom/cosmos_browser_enables_the_user_to_browse_the_web/ckfcz1k)

### Release plans

We're working hard to get our first MVP release out as soon as possible. We'll be rapidly updating with new features.

[Design Guidelines](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html)

[Backend](https://github.com/Rohfosho/CosmosBrowserBackend)


Contributer | Task
--- | ---
Stefan | Android
Justice | Android
Rohith | Backend
Shreyas | Backend
