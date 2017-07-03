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

(:action moveSync#activityo#ev11
:precondition (and (token 958) (tracePointer ev11))
:effect (and (allowed) (not (token 958)) (token 952) (not (tracePointer ev11)) (tracePointer ev12))
)

(:action moveSync#activityo#ev27
:precondition (and (token 958) (tracePointer ev27))
:effect (and (allowed) (not (token 958)) (token 952) (not (tracePointer ev27)) (tracePointer ev28))
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

(:action moveSync#activityu#ev31
:precondition (and (token 960) (tracePointer ev31))
:effect (and (allowed) (not (token 960)) (token 962) (not (tracePointer ev31)) (tracePointer ev32))
)

(:action moveInTheModel#activityu
:precondition (token 960)
:effect (and (not (allowed)) (not (token 960)) (token 962) (increase (total-cost) 1)
)
)

(:action moveSync#activityb#ev40
:precondition (and (token 951) (tracePointer ev40))
:effect (and (allowed) (not (token 951)) (token 924) (not (tracePointer ev40)) (tracePointer evEND))
)

(:action moveInTheModel#activityb
:precondition (token 951)
:effect (and (not (allowed)) (not (token 951)) (token 924) (increase (total-cost) 1)
)
)

(:action moveSync#activitys#ev15
:precondition (and (token 952) (tracePointer ev15))
:effect (and (allowed) (not (token 952)) (token 953) (not (tracePointer ev15)) (tracePointer ev16))
)

(:action moveSync#activitys#ev32
:precondition (and (token 952) (tracePointer ev32))
:effect (and (allowed) (not (token 952)) (token 953) (not (tracePointer ev32)) (tracePointer ev33))
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

(:action moveSync#activityt#ev25
:precondition (and (token 961) (tracePointer ev25))
:effect (and (allowed) (not (token 961)) (token 960) (not (tracePointer ev25)) (tracePointer ev26))
)

(:action moveInTheModel#activityt
:precondition (token 961)
:effect (and (not (allowed)) (not (token 961)) (token 960) (increase (total-cost) 1)
)
)

(:action moveSync#activityf#ev5
:precondition (and (token 966) (tracePointer ev5))
:effect (and (allowed) (not (token 966)) (token 951) (not (tracePointer ev5)) (tracePointer ev6))
)

(:action moveSync#activityf#ev24
:precondition (and (token 966) (tracePointer ev24))
:effect (and (allowed) (not (token 966)) (token 951) (not (tracePointer ev24)) (tracePointer ev25))
)

(:action moveSync#activityf#ev39
:precondition (and (token 966) (tracePointer ev39))
:effect (and (allowed) (not (token 966)) (token 951) (not (tracePointer ev39)) (tracePointer ev40))
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

(:action moveSync#activityh#ev35
:precondition (and (token 964) (tracePointer ev35))
:effect (and (allowed) (not (token 964)) (token 949) (not (tracePointer ev35)) (tracePointer ev36))
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

(:action moveSync#activityi#ev23
:precondition (and (token 974) (tracePointer ev23))
:effect (and (allowed) (not (token 974)) (token 972) (not (tracePointer ev23)) (tracePointer ev24))
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

(:action moveSync#activitym#ev26
:precondition (and (token 956) (tracePointer ev26))
:effect (and (allowed) (not (token 956)) (token 955) (not (tracePointer ev26)) (tracePointer ev27))
)

(:action moveInTheModel#activitym
:precondition (token 956)
:effect (and (not (allowed)) (not (token 956)) (token 955) (increase (total-cost) 1)
)
)

(:action moveSync#activityg#ev6
:precondition (and (token 951) (tracePointer ev6))
:effect (and (allowed) (not (token 951)) (token 963) (not (tracePointer ev6)) (tracePointer ev7))
)

(:action moveSync#activityg#ev22
:precondition (and (token 951) (tracePointer ev22))
:effect (and (allowed) (not (token 951)) (token 963) (not (tracePointer ev22)) (tracePointer ev23))
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

(:action moveSync#activitya#ev2
:precondition (and (token 923) (tracePointer ev2))
:effect (and (allowed) (not (token 923)) (token 949) (not (tracePointer ev2)) (tracePointer ev3))
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

(:action moveSync#activityp#ev34
:precondition (and (token 953) (tracePointer ev34))
:effect (and (allowed) (not (token 953)) (token 959) (not (tracePointer ev34)) (tracePointer ev35))
)

(:action moveInTheModel#activityp
:precondition (token 953)
:effect (and (not (allowed)) (not (token 953)) (token 959) (increase (total-cost) 1)
)
)

(:action moveSync#activityc#ev1
:precondition (and (token 949) (tracePointer ev1))
:effect (and (allowed) (not (token 949)) (token 965) (not (tracePointer ev1)) (tracePointer ev2))
)

(:action moveSync#activityc#ev19
:precondition (and (token 949) (tracePointer ev19))
:effect (and (allowed) (not (token 949)) (token 965) (not (tracePointer ev19)) (tracePointer ev20))
)

(:action moveSync#activityc#ev36
:precondition (and (token 949) (tracePointer ev36))
:effect (and (allowed) (not (token 949)) (token 965) (not (tracePointer ev36)) (tracePointer ev37))
)

(:action moveInTheModel#activityc
:precondition (token 949)
:effect (and (not (allowed)) (not (token 949)) (token 965) (increase (total-cost) 1)
)
)

(:action moveSync#activityn#ev10
:precondition (and (token 955) (tracePointer ev10))
:effect (and (allowed) (not (token 955)) (token 957) (not (tracePointer ev10)) (tracePointer ev11))
)

(:action moveSync#activityn#ev30
:precondition (and (token 955) (tracePointer ev30))
:effect (and (allowed) (not (token 955)) (token 957) (not (tracePointer ev30)) (tracePointer ev31))
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

(:action moveSync#activityl#ev29
:precondition (and (token 970) (tracePointer ev29))
:effect (and (allowed) (not (token 970)) (token 971) (not (tracePointer ev29)) (tracePointer ev30))
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

(:action moveSync#activityd#ev21
:precondition (and (token 965) (tracePointer ev21))
:effect (and (allowed) (not (token 965)) (token 967) (not (tracePointer ev21)) (tracePointer ev22))
)

(:action moveSync#activityd#ev37
:precondition (and (token 965) (tracePointer ev37))
:effect (and (allowed) (not (token 965)) (token 967) (not (tracePointer ev37)) (tracePointer ev38))
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

(:action moveSync#activityk#ev28
:precondition (and (token 968) (tracePointer ev28))
:effect (and (allowed) (not (token 968)) (token 969) (not (tracePointer ev28)) (tracePointer ev29))
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

(:action moveSync#activityj#ev33
:precondition (and (token 973) (tracePointer ev33))
:effect (and (allowed) (not (token 973)) (token 954) (not (tracePointer ev33)) (tracePointer ev34))
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

(:action moveSync#activitye#ev20
:precondition (and (token 967) (tracePointer ev20))
:effect (and (allowed) (not (token 967)) (token 966) (not (tracePointer ev20)) (tracePointer ev21))
)

(:action moveSync#activitye#ev38
:precondition (and (token 967) (tracePointer ev38))
:effect (and (allowed) (not (token 967)) (token 966) (not (tracePointer ev38)) (tracePointer ev39))
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

(:action moveInTheLog#activityc#ev1-ev2
:precondition (and (tracePointer ev1) (allowed))
:effect (and (not (tracePointer ev1)) (tracePointer ev2) (increase (total-cost) 1)
))

(:action moveInTheLog#activitya#ev2-ev3
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

(:action moveInTheLog#activityf#ev5-ev6
:precondition (and (tracePointer ev5) (allowed))
:effect (and (not (tracePointer ev5)) (tracePointer ev6) (increase (total-cost) 1)
))

(:action moveInTheLog#activityg#ev6-ev7
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

(:action moveInTheLog#activityn#ev10-ev11
:precondition (and (tracePointer ev10) (allowed))
:effect (and (not (tracePointer ev10)) (tracePointer ev11) (increase (total-cost) 1)
))

(:action moveInTheLog#activityo#ev11-ev12
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

(:action moveInTheLog#activitys#ev15-ev16
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

(:action moveInTheLog#activitye#ev20-ev21
:precondition (and (tracePointer ev20) (allowed))
:effect (and (not (tracePointer ev20)) (tracePointer ev21) (increase (total-cost) 1)
))

(:action moveInTheLog#activityd#ev21-ev22
:precondition (and (tracePointer ev21) (allowed))
:effect (and (not (tracePointer ev21)) (tracePointer ev22) (increase (total-cost) 1)
))

(:action moveInTheLog#activityg#ev22-ev23
:precondition (and (tracePointer ev22) (allowed))
:effect (and (not (tracePointer ev22)) (tracePointer ev23) (increase (total-cost) 1)
))

(:action moveInTheLog#activityi#ev23-ev24
:precondition (and (tracePointer ev23) (allowed))
:effect (and (not (tracePointer ev23)) (tracePointer ev24) (increase (total-cost) 1)
))

(:action moveInTheLog#activityf#ev24-ev25
:precondition (and (tracePointer ev24) (allowed))
:effect (and (not (tracePointer ev24)) (tracePointer ev25) (increase (total-cost) 1)
))

(:action moveInTheLog#activityt#ev25-ev26
:precondition (and (tracePointer ev25) (allowed))
:effect (and (not (tracePointer ev25)) (tracePointer ev26) (increase (total-cost) 1)
))

(:action moveInTheLog#activitym#ev26-ev27
:precondition (and (tracePointer ev26) (allowed))
:effect (and (not (tracePointer ev26)) (tracePointer ev27) (increase (total-cost) 1)
))

(:action moveInTheLog#activityo#ev27-ev28
:precondition (and (tracePointer ev27) (allowed))
:effect (and (not (tracePointer ev27)) (tracePointer ev28) (increase (total-cost) 1)
))

(:action moveInTheLog#activityk#ev28-ev29
:precondition (and (tracePointer ev28) (allowed))
:effect (and (not (tracePointer ev28)) (tracePointer ev29) (increase (total-cost) 1)
))

(:action moveInTheLog#activityl#ev29-ev30
:precondition (and (tracePointer ev29) (allowed))
:effect (and (not (tracePointer ev29)) (tracePointer ev30) (increase (total-cost) 1)
))

(:action moveInTheLog#activityn#ev30-ev31
:precondition (and (tracePointer ev30) (allowed))
:effect (and (not (tracePointer ev30)) (tracePointer ev31) (increase (total-cost) 1)
))

(:action moveInTheLog#activityu#ev31-ev32
:precondition (and (tracePointer ev31) (allowed))
:effect (and (not (tracePointer ev31)) (tracePointer ev32) (increase (total-cost) 1)
))

(:action moveInTheLog#activitys#ev32-ev33
:precondition (and (tracePointer ev32) (allowed))
:effect (and (not (tracePointer ev32)) (tracePointer ev33) (increase (total-cost) 1)
))

(:action moveInTheLog#activityj#ev33-ev34
:precondition (and (tracePointer ev33) (allowed))
:effect (and (not (tracePointer ev33)) (tracePointer ev34) (increase (total-cost) 1)
))

(:action moveInTheLog#activityp#ev34-ev35
:precondition (and (tracePointer ev34) (allowed))
:effect (and (not (tracePointer ev34)) (tracePointer ev35) (increase (total-cost) 1)
))

(:action moveInTheLog#activityh#ev35-ev36
:precondition (and (tracePointer ev35) (allowed))
:effect (and (not (tracePointer ev35)) (tracePointer ev36) (increase (total-cost) 1)
))

(:action moveInTheLog#activityc#ev36-ev37
:precondition (and (tracePointer ev36) (allowed))
:effect (and (not (tracePointer ev36)) (tracePointer ev37) (increase (total-cost) 1)
))

(:action moveInTheLog#activityd#ev37-ev38
:precondition (and (tracePointer ev37) (allowed))
:effect (and (not (tracePointer ev37)) (tracePointer ev38) (increase (total-cost) 1)
))

(:action moveInTheLog#activitye#ev38-ev39
:precondition (and (tracePointer ev38) (allowed))
:effect (and (not (tracePointer ev38)) (tracePointer ev39) (increase (total-cost) 1)
))

(:action moveInTheLog#activityf#ev39-ev40
:precondition (and (tracePointer ev39) (allowed))
:effect (and (not (tracePointer ev39)) (tracePointer ev40) (increase (total-cost) 1)
))

(:action moveInTheLog#activityb#ev40-evEND
:precondition (and (tracePointer ev40) (allowed))
:effect (and (not (tracePointer ev40)) (tracePointer evEND) (increase (total-cost) 1)
))

)