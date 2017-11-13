(ns blocks.util
  (:require [goog.string :as gstring :refer 
              [whitespaceEscape escapeString escapeChar]]))

(def special-chars
  {"(" ")"
   "[" "]"
   "{" "}"
   "'(" ")"
   "#{" "}"
   "`(" ")"
   "^{" "}"
   "#(" ")"
   "#?(" ")"
   "`#(" ")"
   "'#(" ")"
   "#?@(" ")"})

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

(defn san-block [html]
  "syntax quoted anonymous function - `#()"
  (str html "<div class='san block'>"))

(defn qan-block [html]
  "quoted anonymous function - '#()"
  (str html "<div class='qan block'>"))

(defn spl-block [html]
  "splicing reader conditional - #?@()"
  (str html "<div class='spl block'>"))

(defn eq [x xs s]
  (let [check (interleave xs (rest s))]
    (and (= x (first s))
         (not 
           (some false? 
             (for [[x1 x2] (partition 2 check)]
               (= x1 x2)))))))

(defn -blockify
  [[x & xs] stack html]
  (cond (nil? x) (if (empty? stack) html "The code could not be read.")
        
        (= x "(") (recur xs (conj stack ")") (reg-block html)) 
        (= x "[") (recur xs (conj stack "]") (vec-block html))
        (= x "{") (recur xs (conj stack "}") (map-block html))
        (= x ")" (peek stack)) (recur xs (pop stack) (end-block html))
        (= x "]" (peek stack)) (recur xs (pop stack) (end-block html))
        (= x "}" (peek stack)) (recur xs (pop stack) (end-block html))
        
        ;(and (= x "'") (= (first xs) "("))
        (eq x xs "'(")
          (recur (rest xs) (conj stack ")") (quo-block html))
        ;(and (= x "`") (= (first xs) "("))
        (eq x xs "`(")
          (recur (rest xs) (conj stack ")") (syn-block html))
        ;(and (= x "#") (= (first xs) "{"))
        (eq x xs "#{")
          (recur (rest xs) (conj stack "}") (set-block html))
        ;(and (= x "^") (= (first xs) "{"))
        (eq x xs "^{")
          (recur (rest xs) (conj stack "}") (met-block html))
        ;(and (= x "#") (= (first xs) "("))
        (eq x xs "#(")
          (recur (rest xs) (conj stack ")") (ano-block html))

        ;(and (= x "#") (= (first xs) "?") (= (second xs) "("))
        (eq x xs "#?(")
          (recur (drop 2 xs) (conj stack ")") (rec-block html))
        (eq x xs "`#(")
          (recur (drop 2 xs) (conj stack ")") (san-block html))
        (eq x xs "'#(")
          (recur (drop 2 xs) (conj stack ")") (qan-block html))
        (eq x xs "#?@(")
          (recur (drop 3 xs) (conj stack ")") (spl-block html))
        
        (= x "\n") (recur xs stack (str html "<br />"))
        :else (recur xs stack 
                (str html x))))
   
(defn blockify
  [source-code]
  (-blockify (clojure.string/split source-code #"") () ""))
