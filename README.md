# clj-exrange

[![Clojars Project](https://img.shields.io/clojars/v/com.github.dazumaya/clj-exrange.svg)](https://clojars.org/com.github.dazumaya/clj-exrange)

clj-exrange expands glob-like text patterns.

## Usage

```clojure
;; Repl
> (exrange "server-{master,[01-02]}.example.{com,jp}")
["server-master.example.com"
 "server-master.example.jp"
 "server-01.example.com"
 "server-01.example.jp"
 "server-02.example.com"
 "server-02.example.jp"]
```

## License

MIT License

Copyright (c) 2023 dazumaya
