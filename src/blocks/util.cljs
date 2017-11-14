(ns blocks.util
  (:require [goog.string :as gstring :refer 
              [whitespaceEscape escapeString escapeChar]]))

; Chars to parse:
;   "(" ")"     form
;   "[" "]"     vector
;   "{" "}"     map
;   "'(" ")"    quoted form
;   "#{" "}"    hash set
;   "`(" ")"    syntax quoted form
;   "^{" "}"    metadata map
;   "#(" ")"    anonymous function
;   "#?(" ")"   reader conditional
;   "'#(" ")"   quoted anonymous function
;   "`#(" ")"   syntax quoted anonymous function
;   "#?@(" ")"  splicing reader conditional
;   ignore anything between "" and ;\n

(defn reg-block [html]
  (str html "<div class='reg block'>"))

(defn vec-block [html]
  (str html "<div class='vec block'>"))

(defn map-block [html]
  (str html "<div class='map block'>"))

(defn end-block [html]
  (str html "</div>"))

(defn quo-block [html]
  "quote block - '()"
  (str html "<div class='quo block'>"))

(defn syn-block [html]
  "syntax quote block - `()"
  (str html "<div class='syn block'>"))

(defn set-block [html]
  "hash set - #{}"
  (str html "<div class='set block'>"))

(defn met-block [html]
  "metadata map - ^{}"
  (str html "<div class='met block'>"))

(defn ano-block [html]
  "anonymous function - #()"
  (str html "<div class='ano block'>"))

(defn rec-block [html]
  "reader conditional - #?()"
  (str html "<div class='rec block'>"))

(defn qan-block [html]
  "quoted anonymous function - '#()"
  (str html "<div class='qan block'>"))

(defn san-block [html]
  "syntax quoted anonymous function - `#()"
  (str html "<div class='san block'>"))

(defn spl-block [html]
  "splicing reader conditional - #?@()"
  (str html "<div class='spl block'>"))

(defn eq [l s]
  (let [check (interleave l s)]
    (not-any? false? 
        (for [[x1 x2] (partition 2 check)]
          (= x1 x2)))))

(defn churn-endquote [charlist html]
  (let [f (first charlist)
        r (rest charlist)]
    (cond (nil? f) (list () html)
          (and (= f "\\") (= (first r) "\"")) 
            (recur (rest r) (str html "\\\""))
          (= f "\"") (list r (str html f))
          :else (recur r (str html f)))))

(defn churn-newline [charlist html]
  (let [f (first charlist)
        r (rest charlist)]
    (cond (nil? f) (list () html)
          (= f "\n") (list charlist html)
          :else (recur r (str html f)))))

(defn -blockify
  [charlist stack html]
  (let [f (first charlist)
        r (rest charlist)]
    (cond (nil? f) (if (empty? stack) html "The code could not be read.")
        
        (= f "(") (recur r (conj stack ")") (reg-block html)) 
        (= f "[") (recur r (conj stack "]") (vec-block html))
        (= f "{") (recur r (conj stack "}") (map-block html))
        
        (eq charlist "'(")
          (recur (rest r) (conj stack ")") (quo-block html))
        (eq charlist "`(")
          (recur (rest r) (conj stack ")") (syn-block html))
        (eq charlist "#{")
          (recur (rest r) (conj stack "}") (set-block html))
        (eq charlist "^{")
          (recur (rest r) (conj stack "}") (met-block html))
        (eq charlist "#(")
          (recur (rest r) (conj stack ")") (ano-block html))

        (eq charlist "#?(")
          (recur (drop 2 r) (conj stack ")") (rec-block html))
        (eq charlist "`#(")
          (recur (drop 2 r) (conj stack ")") (san-block html))
        (eq charlist "'#(")
          (recur (drop 2 r) (conj stack ")") (qan-block html))

        (eq charlist "#?@(")
          (recur (drop 3 r) (conj stack ")") (spl-block html))
        
        (= f ")" (peek stack)) (recur r (pop stack) (end-block html))
        (= f "]" (peek stack)) (recur r (pop stack) (end-block html))
        (= f "}" (peek stack)) (recur r (pop stack) (end-block html))

        (= f "\"") 
          (let [[c h] (churn-endquote (rest charlist) (str html f))]
            (recur c stack h))
        (= f ";") 
          (let [[c h] (churn-newline (rest charlist) (str html f))]
            (recur c stack h))

        (= f "\n") (recur r stack (str html "<br />"))
        :else (recur r stack (str html f)))))
   
(defn blockify
  [source-code]
  (-blockify (clojure.string/split source-code #"") () ""))
