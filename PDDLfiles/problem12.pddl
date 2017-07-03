(define (problem Align) (:domain Mining)
(:objects
957 - place
961 - place
969 - place
955 - place
949 - place
958 - place
960 - place
972 - place
956 - place
959 - place
964 - place
973 - place
952 - place
954 - place
971 - place
970 - place
923 - place
968 - place
967 - place
953 - place
924 - place
965 - place
951 - place
962 - place
963 - place
966 - place
974 - place
ev1 - event
ev2 - event
ev3 - event
ev4 - event
ev5 - event
ev6 - event
ev7 - event
ev8 - event
ev9 - event
ev10 - event
ev11 - event
ev12 - event
ev13 - event
ev14 - event
ev15 - event
ev16 - event
ev17 - event
ev18 - event
ev19 - event
ev20 - event
ev21 - event
ev22 - event
ev23 - event
ev24 - event
ev25 - event
ev26 - event
ev27 - event
ev28 - event
ev29 - event
ev30 - event
ev31 - event
ev32 - event
ev33 - event
ev34 - event
ev35 - event
ev36 - event
ev37 - event
ev38 - event
ev39 - event
ev40 - event
evEND - event
)
(:init
(tracePointer ev1)
(allowed)
(token 923)
(= (total-cost) 0)
)
(:goal
(and
(not (token 957))
(not (token 961))
(not (token 969))
(not (token 955))
(not (token 949))
(not (token 958))
(not (token 960))
(not (token 972))
(not (token 956))
(not (token 959))
(not (token 964))
(not (token 973))
(not (token 952))
(not (token 954))
(not (token 971))
(not (token 970))
(not (token 923))
(not (token 968))
(not (token 967))
(not (token 953))
(token 924)
(not (token 965))
(not (token 951))
(not (token 962))
(not (token 963))
(not (token 966))
(not (token 974))
(tracePointer evEND)
))
(:metric minimize (total-cost))
)