(ns hot-potato.core
  (:use [amalloy.utils.debug :only [?]]))

(defmulti permute
  "Interpret some kind of instruction list for reordering the elements
  of a vector. Need not return a vector; any seq will do. Built-in
  implementations exist for maps, which are read as a series of swaps
  to make in the vector, and numbers, which are treated as rotations."
  (fn [how v]
    (type how)))

;; warning warning unhygenic macro. exposes the following symbols to
;; the expansion context: orig (original vector), acc (result
;; accumulator), a/b (swap indexes)
(defmacro pair-permuter [class how]
  `(defmethod permute ~class [swaps# ~'orig]
     (reduce (fn ~'[acc [a b]]
               ~how)
             ~'orig swaps#)))

;; Interpret maps as a set of swaps to make: {0 2} and {2 0} both mean
;; to swap positions 0 and 2
(pair-permuter
 clojure.lang.IPersistentMap
 (assoc acc
   a (orig b)
   b (orig a)))

;; Vectors are like maps, but one-way instead of two-way: [[0 2] [2
;; 0]] is the same as {0 2} for maps, but [[0 2]] copies the 0th
;; parameter into the 2nd slot AND ALSO passes it in the 0th
;; slot. This allows more flexible reordering if the swap methodology
;; is insufficient
(pair-permuter
 clojure.lang.IPersistentVector
 (assoc acc a (orig b)))

;; permute +1 turns (rotated 1 2 3) into (original 3 1 2)
;; permute -1 turns (rotated 1 2 3) into (original 2 3 1)
(defmethod permute Number
  [rot v]
  (apply concat (if (pos? rot)
                  ((juxt take-last drop-last) rot v)
                  ((juxt drop take) (- rot) v))))

(defn reorder
  "Create a new version of the given function which behaves the same
but takes its arguments in a different order. Before being passed to
the base function, args are permuted as defined by the 'how'
argument (see the permute function in this namespace for details on
how permutations can be specified).

For convenience, if no permutation is defined then reorder assumes a
two-argument function and simply swaps the arguments."
  ([f] (fn [a b] (f b a)))
  ([how f] (fn [& args] (apply f (permute how (vec args))))))
