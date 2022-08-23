(def socket (js/WebSocket. "wss://Node-Babashka-Fetch-Example.eccentric-j.repl.co"))

(defn on-open
  [event]
  (println "Connected to server via ws...")
  (js/console.log event))

(defn on-message
  [event]
  (println event)
  (let [log-el (js/document.getElementById "log")]
    (.appendChild log-el (js/document.createTextNode (.-data event)))))


(.addEventListener socket "open" on-open)
(.addEventListener socket "message" on-message)

(set! (.-socket js/window) socket)