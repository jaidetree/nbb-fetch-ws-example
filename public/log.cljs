;; A small browser script to connect to the websocket server then on any
;; incoming http express request, append to the log element.


(def host js/window.location.host)
(def protocol
  (if (.startsWith js/window.location.host "https")
    "wss:"
    "ws:"))

(def socket (js/WebSocket. (str protocol "//" host)))

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
