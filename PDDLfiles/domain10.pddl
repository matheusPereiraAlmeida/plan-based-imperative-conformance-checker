(define (domain Mining)
(:requirements :typing :equality)
(:types place event)

(:predicates
(token ?p - place)
(tracePointer ?e - event)
(allowed)
)

(:functions
(total-cost)
)

(:action moveSync#c#ev4
:precondition (and (token 7) (tracePointer ev4))
:effect (and (allowed) (not (token 7)) (token 1) (not (tracePointer ev4)) (tracePointer evEND))
)

(:action moveInTheModel#c
:precondition (token 7)
:effect (and (not (allowed)) (not (token 7)) (token 1) (increase (total-cost) 1)
)
)

(:action moveInTheModel#generatedinv0
:precondition (token 7)
:effect (and (not (allowed)) (not (token 7)) (token 6) (increase (total-cost) 0)
)
)

(:action moveSync#b#ev2
:precondition (and (token 6) (tracePointer ev2))
:effect (and (allowed) (not (token 6)) (token 7) (not (tracePointer ev2)) (tracePointer ev3))
)

(:action moveSync#b#ev3
:precondition (and (token 6) (tracePointer ev3))
:effect (and (allowed) (not (token 6)) (token 7) (not (tracePointer ev3)) (tracePointer ev4))
)

(:action moveInTheModel#b
:precondition (token 6)
:effect (and (not (allowed)) (not (token 6)) (token 7) (increase (total-cost) 1)
)
)

(:action moveSync#a#ev1
:precondition (and (token 0) (tracePointer ev1))
:effect (and (allowed) (not (token 0)) (token 6) (not (tracePointer ev1)) (tracePointer ev2))
)

(:action moveInTheModel#a
:precondition (token 0)
:effect (and (not (allowed)) (not (token 0)) (token 6) (increase (total-cost) 1)
)
)

(:action moveInTheModel#d
:precondition (token 7)
:effect (and (not (allowed)) (not (token 7)) (token 1) (increase (total-cost) 1)
)
)

(:action moveInTheLog#a#ev1-ev2
:precondition (and (tracePointer ev1) (allowed))
:effect (and (not (tracePointer ev1)) (tracePointer ev2) (increase (total-cost) 1)
))

(:action moveInTheLog#b#ev2-ev3
:precondition (and (tracePointer ev2) (allowed))
:effect (and (not (tracePointer ev2)) (tracePointer ev3) (increase (total-cost) 1)
))

(:action moveInTheLog#b#ev3-ev4
:precondition (and (tracePointer ev3) (allowed))
:effect (and (not (tracePointer ev3)) (tracePointer ev4) (increase (total-cost) 1)
))

(:action moveInTheLog#c#ev4-evEND
:precondition (and (tracePointer ev4) (allowed))
:effect (and (not (tracePointer ev4)) (tracePointer evEND) (increase (total-cost) 1)
))

)