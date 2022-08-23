(ns example.server.echo
  "An example server that just echos json POST requests"
  (:require 
   [clojure.pprint :refer [pprint]]
   [promesa.core :as p]
   ["express$default" :as express]
   ["ws$default" :refer [WebSocketServer] :as WebSocket]))

(def http-port 8000)
(def ws-port 8080)

(def app (express))
(def wss (atom nil))

(defn- on-start 
  [err]
  (if err 
    (js/console.error err)
    (println "Echo server listening on port" http-port)))

(defn broadcast
  [msg]
  (let [wss @wss
        clients (->> (.-clients wss)
                     (js->clj)
                     (filter #(= (.-readyState %) (.-OPEN WebSocket))))]
    (doseq [client clients]
      (.send client (str "[" (js/Date.now) "]" (:method msg) " " (:url msg) " " (pr-str (:body msg)))))))

(defn echo
  [req res]
  (println "Echo server received request")
  (broadcast 
   {:method "POST"
    :url (.-url req)
    :body (js->clj (.-body req) :keywordize-keys true)})
  (.json res (.-body req)))

(defn start-http-server
  []
  (doto app
    (.use (.json express))
    (.use (.static express "public"))
    (.post "*" echo))

  (js/Promise.
   (fn [resolve _reject]
     (.listen app 
              http-port 
              #(do 
                 (on-start %)
                 (resolve app))))))

(defn start-ws-server
  []
  (let [server (WebSocketServer. #js {:server app})]
    (.on server "connection"
         (fn [ws]
           (.send ws "Greetings")))
    (reset! wss server)))

(defn start!
  "
  Quick example server to echo JSON.
  Returns a promise when the server is ready for requests.
  "
  []
  (p/do!
   (start-http-server)
   (start-ws-server)))
