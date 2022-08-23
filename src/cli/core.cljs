(ns cli.core
  (:require
   [clojure.pprint :refer [pprint]]
   [nbb.repl :as repl]
   [promesa.core :as p]
   [clojure.repl :refer [doc]]
   [example.server.echo :as echo-server]))

(defn serialize
  [body]
  (-> body
      (clj->js)
      (js/JSON.stringify nil 2)))

(defn log
  [response]
  (println "Response")
  (pprint response))

(defn fetch
  [body]
  (p/-> (js/fetch 
          "http://localhost:8000"
          (clj->js {:method "POST"
                     :headers {:Content-Type "application/json"}
                     :body (serialize body)}))
         (.json)
         (js->clj :keywordize-keys true)
         (log)
         (p/catch js/console.error)))

(defn -main
  []
  (p/do! 
   (echo-server/start!) 
   (fetch
    {:status :ok
     :message "It works"})
   (repl/repl)
   (println "Repl closed")))