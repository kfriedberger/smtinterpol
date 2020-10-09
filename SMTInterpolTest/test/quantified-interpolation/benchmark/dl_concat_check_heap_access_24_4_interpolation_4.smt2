(set-option :print-success false)
(set-option :produce-proofs false)
(set-option :interpolant-check-mode true)
(set-option :produce-interpolants true)
(set-option :print-terms-cse false)

(set-info :smt-lib-version 2.6)
(set-logic UF)
(set-info :source |
  GRASShopper benchmarks.
  Authors: Ruzica Piskac, Thomas Wies, and Damien Zufferey
  URL: http://cs.nyu.edu/wies/software/grasshopper
  See also: GRASShopper - Complete Heap Verification with Mixed Specifications. In TACAS 2014, pages 124-139.

  If this benchmark is satisfiable, GRASShopper reports the following error message:
  tests/spl/dl/dl_concat.spl:24:4-24:Possible heap access through null or dangling reference
  |)
(set-info :category "crafted")
(set-info :status unsat)
(declare-sort Loc 0)
(declare-sort SetLoc 0)
(declare-sort FldBool 0)
(declare-sort FldLoc 0)
(declare-fun null$0 () Loc)
(declare-fun read$0 (FldLoc Loc) Loc)
(declare-fun write$0 (FldLoc Loc Loc) FldLoc)
(declare-fun emptyset$0 () SetLoc)
(declare-fun setenum$0 (Loc) SetLoc)
(declare-fun union$0 (SetLoc SetLoc) SetLoc)
(declare-fun intersection$0 (SetLoc SetLoc) SetLoc)
(declare-fun setminus$0 (SetLoc SetLoc) SetLoc)
(declare-fun Btwn$0 (FldLoc Loc Loc Loc) Bool)
(declare-fun in$0 (Loc SetLoc) Bool)
(declare-fun Alloc$0 () SetLoc)
(declare-fun Axiom_8$0 () Bool)
(declare-fun Axiom_9$0 () Bool)
(declare-fun FP$0 () SetLoc)
(declare-fun FP_Caller$0 () SetLoc)
(declare-fun FP_Caller_1$0 () SetLoc)
(declare-fun dlseg_domain$0 (FldLoc FldLoc Loc Loc Loc Loc) SetLoc)
(declare-fun dlseg_struct$0 (SetLoc FldLoc FldLoc Loc Loc Loc Loc) Bool)
(declare-fun end1$0 () Loc)
(declare-fun end2$0 () Loc)
(declare-fun next$0 () FldLoc)
(declare-fun next_1$0 () FldLoc)
(declare-fun prev$0 () FldLoc)
(declare-fun sk_?X_11$0 () SetLoc)
(declare-fun sk_?X_12$0 () SetLoc)
(declare-fun sk_?X_13$0 () SetLoc)
(declare-fun start1$0 () Loc)
(declare-fun start2$0 () Loc)

(assert (! (forall ((?y Loc))
        (or (not (Btwn$0 next$0 null$0 ?y ?y)) (= null$0 ?y)
            (Btwn$0 next$0 null$0 (read$0 next$0 null$0) ?y))) :named P0))

(assert (! (forall ((?y Loc))
        (or (not (Btwn$0 next$0 end1$0 ?y ?y)) (= end1$0 ?y)
            (Btwn$0 next$0 end1$0 (read$0 next$0 end1$0) ?y))) :named P1))

(assert (! (forall ((?y Loc))
        (or (not (Btwn$0 next$0 end2$0 ?y ?y)) (= end2$0 ?y)
            (Btwn$0 next$0 end2$0 (read$0 next$0 end2$0) ?y))) :named P2))

(assert (! (forall ((?y Loc))
        (or (not (= (read$0 next$0 null$0) null$0))
            (not (Btwn$0 next$0 null$0 ?y ?y)) (= null$0 ?y))) :named P3))

(assert (! (forall ((?y Loc))
        (or (not (= (read$0 next$0 end1$0) end1$0))
            (not (Btwn$0 next$0 end1$0 ?y ?y)) (= end1$0 ?y))) :named P4))

(assert (! (forall ((?y Loc))
        (or (not (= (read$0 next$0 end2$0) end2$0))
            (not (Btwn$0 next$0 end2$0 ?y ?y)) (= end2$0 ?y))) :named P5))

