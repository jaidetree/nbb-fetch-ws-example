;; A small browser script to connect to the websocket server then on any
;; incoming http express request, append to the log element.


;; Determine correct ws url based on current address and protocol
(def page-url (js/URL. js/window.location.href))
(def host (.-host page-url))
(def protocol 
  (if (.startsWith (.-protocol page-url) "https")
    "wss:"
    "ws:"))
(def wss-url (str protocol "//" host))

;; Connect to the socket server
(def socket (js/WebSocket. wss-url))

(defn on-open
  "
  Event handler when the websocket connection is opened
  "
  [event]
  (println "Connected to websocket server..."))

(defn on-message
  "
  Event handler when the websocket connection receives any message
  "
  [event]
  (let [log-el (js/document.getElementById "log")
        text (.-innerText log-el)]
    (set! (.-innerText log-el) (str text (.-data event)))))


(.addEventListener socket "open" on-open)
(.addEventListener socket "message" on-message)

;; Expose window.socket for debugging in console
(set! (.-socket js/window) socket)
