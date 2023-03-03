(ns clodl.validator)

(defn in-wordlist?
  "Checks if the word is part of the wordlist"
  [guess word-list]
  (boolean (some #{guess} word-list)))

(defn valid?
  "Check that the guess is made out of valid chars and is exactly five characters long"
  [guess word-list]
  (and (and
   (= (count guess) 5)
   (every? #(Character/isLetter %) guess)) (in-wordlist? guess word-list)))
