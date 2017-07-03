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

(:action moveInTheModel#activityo
:precondition (token 958)
:effect (and (not (allowed)) (not (token 958)) (token 952) (increase (total-cost) 1)
)
)

(:action moveInTheModel#generatedinv0
:precondition (and (token 969) (token 971))
:effect (and (not (allowed)) (not (token 969)) (not (token 971)) (token 973) (increase (total-cost) 0)
)
)

(:action moveInTheModel#activityu
:precondition (token 960)
:effect (and (not (allowed)) (not (token 960)) (token 962) (increase (total-cost) 1)
)
)

(:action moveSync#activityb#ev5
:precondition (and (token 951) (tracePointer ev5))
:effect (and (allowed) (not (token 951)) (token 924) (not (tracePointer ev5)) (tracePointer ev6))
)

(:action moveInTheModel#activityb
:precondition (token 951)
:effect (and (not (allowed)) (not (token 951)) (token 924) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activitys
:precondition (token 952)
:effect (and (not (allowed)) (not (token 952)) (token 953) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityt
:precondition (token 961)
:effect (and (not (allowed)) (not (token 961)) (token 960) (increase (total-cost) 1)
)
)

(:action moveSync#activityf#ev6
:precondition (and (token 966) (tracePointer ev6))
:effect (and (allowed) (not (token 966)) (token 951) (not (tracePointer ev6)) (tracePointer evEND))
)

(:action moveInTheModel#activityf
:precondition (token 966)
:effect (and (not (allowed)) (not (token 966)) (token 951) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityh
:precondition (token 964)
:effect (and (not (allowed)) (not (token 964)) (token 949) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityi
:precondition (token 974)
:effect (and (not (allowed)) (not (token 974)) (token 972) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activitym
:precondition (token 956)
:effect (and (not (allowed)) (not (token 956)) (token 955) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityg
:precondition (token 951)
:effect (and (not (allowed)) (not (token 951)) (token 963) (increase (total-cost) 1)
)
)

(:action moveInTheModel#generatedinv1
:precondition (token 963)
:effect (and (not (allowed)) (not (token 963)) (token 961) (token 956) (token 958) (token 974) (increase (total-cost) 0)
)
)

(:action moveInTheModel#generatedinv2
:precondition (token 972)
:effect (and (not (allowed)) (not (token 972)) (token 968) (token 970) (increase (total-cost) 0)
)
)

(:action moveInTheModel#activityq
:precondition (token 952)
:effect (and (not (allowed)) (not (token 952)) (token 953) (increase (total-cost) 1)
)
)

(:action moveSync#activitya#ev1
:precondition (and (token 923) (tracePointer ev1))
:effect (and (allowed) (not (token 923)) (token 949) (not (tracePointer ev1)) (tracePointer ev2))
)

(:action moveInTheModel#activitya
:precondition (token 923)
:effect (and (not (allowed)) (not (token 923)) (token 949) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityp
:precondition (token 953)
:effect (and (not (allowed)) (not (token 953)) (token 959) (increase (total-cost) 1)
)
)

(:action moveSync#activityc#ev2
:precondition (and (token 949) (tracePointer ev2))
:effect (and (allowed) (not (token 949)) (token 965) (not (tracePointer ev2)) (tracePointer ev3))
)

(:action moveInTheModel#activityc
:precondition (token 949)
:effect (and (not (allowed)) (not (token 949)) (token 965) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityn
:precondition (token 955)
:effect (and (not (allowed)) (not (token 955)) (token 957) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityl
:precondition (token 970)
:effect (and (not (allowed)) (not (token 970)) (token 971) (increase (total-cost) 1)
)
)

(:action moveSync#activityd#ev3
:precondition (and (token 965) (tracePointer ev3))
:effect (and (allowed) (not (token 965)) (token 967) (not (tracePointer ev3)) (tracePointer ev4))
)

(:action moveInTheModel#activityd
:precondition (token 965)
:effect (and (not (allowed)) (not (token 965)) (token 967) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityr
:precondition (token 952)
:effect (and (not (allowed)) (not (token 952)) (token 953) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityk
:precondition (token 968)
:effect (and (not (allowed)) (not (token 968)) (token 969) (increase (total-cost) 1)
)
)

(:action moveInTheModel#activityj
:precondition (token 973)
:effect (and (not (allowed)) (not (token 973)) (token 954) (increase (total-cost) 1)
)
)

(:action moveSync#activitye#ev4
:precondition (and (token 967) (tracePointer ev4))
:effect (and (allowed) (not (token 967)) (token 966) (not (tracePointer ev4)) (tracePointer ev5))
)

(:action moveInTheModel#activitye
:precondition (token 967)
:effect (and (not (allowed)) (not (token 967)) (token 966) (increase (total-cost) 1)
)
)

(:action moveInTheModel#generatedinv3
:precondition (and (token 962) (token 959) (token 954) (token 957))
:effect (and (not (allowed)) (not (token 962)) (not (token 959)) (not (token 954)) (not (token 957)) (token 964) (increase (total-cost) 0)
)
)

(:action moveInTheLog#activitya#ev1-ev2
:precondition (and (tracePointer ev1) (allowed))
:effect (and (not (tracePointer ev1)) (tracePointer ev2) (increase (total-cost) 1)
))

(:action moveInTheLog#activityc#ev2-ev3
:precondition (and (tracePointer ev2) (allowed))
:effect (and (not (tracePointer ev2)) (tracePointer ev3) (increase (total-cost) 1)
))

(:action moveInTheLog#activityd#ev3-ev4
:precondition (and (tracePointer ev3) (allowed))
:effect (and (not (tracePointer ev3)) (tracePointer ev4) (increase (total-cost) 1)
))

(:action moveInTheLog#activitye#ev4-ev5
:precondition (and (tracePointer ev4) (allowed))
:effect (and (not (tracePointer ev4)) (tracePointer ev5) (increase (total-cost) 1)
))

(:action moveInTheLog#activityb#ev5-ev6
:precondition (and (tracePointer ev5) (allowed))
:effect (and (not (tracePointer ev5)) (tracePointer ev6) (increase (total-cost) 1)
))

(:action moveInTheLog#activityf#ev6-evEND
:precondition (and (tracePointer ev6) (allowed))
:effect (and (not (tracePointer ev6)) (tracePointer evEND) (increase (total-cost) 1)
))

)