(assert (! (Btwn$0 next$0 null$0 (read$0 next$0 null$0) (read$0 next$0 null$0)) :named P6))

(assert (! (Btwn$0 next$0 end1$0 (read$0 next$0 end1$0) (read$0 next$0 end1$0)) :named P7))

(assert (! (Btwn$0 next$0 end2$0 (read$0 next$0 end2$0) (read$0 next$0 end2$0)) :named P8))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 null$0) null$0)
        (not (= (read$0 next$0 null$0) null$0))
        (not (in$0 null$0 sk_?X_13$0)) (not (in$0 null$0 sk_?X_13$0)))) :named P9))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 null$0) end1$0)
        (not (= (read$0 next$0 end1$0) null$0))
        (not (in$0 end1$0 sk_?X_13$0)) (not (in$0 null$0 sk_?X_13$0)))) :named P10))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 null$0) end2$0)
        (not (= (read$0 next$0 end2$0) null$0))
        (not (in$0 end2$0 sk_?X_13$0)) (not (in$0 null$0 sk_?X_13$0)))) :named P11))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 start1$0) null$0)
        (not (= (read$0 next$0 null$0) start1$0))
        (not (in$0 null$0 sk_?X_13$0)) (not (in$0 start1$0 sk_?X_13$0)))) :named P12))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 start1$0) end1$0)
        (not (= (read$0 next$0 end1$0) start1$0))
        (not (in$0 end1$0 sk_?X_13$0)) (not (in$0 start1$0 sk_?X_13$0)))) :named P13))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 start1$0) end2$0)
        (not (= (read$0 next$0 end2$0) start1$0))
        (not (in$0 end2$0 sk_?X_13$0)) (not (in$0 start1$0 sk_?X_13$0)))) :named P14))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 start2$0) null$0)
        (not (= (read$0 next$0 null$0) start2$0))
        (not (in$0 null$0 sk_?X_13$0)) (not (in$0 start2$0 sk_?X_13$0)))) :named P15))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 start2$0) end1$0)
        (not (= (read$0 next$0 end1$0) start2$0))
        (not (in$0 end1$0 sk_?X_13$0)) (not (in$0 start2$0 sk_?X_13$0)))) :named P16))

(assert (! (or (not Axiom_8$0)
    (or (= (read$0 prev$0 start2$0) end2$0)
        (not (= (read$0 next$0 end2$0) start2$0))
        (not (in$0 end2$0 sk_?X_13$0)) (not (in$0 start2$0 sk_?X_13$0)))) :named P17))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 null$0) null$0)
        (not (= (read$0 next$0 null$0) null$0))
        (not (in$0 null$0 sk_?X_12$0)) (not (in$0 null$0 sk_?X_12$0)))) :named P18))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 null$0) end1$0)
        (not (= (read$0 next$0 end1$0) null$0))
        (not (in$0 end1$0 sk_?X_12$0)) (not (in$0 null$0 sk_?X_12$0)))) :named P19))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 null$0) end2$0)
        (not (= (read$0 next$0 end2$0) null$0))
        (not (in$0 end2$0 sk_?X_12$0)) (not (in$0 null$0 sk_?X_12$0)))) :named P20))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 start1$0) null$0)
        (not (= (read$0 next$0 null$0) start1$0))
        (not (in$0 null$0 sk_?X_12$0)) (not (in$0 start1$0 sk_?X_12$0)))) :named P21))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 start1$0) end1$0)
        (not (= (read$0 next$0 end1$0) start1$0))
        (not (in$0 end1$0 sk_?X_12$0)) (not (in$0 start1$0 sk_?X_12$0)))) :named P22))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 start1$0) end2$0)
        (not (= (read$0 next$0 end2$0) start1$0))
        (not (in$0 end2$0 sk_?X_12$0)) (not (in$0 start1$0 sk_?X_12$0)))) :named P23))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 start2$0) null$0)
        (not (= (read$0 next$0 null$0) start2$0))
        (not (in$0 null$0 sk_?X_12$0)) (not (in$0 start2$0 sk_?X_12$0)))) :named P24))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 start2$0) end1$0)
        (not (= (read$0 next$0 end1$0) start2$0))
        (not (in$0 end1$0 sk_?X_12$0)) (not (in$0 start2$0 sk_?X_12$0)))) :named P25))

