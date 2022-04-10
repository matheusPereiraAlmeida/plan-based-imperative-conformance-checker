(define (problem Align) (:domain Mining)
(:objects
0 - place
6 - place
1 - place
7 - place
ev1 - event
ev2 - event
ev3 - event
ev4 - event
evEND - event
)
(:init
(tracePointer ev1)
(allowed)
(token 0)
(= (total-cost) 0)
)
(:goal
(and
(not (token 0))
(not (token 6))
(token 1)
(not (token 7))
(tracePointer evEND)
))
(:metric minimize (total-cost))
)