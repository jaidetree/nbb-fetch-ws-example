(ns example.server.echo
  "An example server that just echos json POST requests"
  (:require 
   [clojure.pprint :refer [pprint]]
   [promesa.core :as p]
   ["express$default" :as express]
   ["ws$default" :refer [WebSocketServer] :as WebSocket]))

(def http-port 8000)

(def app (express))

(defn- on-start 
  [err]
  (if err 
    (js/console.error err)
    (println "echo server listening on port" http-port)))

(defn broadcast
  [wss msg]
  (let [clients (->> (.-clients wss)
                     (js->clj)
                     (filter #(= (.-readyState %) (.-OPEN WebSocket))))]
    (doseq [client clients]
      (.send client (str "[" (-> (js/Date.) (.toISOString)) "] "
                         (:method msg)     " "
                         (:url msg)        " "
                         (prn-str (:body msg)))))))

(defn echo
  [req res]
  (broadcast
   (.-wss req)
   {:method "POST"
    :url (.-url req)
    :body (js->clj (.-body req) :keywordize-keys true)})
  (doto res
    (.setHeader "Content-Type" "application/json")
    (.json (.-body req))))

(defn request-wss-mw
  "
  Request websocket server middleware
  Assigns wss to the req object
  "
  [wss]
  (fn wss-middleware
    [req _res next]
    (set! (.-wss req) wss)
    (next)))

(defn start-http-server
  [wss]
  (doto app
    (.use (.json express))
    (.use (.static express "public"))
    (.use (request-wss-mw wss))
    (.post "*" echo))

  (js/Promise.
   (fn [resolve _reject]
     (resolve
      (.listen app
               http-port
               on-start)))))


(defn start!
  "
  Quick example server to echo JSON.
  Returns a promise when the server is ready for requests.
  "
  []
  (p/let [wss         (WebSocketServer. #js {:noServer true})
          http-server (start-http-server wss)]
    (p/create
     (fn [resolve _reject]
       (.on http-server "upgrade"
            (fn [request socket head]
              (.handleUpgrade wss request socket head
                              (fn [socket]
                                (println "WebSocket client connected...")
                                (.emit wss "connection" socket request)
                                (resolve {:wss wss :http http-server})))))))))