(assert (! (or (not Axiom_9$0)
    (or (= (read$0 prev$0 start2$0) end2$0)
        (not (= (read$0 next$0 end2$0) start2$0))
        (not (in$0 end2$0 sk_?X_12$0)) (not (in$0 start2$0 sk_?X_12$0)))) :named P26))

(assert (! (forall ((x Loc))
        (or
            (and (in$0 x (union$0 FP_Caller$0 Alloc$0))
                 (or (in$0 x FP_Caller$0) (in$0 x Alloc$0)))
            (and (not (in$0 x FP_Caller$0)) (not (in$0 x Alloc$0))
                 (not (in$0 x (union$0 FP_Caller$0 Alloc$0)))))) :named P27))

(assert (! (forall ((x Loc))
        (or
            (and (in$0 x (union$0 sk_?X_13$0 sk_?X_12$0))
                 (or (in$0 x sk_?X_13$0) (in$0 x sk_?X_12$0)))
            (and (not (in$0 x sk_?X_13$0)) (not (in$0 x sk_?X_12$0))
                 (not (in$0 x (union$0 sk_?X_13$0 sk_?X_12$0)))))) :named P28))

(assert (! (forall ((x Loc))
        (or
            (and (in$0 x (union$0 FP$0 FP_Caller$0))
                 (or (in$0 x FP$0) (in$0 x FP_Caller$0)))
            (and (not (in$0 x FP$0)) (not (in$0 x FP_Caller$0))
                 (not (in$0 x (union$0 FP$0 FP_Caller$0)))))) :named P29))

(assert (! (forall ((x Loc))
        (or
            (and (in$0 x sk_?X_13$0) (in$0 x sk_?X_12$0)
                 (in$0 x (intersection$0 sk_?X_13$0 sk_?X_12$0)))
            (and (or (not (in$0 x sk_?X_13$0)) (not (in$0 x sk_?X_12$0)))
                 (not (in$0 x (intersection$0 sk_?X_13$0 sk_?X_12$0)))))) :named P30))

(assert (! (forall ((x Loc))
        (or
            (and (in$0 x FP_Caller$0) (in$0 x (setminus$0 FP_Caller$0 FP$0))
                 (not (in$0 x FP$0)))
            (and (or (in$0 x FP$0) (not (in$0 x FP_Caller$0)))
                 (not (in$0 x (setminus$0 FP_Caller$0 FP$0)))))) :named P31))

(assert (! (forall ((y Loc) (x Loc))
        (or (and (= x y) (in$0 x (setenum$0 y)))
            (and (not (= x y)) (not (in$0 x (setenum$0 y)))))) :named P32))

(assert (! (= (read$0 (write$0 next$0 end1$0 start2$0) end1$0) start2$0) :named P33))

(assert (! (or (= null$0 end1$0)
    (= (read$0 next$0 null$0)
      (read$0 (write$0 next$0 end1$0 start2$0) null$0))) :named P34))

(assert (! (or (= end1$0 end1$0)
    (= (read$0 next$0 end1$0)
      (read$0 (write$0 next$0 end1$0 start2$0) end1$0))) :named P35))

(assert (! (or (= end2$0 end1$0)
    (= (read$0 next$0 end2$0)
      (read$0 (write$0 next$0 end1$0 start2$0) end2$0))) :named P36))

(assert (! (= (read$0 next$0 null$0) null$0) :named P37))

(assert (! (= (read$0 next_1$0 null$0) null$0) :named P38))

(assert (! (= (read$0 prev$0 null$0) null$0) :named P39))

(assert (! (forall ((x Loc)) (not (in$0 x emptyset$0))) :named P40))

(assert (! (or
    (and (Btwn$0 next$0 start1$0 null$0 null$0)
         (or (and (= null$0 end1$0) (= start1$0 null$0))
             (and (= (read$0 next$0 end1$0) null$0)
                  (= (read$0 prev$0 start1$0) null$0)
                  (in$0 end1$0 sk_?X_13$0)))
         Axiom_8$0)
    (not
         (dlseg_struct$0 sk_?X_13$0 next$0 prev$0 start1$0 null$0 null$0
           end1$0))) :named P41))

(assert (! (= FP_Caller_1$0 (setminus$0 FP_Caller$0 FP$0)) :named P42))

(assert (! (= Alloc$0 (union$0 FP_Caller$0 Alloc$0)) :named P43))

(assert (! (= sk_?X_11$0 (union$0 sk_?X_13$0 sk_?X_12$0)) :named P44))

