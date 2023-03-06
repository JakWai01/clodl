(ns clodl.validator)

(defn in-wordlist?
  "Checks if the word is part of the wordlist"
  [guess word-list]
  (boolean (some #{guess} word-list)))

; You can leave out everything except 'in-wordlist'
(defn valid?
  "Check that the guess is made out of valid chars and is exactly five characters long"
  [guess word-list]
  (and
   (= (count guess) 5)
   (every? #(Character/isLetter %) guess)) (in-wordlist? guess word-list))

(defmacro try-monad
  [expr handler]
  `(try ~expr
        (catch Exception e# (~handler e#))))

; Use this for valid?
; Use this for in-wordlist

(defn divide [a b]
  (try-monad (/ a b) (constantly nil)))

(defn divide-and-handle [a b]
  (try-monad (/ a b)
             (fn  [e]
               (if  (instance? ArithmeticException e)
                 "Cannot divide by zero."
                 (str "An error occured: " e)))))

;; (divide 10 0) ; Returns nil
;; (divide-and-handle 10 0) ; Returns "Cannot divide by zero."
;; (divide-and-handle 10 "foo") ; Returns "An error occurred: java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Number"