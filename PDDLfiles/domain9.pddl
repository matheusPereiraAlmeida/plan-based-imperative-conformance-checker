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

(:action moveSync#activityo#ev10
:precondition (and (token 958) (tracePointer ev10))
:effect (and (allowed) (not (token 958)) (token 952) (not (tracePointer ev10)) (tracePointer ev11))
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

(:action moveSync#activityu#ev14
:precondition (and (token 960) (tracePointer ev14))
:effect (and (allowed) (not (token 960)) (token 962) (not (tracePointer ev14)) (tracePointer ev15))
)

(:action moveInTheModel#activityu
:precondition (token 960)
:effect (and (not (allowed)) (not (token 960)) (token 962) (increase (total-cost) 1)
)
)

(:action moveSync#activityb#ev23
:precondition (and (token 951) (tracePointer ev23))
:effect (and (allowed) (not (token 951)) (token 924) (not (tracePointer ev23)) (tracePointer evEND))
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

(:action moveSync#activityt#ev9
:precondition (and (token 961) (tracePointer ev9))
:effect (and (allowed) (not (token 961)) (token 960) (not (tracePointer ev9)) (tracePointer ev10))
)

(:action moveInTheModel#activityt
:precondition (token 961)
:effect (and (not (allowed)) (not (token 961)) (token 960) (increase (total-cost) 1)
)
)

(:action moveSync#activityf#ev6
:precondition (and (token 966) (tracePointer ev6))
:effect (and (allowed) (not (token 966)) (token 951) (not (tracePointer ev6)) (tracePointer ev7))
)

(:action moveSync#activityf#ev22
:precondition (and (token 966) (tracePointer ev22))
:effect (and (allowed) (not (token 966)) (token 951) (not (tracePointer ev22)) (tracePointer ev23))
)

(:action moveInTheModel#activityf
:precondition (token 966)
:effect (and (not (allowed)) (not (token 966)) (token 951) (increase (total-cost) 1)
)
)

(:action moveSync#activityh#ev18
:precondition (and (token 964) (tracePointer ev18))
:effect (and (allowed) (not (token 964)) (token 949) (not (tracePointer ev18)) (tracePointer ev19))
)

(:action moveInTheModel#activityh
:precondition (token 964)
:effect (and (not (allowed)) (not (token 964)) (token 949) (increase (total-cost) 1)
)
)

(:action moveSync#activityi#ev8
:precondition (and (token 974) (tracePointer ev8))
:effect (and (allowed) (not (token 974)) (token 972) (not (tracePointer ev8)) (tracePointer ev9))
)

(:action moveInTheModel#activityi
:precondition (token 974)
:effect (and (not (allowed)) (not (token 974)) (token 972) (increase (total-cost) 1)
)
)

(:action moveSync#activitym#ev7
:precondition (and (token 956) (tracePointer ev7))
:effect (and (allowed) (not (token 956)) (token 955) (not (tracePointer ev7)) (tracePointer ev8))
)

(:action moveInTheModel#activitym
:precondition (token 956)
:effect (and (not (allowed)) (not (token 956)) (token 955) (increase (total-cost) 1)
)
)

(:action moveSync#activityg#ev5
:precondition (and (token 951) (tracePointer ev5))
:effect (and (allowed) (not (token 951)) (token 963) (not (tracePointer ev5)) (tracePointer ev6))
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

(:action moveSync#activityq#ev15
:precondition (and (token 952) (tracePointer ev15))
:effect (and (allowed) (not (token 952)) (token 953) (not (tracePointer ev15)) (tracePointer ev16))
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

(:action moveSync#activityp#ev17
:precondition (and (token 953) (tracePointer ev17))
:effect (and (allowed) (not (token 953)) (token 959) (not (tracePointer ev17)) (tracePointer ev18))
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

(:action moveSync#activityc#ev19
:precondition (and (token 949) (tracePointer ev19))
:effect (and (allowed) (not (token 949)) (token 965) (not (tracePointer ev19)) (tracePointer ev20))
)

(:action moveInTheModel#activityc
:precondition (token 949)
:effect (and (not (allowed)) (not (token 949)) (token 965) (increase (total-cost) 1)
)
)

(:action moveSync#activityn#ev11
:precondition (and (token 955) (tracePointer ev11))
:effect (and (allowed) (not (token 955)) (token 957) (not (tracePointer ev11)) (tracePointer ev12))
)

(:action moveInTheModel#activityn
:precondition (token 955)
:effect (and (not (allowed)) (not (token 955)) (token 957) (increase (total-cost) 1)
)
)

(:action moveSync#activityl#ev12
:precondition (and (token 970) (tracePointer ev12))
:effect (and (allowed) (not (token 970)) (token 971) (not (tracePointer ev12)) (tracePointer ev13))
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

(:action moveSync#activityd#ev20
:precondition (and (token 965) (tracePointer ev20))
:effect (and (allowed) (not (token 965)) (token 967) (not (tracePointer ev20)) (tracePointer ev21))
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

(:action moveSync#activityk#ev13
:precondition (and (token 968) (tracePointer ev13))
:effect (and (allowed) (not (token 968)) (token 969) (not (tracePointer ev13)) (tracePointer ev14))
)

(:action moveInTheModel#activityk
:precondition (token 968)
:effect (and (not (allowed)) (not (token 968)) (token 969) (increase (total-cost) 1)
)
)

(:action moveSync#activityj#ev16
:precondition (and (token 973) (tracePointer ev16))
:effect (and (allowed) (not (token 973)) (token 954) (not (tracePointer ev16)) (tracePointer ev17))
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

(:action moveSync#activitye#ev21
:precondition (and (token 967) (tracePointer ev21))
:effect (and (allowed) (not (token 967)) (token 966) (not (tracePointer ev21)) (tracePointer ev22))
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

(:action moveInTheLog#activityg#ev5-ev6
:precondition (and (tracePointer ev5) (allowed))
:effect (and (not (tracePointer ev5)) (tracePointer ev6) (increase (total-cost) 1)
))

(:action moveInTheLog#activityf#ev6-ev7
:precondition (and (tracePointer ev6) (allowed))
:effect (and (not (tracePointer ev6)) (tracePointer ev7) (increase (total-cost) 1)
))

(:action moveInTheLog#activitym#ev7-ev8
:precondition (and (tracePointer ev7) (allowed))
:effect (and (not (tracePointer ev7)) (tracePointer ev8) (increase (total-cost) 1)
))

(:action moveInTheLog#activityi#ev8-ev9
:precondition (and (tracePointer ev8) (allowed))
:effect (and (not (tracePointer ev8)) (tracePointer ev9) (increase (total-cost) 1)
))

(:action moveInTheLog#activityt#ev9-ev10
:precondition (and (tracePointer ev9) (allowed))
:effect (and (not (tracePointer ev9)) (tracePointer ev10) (increase (total-cost) 1)
))

(:action moveInTheLog#activityo#ev10-ev11
:precondition (and (tracePointer ev10) (allowed))
:effect (and (not (tracePointer ev10)) (tracePointer ev11) (increase (total-cost) 1)
))

(:action moveInTheLog#activityn#ev11-ev12
:precondition (and (tracePointer ev11) (allowed))
:effect (and (not (tracePointer ev11)) (tracePointer ev12) (increase (total-cost) 1)
))

(:action moveInTheLog#activityl#ev12-ev13
:precondition (and (tracePointer ev12) (allowed))
:effect (and (not (tracePointer ev12)) (tracePointer ev13) (increase (total-cost) 1)
))

(:action moveInTheLog#activityk#ev13-ev14
:precondition (and (tracePointer ev13) (allowed))
:effect (and (not (tracePointer ev13)) (tracePointer ev14) (increase (total-cost) 1)
))

(:action moveInTheLog#activityu#ev14-ev15
:precondition (and (tracePointer ev14) (allowed))
:effect (and (not (tracePointer ev14)) (tracePointer ev15) (increase (total-cost) 1)
))

(:action moveInTheLog#activityq#ev15-ev16
:precondition (and (tracePointer ev15) (allowed))
:effect (and (not (tracePointer ev15)) (tracePointer ev16) (increase (total-cost) 1)
))

(:action moveInTheLog#activityj#ev16-ev17
:precondition (and (tracePointer ev16) (allowed))
:effect (and (not (tracePointer ev16)) (tracePointer ev17) (increase (total-cost) 1)
))

(:action moveInTheLog#activityp#ev17-ev18
:precondition (and (tracePointer ev17) (allowed))
:effect (and (not (tracePointer ev17)) (tracePointer ev18) (increase (total-cost) 1)
))

(:action moveInTheLog#activityh#ev18-ev19
:precondition (and (tracePointer ev18) (allowed))
:effect (and (not (tracePointer ev18)) (tracePointer ev19) (increase (total-cost) 1)
))

(:action moveInTheLog#activityc#ev19-ev20
:precondition (and (tracePointer ev19) (allowed))
:effect (and (not (tracePointer ev19)) (tracePointer ev20) (increase (total-cost) 1)
))

(:action moveInTheLog#activityd#ev20-ev21
:precondition (and (tracePointer ev20) (allowed))
:effect (and (not (tracePointer ev20)) (tracePointer ev21) (increase (total-cost) 1)
))

(:action moveInTheLog#activitye#ev21-ev22
:precondition (and (tracePointer ev21) (allowed))
:effect (and (not (tracePointer ev21)) (tracePointer ev22) (increase (total-cost) 1)
))

(:action moveInTheLog#activityf#ev22-ev23
:precondition (and (tracePointer ev22) (allowed))
:effect (and (not (tracePointer ev22)) (tracePointer ev23) (increase (total-cost) 1)
))

(:action moveInTheLog#activityb#ev23-evEND
:precondition (and (tracePointer ev23) (allowed))
:effect (and (not (tracePointer ev23)) (tracePointer evEND) (increase (total-cost) 1)
))

)