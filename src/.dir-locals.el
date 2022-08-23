;; When working with nbb in emacs, it needs to use clojure-mode not
;; clojurescript-mode to connect to a repl. This handles that automatically in
;; emacs by assigning clojure mode instead.
((clojurescript-mode (mode . clojure)))
