(defproject clodl "0.1.0-SNAPSHOT"
  :description "Play wordle in the terminal with clodl"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :repl-options {:init-ns clodl.core}
  :main clodl.core/-main
  :plugins [[lein-cljfmt "0.9.2"]])
