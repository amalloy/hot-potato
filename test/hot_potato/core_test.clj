(ns hot-potato.core-test
  (:use hot-potato.core
        clojure.test))

(deftest permute-map-test
  (is (= [2 1]
           (permute {0 1} [1 2])
           (permute {1 0} [1 2]))))

(deftest swap-int-test
  (is (= [2 1]
           (permute 1 [1 2])
           (permute -1 [1 2]))))

(deftest permute-int-test
  (is (= [3 1 2]
           (permute 1 [1 2 3])
           (permute -2 [1 2 3])))
  (is (= [2 3 1]
           (permute -1 [1 2 3])
           (permute 2 [1 2 3]))))

(deftest basic-reorder-test
  (is (= [2 3] ((reorder map) [1 2] inc))))

(deftest permute-reorder-test
  (is (= 4/3 ((reorder {0 2} /) 1 3 4)))) ; (/ 4 1 3)