(assert (! (= sk_?X_12$0 (dlseg_domain$0 next$0 prev$0 start2$0 null$0 null$0 end2$0)) :named P45))

(assert (! (= FP_Caller$0 (union$0 FP$0 FP_Caller$0)) :named P46))

(assert (! (dlseg_struct$0 sk_?X_13$0 next$0 prev$0 start1$0 null$0 null$0 end1$0) :named P47))

(assert (! (not (= start2$0 null$0)) :named P48))

(assert (! (not (in$0 start2$0 FP$0)) :named P49))

(assert (! (forall ((l1 Loc))
        (or
            (and (Btwn$0 next$0 start2$0 l1 null$0)
                 (in$0 l1
                   (dlseg_domain$0 next$0 prev$0 start2$0 null$0 null$0
                     end2$0))
                 (not (= l1 null$0)))
            (and (or (= l1 null$0) (not (Btwn$0 next$0 start2$0 l1 null$0)))
                 (not
                      (in$0 l1
                        (dlseg_domain$0 next$0 prev$0 start2$0 null$0 null$0
                          end2$0)))))) :named P50))

(assert (! (or
    (and (Btwn$0 next$0 start2$0 null$0 null$0)
         (or (and (= null$0 end2$0) (= start2$0 null$0))
             (and (= (read$0 next$0 end2$0) null$0)
                  (= (read$0 prev$0 start2$0) null$0)
                  (in$0 end2$0 sk_?X_12$0)))
         Axiom_9$0)
    (not
         (dlseg_struct$0 sk_?X_12$0 next$0 prev$0 start2$0 null$0 null$0
           end2$0))) :named P51))

(assert (! (= next_1$0 (write$0 next$0 end1$0 start2$0)) :named P52))

(assert (! (= emptyset$0 (intersection$0 sk_?X_13$0 sk_?X_12$0)) :named P53))

(assert (! (= sk_?X_11$0 FP$0) :named P54))

(assert (! (= sk_?X_13$0 (dlseg_domain$0 next$0 prev$0 start1$0 null$0 null$0 end1$0)) :named P55))

(assert (! (dlseg_struct$0 sk_?X_12$0 next$0 prev$0 start2$0 null$0 null$0 end2$0) :named P56))

(assert (! (not (= start1$0 null$0)) :named P57))

(assert (! (not (in$0 null$0 Alloc$0)) :named P58))

(assert (! (forall ((l1 Loc))
        (or
            (and (Btwn$0 next$0 start1$0 l1 null$0)
                 (in$0 l1
                   (dlseg_domain$0 next$0 prev$0 start1$0 null$0 null$0
                     end1$0))
                 (not (= l1 null$0)))
            (and (or (= l1 null$0) (not (Btwn$0 next$0 start1$0 l1 null$0)))
                 (not
                      (in$0 l1
                        (dlseg_domain$0 next$0 prev$0 start1$0 null$0 null$0
                          end1$0)))))) :named P59))

