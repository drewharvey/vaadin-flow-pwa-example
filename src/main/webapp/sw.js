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

// this.addEventListener('fetch', event => {
//     // request.mode = navigate isn't supported in all browsers
//     // so include a check for Accept: text/html header.
//     if (event.request.mode === 'navigate' || (event.request.method === 'GET' && event.request.headers.get('accept').includes('text/html'))) {
//         event.respondWith(
//             fetch(event.request.url).catch(error => {
//                 // Return the offline page
//                 return caches.match('offline.html');
//             })
//         );
//     }
//     else{
//         // Respond with everything else if we can
//         event.respondWith(caches.match(event.request)
//             .then(function (response) {
//                 return response || fetch(event.request);
//             })
//         );
//     }
// });