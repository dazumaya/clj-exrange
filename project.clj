(defproject com.github.dazumaya/clj-exrange "0.1.0"
  :description "clj-exrange expands glob-like text patterns."
  :url "https://github.com/dazumaya/clj-exrange"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases false}]]
  :repl-options {:init-ns clj-exrange.core}
  :main clj-exrange.core
  :aot [clj-exrange.core])
