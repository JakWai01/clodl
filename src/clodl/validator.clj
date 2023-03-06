(ns clodl.validator)

(defn in-wordlist?
  "Checks if the word is part of the given wordlist"
  [guess word-list]
  (boolean (some #{guess} word-list)))

(defn valid?
  "Check that the guess is made out of valid chars and 
   is exactly five characters long"
  [guess word-list]
  (when (not (and
              (= (count guess) 5)
              (every? #(Character/isLetter %) guess)
              (in-wordlist? guess word-list)))
    (throw (Exception. "Invalid word.")))
  guess)

(defmacro bind [m f]
  `(let [result# ~m]
     (if-not (instance? Exception result#)
       (~f result#)
       result#)))