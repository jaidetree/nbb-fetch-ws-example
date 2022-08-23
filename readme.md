# Node Babashka (nbb) ClojureScript Repl

## What is it?

[nbb](https://github.com/babashka/nbb) is a [ClojureScript](https://clojurescript.org/) interpreter that runs in node. It can be installed like any other npm package and can run .cljs files either directly like node or like the `clj` cli using the `-cp` flag to set the claspath and `-m` to target the `-main` function within a namespace.

## Why?

Unlike official cljs, nbb requires no build step! It is a binary tool that runs .cljs files directly, even macros are handled by nbb directly. While it does not fully support all official ClojureScript yet, it does support most of the language and has the advantage of running cljs quickly. The official Clojure replit takes almost a minute to startup, while this template can run in just a few seconds. It even supports starting an nrepl-server out of the box.

## How

What makes nbb special is that is fully implemented in JS so it does not need to use Java to transform the macros, or produce a bundle. It's a lightweight tool built internally with [shadow-cljs](https://github.com/thheller/shadow-cljs).

To run an arbitrary cljs script try invoking the following in a replit shell:

```sh
npx nbb script-example.cljs
```

To invoke a cljs ns `-main` function similar to using the official `clj` cli tool, try invoking the following in a replit shell:

```sh
npx nbb -cp src -m cli.core
```

As of https://github.com/babashka/nbb/pull/243 projects may contain a root nbb.edn to specify cljs deps and add to the classpath for cljs-style `require` calls.

### Who

`nbb` was written by [Michiel "Borkdude" Borkent](https://github.com/borkdude), the author of [Babashka](https://github.com/babashka/babashka)

## Where

Visit https://github.com/babashka/nbb to learn more about the project.