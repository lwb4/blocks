(ns blocks.start)

(def source-code-starting
"; form

(+ 2 2)
(str \"hello \" \"world\") (+ 2 (* 2 (/ 2 2) 1) 0)

; vector

[:a :b [:c :d [:e] [:f]]] [:four (+ 2 2) :six (* 1 (+ 3 3))] (apply + [1 2 3 4 5])

; map

{:1 1 :2 2 :3 3} {:one (- 2 1) :two (str \"t\" \"w\" \"o\") :empties [() {} []]} 

; quotes

'(quoted symbols)

; hash sets

#{1 2 (+ 2 1)} (defn hash [a b c] #{a b c})

; syntax quoting

(defmacro with-base-env [& body]
  `(binding [*warn-on-reflection* false]
     (with-bindings (if (locals)
                      {}
                      {Compiler/LOCAL_ENV {}})
       ~@body)))

; meta maps

(def ^{:debug true} five 5)

; anonymous function

#(println \"Hello, \" %)

; quoted anonymous function

'#(+ % %)

; syntax quoted anonymous function

(macroexpand `#(println %))

; reader conditionals

#?(:clj  (Clojure expression)
   :cljs (ClojureScript expression)
   :cljr (Clojure CLR expression)
   :default (fallthrough expression))

; splicing reader conditionals

(defn build-list []
  (list #?@(:clj  [5 6 7 8]
            :cljs [1 2 3 4])))")
