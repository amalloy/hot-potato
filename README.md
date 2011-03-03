# hot potato

[hot-potato](https://github.com/amalloy/hot-potato) is a small utility
for juggling around the order in which function arguments are passed.

The general usage is `((reorder map) [1 2 3] inc) => (2 3 4)`:
hot-potato takes the input function `map` and returns a new function
that messes around with argument order before passing them along; you
can then invoke that function or pass it around as you would any other
function.

## API

`reorder` generally messes with the parameter order, and in the
simplest case (seen above), it merely swaps the two arguments
(generalized to reverse them if there are more than two). If you want
more specialized behavior, you can supply an additional argument to
`reorder` dictating how to permute the argument list. Integers cause
arguments to be rotated/cycled: to the right for positive integers and
to the left for negative. Maps swap arguments, so eg `((reorder {0 2}
-) 1 4 6)` invokes `(- 6 4 1)`. For more fine-tuned swap-like
behavior, you can use vectors: `((reorder [[0 1] [0 2]] +) 1 2 3))`
copies the 0th argument into slots 1 and 2, *discarding* the displaced
arguments, so it will invoke `(+ 1 1 1)`.

## Extensions

Reorder is built upon a generic permutation function called `permute`,
which has the three above built-in implementations. If you would like
to add additional permutation behavior, you can define a method of
`hot-potato.core/permute` to modify the argument vector according to any data
structure you like.

## Caveats

Permute does not work for infinite argument sequences, even in the
cases where it could: it converts the arguments into a vector before
doing any swaps. This makes implementing permutions simpler, but will
probably be changed in a future version. In the unlikely case that you
implement a new permutation and I introduce a breaking change to it,
[let me know](https://github.com/amalloy)
