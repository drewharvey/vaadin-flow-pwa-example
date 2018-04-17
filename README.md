
# Vaadin Flow PWA Example #

Simple web application that follows the PWA guidelines and runs offline.

Learn more about [Vaadin Flow Framework](https://vaadin.com/flow).

This application uses the [WebDSP webassembly](https://github.com/shamadee/web-dsp) 
module for filter effects.

## Starting the Server ##  

To start the server, open a terminal and run `mvn jetty:run` and open 
[http://localhost:8080](http://localhost:8080) in browser.

## Enable Offline Support to a Vaadin Flow App ##

Adding basic offline support in Vaadin Flow is very simple. By creating just a few
files we can add enable the app to work offline. By using this Vaadin Flow app as an
example, let's cover everything we need to do in order to support offline.

Files we will create:
 * manifest.json
 * offline.html
 * register-sw.js
 * sw.js

The files we are about to create should be placed in the `src/main/webapp` directory root.
This directory gets served from the base url of the application. For example, if we
have a manifest.json file located at `src/main/webapp/manifest.json`, we will be able to 
access this file at `http://localhost:8080/manifest.json`. It is very important to have
the files located here, as it is a requirement for some of the PWA files (the service
worker file for example).

### Creating the manifest.json ###

The manifest file provides information about your PWA application. You can read more about
manifest files here: [Web App Manifest](https://developers.google.com/web/fundamentals/web-app-manifest/)

We create the manifest.json file and located it in the `src/main/webapp` directory. We will
provide basic information about our PWA.

```
{
  "short_name": "Vaadin Flow PWA Example",
  "name": "Vaadin Flow PWA",
  "icons": [
    {
      "src": "frontend/src/images/icons/icon-72x72.png",
      "sizes": "72x72",
      "type": "image/png"
    },
    {
      "src": "frontend/src/images/icons/icon-128x128.png",
      "sizes": "128x128",
      "type": "image/png"
    },
    {
      "src": "frontend/src/images/icons/icon-152x152.png",
      "sizes": "152x152",
      "type": "image/png"
    },
    {
      "src": "frontend/src/images/icons/icon-192x192.png",
      "sizes": "192x192",
      "type": "image/png"
    }
  ],
  "start_url": "/"
}
```


### Creating the offline.html ###

For this example we want to simply show a specific file when the app is offline. This
file will tell the user that they are offline and need to connect to the internet
in order to use the app's features. This way the user sees this page, instead of that
ugly offline browser page. We are able to do this because our service worker (we will cover
this soon) will cache the offline.html file in order for us to provide it even when 
the app is offline.

We create the offline.html page and locate it in the `src/main/webapp` directory. 

```
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="manifest" href="manifest.json">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <base href=".">
    <title>PWA App</title>
    <link rel="shortcut icon" href="icons/favicon.ico">
    <meta name="viewport" content="width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes">
</head>
<body>
    <h1>Must have internet connection to use this app</h1>
</body>
</html>
```

### Creating the service worker ###

The service worker will be in charge of caching our offline files and serving them
when we are offline.  You can read more about service workers here:
[Introduction to Service Worker](https://developers.google.com/web/ilt/pwa/introduction-to-service-worker)

We create the sw.js file in the `src/main/webapp` directory. This is our service worker file.
The main uses of this code is to cache any files we want accessible when offline. This
service worker also intercepts fetch requests and will return the offline.html file if
the application is offline.

```
var staticCacheName = 'static';
var version = 1;


function updateCache() {
    return caches.open(staticCacheName + version)
        .then(function (cache) {
            return cache.addAll([
                'offline.html',
                'manifest.json'
            ]);
        });
}

self.addEventListener('install', function (event) {
    event.waitUntil(updateCache());
});

var doesRequestAcceptHtml = function (request) {
    return request.headers.get('Accept')
        .split(',')
        .some(function (type) {
            return type === 'text/html';
        });
};

self.addEventListener('fetch', function (event) {
    var request = event.request;
    event.respondWith(
        fetch(request)
            .catch(function () {
                return caches.match('offline.html');
            })
    );
    if (doesRequestAcceptHtml(request)) {
        // HTML pages fallback to offline page
        event.respondWith(
            fetch(request)
                .catch(function () {
                    return caches.match('offline.html');
                })
        );
    } else {
        event.respondWith(
            caches.match(request)
                .then(function (response) {
                    return response || fetch(request);
                })
        );
    }
});
```

Now that we have our service worker file, we will create a wrapper that registers the 
service worker when the application is loaded. We create the register-sw.js file in
the `src/main/webapp` directory. 

```
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('sw.js').then(function(registration) {
        console.log('Service worker registration succeeded:', registration);
    }).catch(function(error) {
        console.log('Service worker registration failed:', error);
    });
}
```

### Including the PWA files ###

Now we have created all of the required files for our app to run offline and as a 
valid PWA.  The next thing to do is make sure these files are included in our webpages.
For this we will create a few files that allow us to include our new files into the 
`<head>` section of all of our webpages.

Files we will create:
 * CustomBootstrapListener.java
 * CustomVaadinServiceInitListener.java
 * new config file

Because Vaadin Flow generates the `<head>` section, we need a way to tell Flow to include
these files we've created. To do this will create a `BootstrapListener`. 

The `BootstrapListener` allows us to 
modify our default webpage "wrapper". This includes things such as the `<head>` section
of the page, where want to include links to the new files we've created.

We create a CustomBootstrapListener class in the `org.vaadin.pwa` package.  Here we will
override the `modifyBootstrapPage` in order to modify the `<head>` section of our 
webpages.

```
package org.vaadin.pwa;

import org.jsoup.nodes.Element;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;

public class CustomBootstrapListener implements BootstrapListener {
    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {

        final Element head = response.getDocument().head();

        // manifest needs to be prepended before scripts or it won't be loaded
        head.prepend("<link rel='manifest' href='manifest.json'>");

        // Add service worker
        response.getDocument().body().appendElement("script")
                .attr("src", "register-sw.js");
    }

}
```

Now we need to tell our Vaadin Flow app to use this specific bootstrap listener. To do this
we will implement a `VaadinServiceInitListener`. We create a CustomVaadinServiceInitListener
class in the `org.vaadin.pwa` package. Here we override the `serviceInit` method in order
to link our `CustomBootstrapListener` we created just previously.

```
package org.vaadin.pwa;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.DependencyFilter;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;

public class CustomVaadinServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(new CustomBootstrapListener());
    }
}
```

The last step is to tell our application to use the `CustomVaadinServiceInitListener` that
we have just created. To do this we need to create a file named
`com.vaadin.flow.server.VaadinServiceInitListener` and locate it in the 
`src/main/resources/META-INF/services` directory (you may have to create some directories).
Your app should now properly serve the `offline.html` file when offline!

### Testing your app offline ###

The easiest way to test that the offline support is working is to use the dev tools
inside Chrome. To do this:

 1. Run your app and open the apps url in chrome (for this app: http://localhost:8080).
 1. Open DevTools, go to the Application panel.
 1. On the left side go to the Service Worker tab.
 1. Enable the Offline checkbox.
 1. Reload the page.
 
If the offline support is working, then you should be presented with the `offline.html`
we created earlier.  If something is wrong, you will see the standard 
"There is no Internet connection" page.
 
To read more about testing offline support visit: [Testing App Offline](https://developers.google.com/web/fundamentals/codelabs/offline/#test_the_app)

### Conclusion ###

That is all that needs to be done to your Vaadin Flow application in order to be a
fully functioning Progressive Web App with offline support. To summerize what we did
here is an outline of the main points:

 * Create a manifest.json to describe our PWA.
 * Create a static offline.html file to be used when the app is offline.
 * Create and register a service worker js file to cache offline assets and handle serving the offline.html when offline.
 * Use a BootstrapListener to add our PWA required files to the `<head>` of each webpage.
 * Use the `VaadinServiceInitListener` to specify our `CustomBootstrapListener`.