(assert (! (forall ((?u Loc) (?v Loc) (?z Loc))
        (and
             (or (Btwn$0 (write$0 next$0 end1$0 start2$0) ?z ?u ?v)
                 (not
                      (or
                          (and (Btwn$0 next$0 ?z ?u ?v)
                               (or (Btwn$0 next$0 ?z ?v end1$0)
                                   (and (Btwn$0 next$0 ?z ?v ?v)
                                        (not
                                             (Btwn$0 next$0 ?z end1$0 end1$0)))))
                          (and (not (= end1$0 ?v))
                               (or (Btwn$0 next$0 ?z end1$0 ?v)
                                   (and (Btwn$0 next$0 ?z end1$0 end1$0)
                                        (not (Btwn$0 next$0 ?z ?v ?v))))
                               (Btwn$0 next$0 ?z ?u end1$0)
                               (or (Btwn$0 next$0 start2$0 ?v end1$0)
                                   (and (Btwn$0 next$0 start2$0 ?v ?v)
                                        (not
                                             (Btwn$0 next$0 start2$0 end1$0
                                               end1$0)))))
                          (and (not (= end1$0 ?v))
                               (or (Btwn$0 next$0 ?z end1$0 ?v)
                                   (and (Btwn$0 next$0 ?z end1$0 end1$0)
                                        (not (Btwn$0 next$0 ?z ?v ?v))))
                               (Btwn$0 next$0 start2$0 ?u ?v)
                               (or (Btwn$0 next$0 start2$0 ?v end1$0)
                                   (and (Btwn$0 next$0 start2$0 ?v ?v)
                                        (not
                                             (Btwn$0 next$0 start2$0 end1$0
                                               end1$0))))))))
             (or
                 (and (Btwn$0 next$0 ?z ?u ?v)
                      (or (Btwn$0 next$0 ?z ?v end1$0)
                          (and (Btwn$0 next$0 ?z ?v ?v)
                               (not (Btwn$0 next$0 ?z end1$0 end1$0)))))
                 (and (not (= end1$0 ?v))
                      (or (Btwn$0 next$0 ?z end1$0 ?v)
                          (and (Btwn$0 next$0 ?z end1$0 end1$0)
                               (not (Btwn$0 next$0 ?z ?v ?v))))
                      (Btwn$0 next$0 ?z ?u end1$0)
                      (or (Btwn$0 next$0 start2$0 ?v end1$0)
                          (and (Btwn$0 next$0 start2$0 ?v ?v)
                               (not (Btwn$0 next$0 start2$0 end1$0 end1$0)))))
                 (and (not (= end1$0 ?v))
                      (or (Btwn$0 next$0 ?z end1$0 ?v)
                          (and (Btwn$0 next$0 ?z end1$0 end1$0)
                               (not (Btwn$0 next$0 ?z ?v ?v))))
                      (Btwn$0 next$0 start2$0 ?u ?v)
                      (or (Btwn$0 next$0 start2$0 ?v end1$0)
                          (and (Btwn$0 next$0 start2$0 ?v ?v)
                               (not (Btwn$0 next$0 start2$0 end1$0 end1$0)))))
                 (not (Btwn$0 (write$0 next$0 end1$0 start2$0) ?z ?u ?v))))) :named P60))

(assert (! (forall ((?x Loc)) (Btwn$0 next$0 ?x ?x ?x)) :named P61))

(assert (! (forall ((?x Loc) (?y Loc)) (or (not (Btwn$0 next$0 ?x ?y ?x)) (= ?x ?y))) :named P62))

(assert (! (forall ((?x Loc) (?y Loc) (?z Loc))
        (or (not (Btwn$0 next$0 ?x ?y ?y)) (not (Btwn$0 next$0 ?x ?z ?z))
            (Btwn$0 next$0 ?x ?y ?z) (Btwn$0 next$0 ?x ?z ?y))) :named P63))

(assert (! (forall ((?x Loc) (?y Loc) (?z Loc))
        (or (not (Btwn$0 next$0 ?x ?y ?z))
            (and (Btwn$0 next$0 ?x ?y ?y) (Btwn$0 next$0 ?y ?z ?z)))) :named P64))

(assert (! (forall ((?x Loc) (?y Loc) (?z Loc))
        (or (not (Btwn$0 next$0 ?x ?y ?y)) (not (Btwn$0 next$0 ?y ?z ?z))
            (Btwn$0 next$0 ?x ?z ?z))) :named P65))

(assert (! (forall ((?u Loc) (?x Loc) (?y Loc) (?z Loc))
        (or (not (Btwn$0 next$0 ?x ?y ?z)) (not (Btwn$0 next$0 ?y ?u ?z))
            (and (Btwn$0 next$0 ?x ?y ?u) (Btwn$0 next$0 ?x ?u ?z)))) :named P66))

(assert (! (forall ((?u Loc) (?x Loc) (?y Loc) (?z Loc))
        (or (not (Btwn$0 next$0 ?x ?y ?z)) (not (Btwn$0 next$0 ?x ?u ?y))
            (and (Btwn$0 next$0 ?x ?u ?z) (Btwn$0 next$0 ?u ?y ?z)))) :named P67))

(check-sat)

(get-interpolants (and P50 P24 P59 P46 P3 P23 P31 P62 P5 P13 P18 P55 P64 P66 P11 P54 P10 P40 P47 P39 P20 P65 P44 P7 P22 P36 P25 P67 P9 P35 P51 P0 P21 P16) (and P4 P52 P41 P28 P53 P49 P19 P57 P60 P42 P2 P37 P45 P34 P48 P27 P43 P6 P14 P33 P17 P38 P56 P29 P1 P15 P63 P26 P8 P12 P61 P30 P32 P58))

(exit)