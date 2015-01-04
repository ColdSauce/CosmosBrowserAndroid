CosmosBrowserAndroid
====================
##Backend [Here](https://github.com/Rohfosho/CosmosBrowserBackend)


Cosmos Browser allows the user to connect to the internet through the use of SMS. No data or WiFi required.

###Made with <3 at MHacks IV

### How it works

After a person inputs a url, our app texts our Twilio number which forwards the URL as a POST request to our Node.JS backend. The backend takes the url, gets the HTML source of the website, minifies it, gets rid of the css, javascript, and images, GZIP compresses it, encodes it in Base64, and sends the data as a series of SMSes. The phone recieves this stream at a rate of 3 messages per second, orders them, decompresses them, and displays the content.

### Release plans

We're working hard to get our first MVP release out as soon as possible. We'll be rapidly updating with new features.

[Design Guidelines](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html)

### Contributing

We are currently working on getting the browser completely decentralized such that a person can donate their phone in down time as a sort of "server". This is similar to how torrents work. We will have a centralized server that will track all of these donation phones and connect them to phones that require internet.

Contributer | Task
--- | ---
Stefan | Android
Rohith | Backend
Chen | Design
Shreyas | Backend
Justice | Android
