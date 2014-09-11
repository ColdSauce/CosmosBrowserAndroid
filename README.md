CosmosBrowserAndroid
====================

Cosmos Browser allows the user to connect to the internet through the use of SMS. No data or WiFi required.

### How it works

After a person inputs a url, our app texts our Twilio number which forwards the URL as a POST request to our Node.JS backend. The backend takes the url, gets the HTML source of the website, minifies it, gets rid of the css, javascript, and images, GZIP compresses it, encodes it in Base64, and sends the data as a series of SMS's. The phone recieves this stream at a rate of 3 messages per second, orders them, decompresses them, and displays the content.

### Release plans

We're working hard to get our first MVP release out as soon as possible. Our expected release date is sometime in the third week of November, and we'll be rapidly updating with new features.

[Design Guidelines](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html)


Contributer | Task
--- | ---
Stefan | Android
Justice | Android
Rohith | Backend
Shreyas | Backend
